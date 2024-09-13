package com.example.educhat

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.educhat.ui.components.chat.Assistant
import com.example.educhat.ui.components.chat.AssistantCard
import com.example.educhat.ui.components.chat.CreateAssistantDialog
import com.example.educhat.ui.components.chat.KnowledgeBase
import com.example.educhat.ui.components.home.Montserrat
import kotlinx.coroutines.launch
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*

// API interface
interface AssistantApi {
    @GET("api/assistant")
    suspend fun getAssistants(): List<Assistant>

    @DELETE("api/assistant/{id}")
    suspend fun deleteAssistant(@Path("id") id: String): Response<Unit>

    @GET("api/knowledge_base")
    suspend fun getKnowledgeBases(): List<KnowledgeBase>

    @POST("api/assistant")
    suspend fun createAssistant(@Body assistant: AssistantCreationRequest): Response<Assistant>
}

data class AssistantCreationRequest(
    val name: String,
    val description: String,
    val systemprompt: String,
    val knowledge_base_id: String,
    val configuration: AssistantConfiguration
)

data class AssistantConfiguration(
    val model: String,
    val service: String = "openai",
    val temperature: String = "0.8"
)

// Retrofit instance
val retrofit = Retrofit.Builder()
    .baseUrl("http://10.0.2.2:8000/")
    .addConverterFactory(GsonConverterFactory.create())
    .build()

val assistantApi = retrofit.create(AssistantApi::class.java)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AssistantCardsScreen(navController: NavController) {
    var assistants by remember { mutableStateOf<List<Assistant>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }
    var error by remember { mutableStateOf<String?>(null) }
    var showCreateDialog by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        fetchAssistants(
            onSuccess = { fetchedAssistants ->
                assistants = fetchedAssistants.sortedByDescending { it.created_at }
                isLoading = false
            },
            onError = { errorMessage ->
                error = errorMessage
                isLoading = false
            }
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Your Assistants", fontWeight = FontWeight.Bold, fontFamily = Montserrat)},
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    IconButton(onClick = { showCreateDialog = true }) {
                        Icon(Icons.Filled.Add, contentDescription = "Create Assistant")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.White,
                    titleContentColor = Color.Black,
                    navigationIconContentColor = Color.Black,
                    actionIconContentColor = Color.Black
                ),
                modifier = Modifier.shadow(elevation = 4.dp)
            )
        }
    ) { innerPadding ->
        when {
            isLoading -> LoadingSpinner()
            error != null -> ErrorComponent(message = error!!)
            else -> {
                LazyColumn(
                    contentPadding = innerPadding,
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(assistants) { assistant ->
                        AssistantCard (
                            assistant = assistant,
                            onSelect = { navController.navigate("chat/${assistant.id}") },
                            onDelete = { deletedAssistant ->
                                coroutineScope.launch {
                                    deleteAssistant(
                                        assistant = deletedAssistant,
                                        onSuccess = {
                                            assistants = assistants.filter { it.id != deletedAssistant.id }
                                        },
                                        onError = { errorMessage ->
                                            error = errorMessage
                                        }
                                    )
                                }
                            }
                        )
                    }
                }
            }
        }
    }

    CreateAssistantDialog(
        isOpen = showCreateDialog,
        onClose = { showCreateDialog = false },
        onCreateSuccess = {
            coroutineScope.launch {
                fetchAssistants(
                    onSuccess = { fetchedAssistants ->
                        assistants = fetchedAssistants.sortedByDescending { it.created_at }
                    },
                    onError = { errorMessage ->
                        error = errorMessage
                    }
                )
            }
        },
        fetchKnowledgeBases = { fetchKnowledgeBases() },
        createAssistant = { name, description, systemPrompt, knowledgeBaseId, model, onSuccess, onError ->
            createAssistant(name, description, systemPrompt, knowledgeBaseId, model, onSuccess, onError)
        }
    )
}

@Composable
fun LoadingSpinner() {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.fillMaxSize()
    ) {
        CircularProgressIndicator()
    }
}

@Composable
fun ErrorComponent(message: String) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.fillMaxSize()
    ) {
        Text(
            text = "Error: $message",
            color = Color.Red,
            style = MaterialTheme.typography.bodyLarge
        )
    }
}

suspend fun fetchAssistants(
    onSuccess: (List<Assistant>) -> Unit,
    onError: (String) -> Unit
) {
    try {
        val assistants = assistantApi.getAssistants()
        onSuccess(assistants)
    } catch (e: Exception) {
        onError("Failed to fetch assistants: ${e.message}")
    }
}

suspend fun deleteAssistant(
    assistant: Assistant,
    onSuccess: () -> Unit,
    onError: (String) -> Unit
) {
    try {
        val response = assistantApi.deleteAssistant(assistant.id)
        if (response.isSuccessful) {
            onSuccess()
        } else {
            onError("Failed to delete assistant: ${response.message()}")
        }
    } catch (e: Exception) {
        onError("Error deleting assistant: ${e.message}")
    }
}

suspend fun fetchKnowledgeBases(): List<KnowledgeBase> {
    return try {
        assistantApi.getKnowledgeBases()
    } catch (e: Exception) {
        println("Error fetching knowledge bases: ${e.message}")
        emptyList()
    }
}

suspend fun createAssistant(
    name: String,
    description: String,
    systemPrompt: String,
    knowledgeBaseId: String,
    model: String,
    onSuccess: () -> Unit,
    onError: (String) -> Unit
) {
    try {
        val assistantRequest = AssistantCreationRequest(
            name = name,
            description = description,
            systemprompt = systemPrompt,
            knowledge_base_id = "1",
            configuration = AssistantConfiguration(
                model = model
            )
        )

        val response = assistantApi.createAssistant(assistantRequest)

        if (response.isSuccessful) {
            onSuccess()
        } else {
            onError("Failed to create assistant: ${response.errorBody()?.string() ?: "Unknown error"}")
        }
    } catch (e: Exception) {
        onError("Error creating assistant: ${e.message}")
    }
}