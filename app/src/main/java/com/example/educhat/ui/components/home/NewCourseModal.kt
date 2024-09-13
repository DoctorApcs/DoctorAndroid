package com.example.educhat.ui.components.home
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import okhttp3.*
import org.json.JSONObject
import androidx.compose.foundation.BorderStroke
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Send
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewModelScope
import com.example.educhat.R
import com.example.educhat.ui.theme.CustomPrimaryEnd
import com.example.educhat.ui.theme.CustomPrimaryStart
import kotlinx.coroutines.launch
import org.json.JSONArray
import java.util.concurrent.TimeUnit
import android.util.Log;

class KnowledgeBaseViewModel : ViewModel() {
    private var webSocket: WebSocket? = null
    private val client = OkHttpClient.Builder()
        .readTimeout(30, TimeUnit.SECONDS)
        .connectTimeout(30, TimeUnit.SECONDS)
        .build()

    var query by mutableStateOf("")
    var maxSections by mutableStateOf("3")
    var includeHumanFeedback by mutableStateOf(true)
    var followGuidelines by mutableStateOf(true)
    var verbose by mutableStateOf(true)
    var model by mutableStateOf("gpt-4o-mini")
    var sections by mutableStateOf(listOf<String>())
    var isLoading by mutableStateOf(false)
    var logs by mutableStateOf("")
    var isHumanFeedbackRequired by mutableStateOf(false)
    var feedbackText by mutableStateOf("")
    var isCompleted by mutableStateOf(false)
    var sources by mutableStateOf(listOf<String>())
    private val API_BASE_URL = "ws://10.0.2.2:8000/api/knowledge_base/generate_course"

    init {
        connectWebSocket()
    }

    private fun connectWebSocket() {
        val request = Request.Builder().url(API_BASE_URL).build()
        webSocket = client.newWebSocket(request, object : WebSocketListener() {
            override fun onOpen(webSocket: WebSocket, response: Response) {
                viewModelScope.launch {
                    // Handle successful connection
                }
            }

            override fun onMessage(webSocket: WebSocket, text: String) {
                viewModelScope.launch {
                    handleWebSocketMessage(text)
                }
            }

            override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
                viewModelScope.launch {
                    // Handle connection failure
                    logs = "WebSocket connection failed: ${t.message}"
                    Log.e("ERROR","WebSocket connection failed: ${t.message}")
                }
            }

            override fun onClosed(webSocket: WebSocket, code: Int, reason: String) {
                viewModelScope.launch {
                    // Handle connection closure
                    logs = "WebSocket connection closed: $reason"
                    print(logs)
                }
            }
        })
    }

    private fun handleWebSocketMessage(message: String) {
        try {
            val data = JSONObject(message)
            when (data.getString("type")) {
                "human_feedback" -> {
                    val newSections = data.getJSONArray("output")
                    sections = newSections.toList()
                    isHumanFeedbackRequired = true
                    isLoading = false
                    logs = ""
                }
                "kb_created" -> {
                    // Handle KB creation
                    logs = "Knowledge base created successfully"
                }
                "end" -> {
                    isCompleted = true
                    isLoading = false
                }
                "logs" -> {
                    val content = data.getString("content")
                    if (content == "added_source_url") {
                        val url = data.getString("output").split("✅ Added source url to research:")[1].trim()
                        sources = listOf(url) + sources
                    } else {
                        logs = data.getString("output")
                    }
                }
            }
        } catch (e: Exception) {
            logs = "Error parsing message: ${e.message}"
        }
    }

    fun handleSubmit() {
        val formData = JSONObject().apply {
            put("query", query)
            put("max_sections", maxSections.toIntOrNull() ?: 3)
            put("include_human_feedback", includeHumanFeedback)
            put("follow_guidelines", followGuidelines)
            put("model", model)
            put("verbose", verbose)
        }
        webSocket?.send(formData.toString())
        isLoading = true
        logs = "Submitting query..."
    }

    fun handleFeedback() {
        val feedback = feedbackText.trim().ifEmpty { "no" }
        val feedbackData = JSONObject().apply {
            put("type", "human_feedback")
            put("content", feedback)
        }
        webSocket?.send(feedbackData.toString())
        isHumanFeedbackRequired = false
        feedbackText = ""
        isLoading = true
        logs = "Submitting feedback..."
    }

    override fun onCleared() {
        super.onCleared()
        webSocket?.close(1000, "ViewModel cleared")
    }

    private fun JSONArray.toList(): List<String> {
        return (0 until length()).map { getString(it) }
    }
}


@Composable
fun KnowledgeBaseScreen(viewModel: KnowledgeBaseViewModel, onDismiss: () -> Unit) {

    Surface(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .background(Color.White)
                .fillMaxSize()
        ) {
            // Enhanced TopAppBar with custom colors and icons
            TopAppBar(
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            "Course Generator",
                            color = Color.Black,
                            style = TextStyle(
                                fontFamily = FontFamily(Font(R.font.montserrat_semi_bold)),
                                fontSize = 20.sp)
                        )
                    }
                },
                backgroundColor = Color.Transparent,
                elevation = 0.dp
            )

            // Main content with animations
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                Column {
                    CourseOptionsSection(viewModel, onDismiss = { onDismiss() })
                    Spacer(modifier = Modifier.height(16.dp))
                    Divider(color = Color.White.copy(alpha = 0.5f))
                    Spacer(modifier = Modifier.height(16.dp))
                    CourseContentSection(viewModel)
                }

                if (viewModel.isCompleted) {
                    CompletionScreen(onDismiss = onDismiss)
                }
            }
        }
    }
}

@Composable
fun CourseOptionsSection(viewModel: KnowledgeBaseViewModel, onDismiss: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(8.dp, shape = MaterialTheme.shapes.medium),
        backgroundColor = Color.White.copy(alpha = 0.9f),
        elevation = 8.dp
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "Create a New Course",
                style = TextStyle(
                    fontFamily = FontFamily(Font(R.font.montserrat_semi_bold)),
                    fontSize = 20.sp),
                color = CustomPrimaryStart
            )
            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = viewModel.query,
                onValueChange = { viewModel.query = it },
                label = { Text("Course Topic") },
                modifier = Modifier.fillMaxWidth(),
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedBorderColor = CustomPrimaryEnd,
                    focusedLabelColor = CustomPrimaryEnd
                )
            )

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = viewModel.maxSections,
                onValueChange = { viewModel.maxSections = it },
                label = { Text("Number of Sections") },
                modifier = Modifier.fillMaxWidth(),
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedBorderColor = CustomPrimaryEnd,
                    focusedLabelColor = CustomPrimaryEnd
                )
            )

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                OutlinedButton(
                    onClick = { onDismiss() },
                    border = BorderStroke(1.dp, Color(0xFF0D47A1)),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFF0D47A1))
                ) {
                    Text("Cancel")
                }
                Spacer(modifier = Modifier.width(8.dp))
                Button(
                    onClick = { viewModel.handleSubmit() },
                    colors = ButtonDefaults.buttonColors(backgroundColor = CustomPrimaryStart)
                ) {
                    Icon(Icons.Default.Send, contentDescription = "Create", tint = Color.White)
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Create", color = Color.White)
                }
            }
        }
    }
}

@Composable
fun CourseContentSection(viewModel: KnowledgeBaseViewModel) {
    Column {
        Text(
            "Course Content",
            style = TextStyle(
                fontFamily = FontFamily(Font(R.font.montserrat_semi_bold)),
                fontSize = 20.sp),
            color = Color.Black
        )

        Spacer(modifier = Modifier.height(8.dp))

        if (viewModel.sections.isNotEmpty()) {
            LazyColumn {
                items(viewModel.sections) { section ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        backgroundColor = Color.White.copy(alpha = 0.9f),
                        elevation = 4.dp
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_link),
                                contentDescription = "Section Icon",
                                tint = Color(0xFF0D47A1),
                                modifier = Modifier.padding(start = 16.dp)
                            )
                            Text(
                                text = section,
                                modifier = Modifier
                                    .padding(16.dp)
                                    .weight(1f),
                                style = MaterialTheme.typography.body1
                            )
                        }
                    }
                }
            }
        } else if (viewModel.isLoading) {
            // Display a custom loading animation
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 32.dp),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(
                    color = Color.White,
                    strokeWidth = 4.dp,
                    modifier = Modifier.size(64.dp)
                )
            }
        }

        if (viewModel.isHumanFeedbackRequired) {
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "We value your feedback! Please provide any suggestions:",
                style = MaterialTheme.typography.subtitle1,
                color = Color.White
            )
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = viewModel.feedbackText,
                onValueChange = { viewModel.feedbackText = it },
                label = { Text("Your Feedback") },
                leadingIcon = {
                    Icon(Icons.Default.Favorite, contentDescription = "Feedback Icon")
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White.copy(alpha = 0.9f)),
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedBorderColor = Color.White,
                    focusedLabelColor = Color.White,
                    cursorColor = Color.White
                )
            )
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                OutlinedButton(
                    onClick = {
                        viewModel.feedbackText = ""
                        viewModel.handleFeedback()
                    },
                    border = BorderStroke(1.dp, Color.White),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.White)
                ) {
                    Text("Skip")
                }
                Spacer(modifier = Modifier.width(8.dp))
                Button(
                    onClick = { viewModel.handleFeedback() },
                    colors = ButtonDefaults.buttonColors(backgroundColor = Color.White)
                ) {
                    Icon(Icons.Default.Send, contentDescription = "Submit Feedback", tint = Color(0xFF0D47A1))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Submit", color = Color(0xFF0D47A1))
                }
            }
        }

        if (viewModel.sources.isNotEmpty()) {
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                "Sources",
                style = MaterialTheme.typography.h6.copy(fontWeight = FontWeight.Bold),
                color = Color.White
            )
            Spacer(modifier = Modifier.height(8.dp))
            LazyRow {
                items(viewModel.sources) { source ->
                    Card(
                        modifier = Modifier
                            .padding(end = 8.dp)
                            .size(120.dp),
                        backgroundColor = Color.White.copy(alpha = 0.9f),
                        elevation = 4.dp
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(8.dp),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Icon(
                                Icons.Default.Email,
                                contentDescription = "Source Icon",
                                tint = Color(0xFF0D47A1),
                                modifier = Modifier.size(40.dp)
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = getHostname(source),
                                style = MaterialTheme.typography.caption,
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun CompletionScreen(onDismiss: () -> Unit) {
    // A fancy completion screen with animations
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            // Success animation or icon
            Icon(
                painter = painterResource(id = R.drawable.more_button),
                contentDescription = "Success",
                tint = Color.White,
                modifier = Modifier.size(100.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                "Course Creation Completed!",
                style = MaterialTheme.typography.h4.copy(fontWeight = FontWeight.Bold),
                color = Color.White,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                "Your new course is ready to use.",
                style = MaterialTheme.typography.subtitle1,
                color = Color.White,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(24.dp))
            Button(
                onClick = { onDismiss() },
                colors = ButtonDefaults.buttonColors(backgroundColor = Color.White)
            ) {
                Text("Back to Home", color = Color(0xFF0D47A1))
            }
        }
    }
}


fun getHostname(url: String): String {
    return try {
        java.net.URL(url).host
    } catch (e: Exception) {
        "Unknown"
    }
}