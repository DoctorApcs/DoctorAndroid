package com.example.educhat.ui.components.home
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import okhttp3.*
import org.json.JSONObject

class KnowledgeBaseViewModel : ViewModel() {
    private var webSocket: WebSocket? = null
    private val client = OkHttpClient()

    var query by mutableStateOf("")
    var maxSections by mutableStateOf("3")
    var includeHumanFeedback by mutableStateOf(false)
    var followGuidelines by mutableStateOf(false)
    var verbose by mutableStateOf(false)
    var model by mutableStateOf("gpt-4o-mini")
    var sections by mutableStateOf(listOf<String>())
    var isLoading by mutableStateOf(false)
    var logs by mutableStateOf("")
    var isHumanFeedbackRequired by mutableStateOf(false)
    var feedbackText by mutableStateOf("")
    var isCompleted by mutableStateOf(false)
    var sources by mutableStateOf(listOf<String>())
    private val API_BASE_URL = "ws://localhost:8000/api/knowledge_base/generate_course"

    fun connectWebSocket() {
        val request = Request.Builder().url(API_BASE_URL).build()
        webSocket = client.newWebSocket(request, object : WebSocketListener() {
            override fun onMessage(webSocket: WebSocket, text: String) {
                handleWebSocketMessage(text)
            }
        })
    }

    private fun handleWebSocketMessage(message: String) {
        val data = JSONObject(message)
        when (data.getString("type")) {
            "human_feedback" -> {
                val newSections = data.getJSONArray("output")
                sections = List(newSections.length()) { newSections.getString(it) }
                isHumanFeedbackRequired = true
                isLoading = false
                logs = ""
            }
            "kb_created" -> {
                // Handle KB creation
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
    }

    override fun onCleared() {
        super.onCleared()
        webSocket?.close(1000, "ViewModel cleared")
    }
}

@Composable
fun KnowledgeBaseScreen(viewModel: KnowledgeBaseViewModel) {
    LaunchedEffect(Unit) {
        viewModel.connectWebSocket()
    }

    Column(modifier = Modifier.fillMaxSize()) {
        // Course Options
        CourseOptionsSection(viewModel)

        // Course Content
        CourseContentSection(viewModel)
    }
}

@Composable
fun CourseOptionsSection(viewModel: KnowledgeBaseViewModel) {
    Column(modifier = Modifier.padding(16.dp)) {
        OutlinedTextField(
            value = viewModel.query,
            onValueChange = { viewModel.query = it },
            label = { Text("Query") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = viewModel.maxSections,
            onValueChange = { viewModel.maxSections = it },
            label = { Text("Max Sections") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        Row {
            Checkbox(
                checked = viewModel.includeHumanFeedback,
                onCheckedChange = { viewModel.includeHumanFeedback = it }
            )
            Text("Include Human Feedback")
        }

        Row {
            Checkbox(
                checked = viewModel.followGuidelines,
                onCheckedChange = { viewModel.followGuidelines = it }
            )
            Text("Follow Guidelines")
        }

        Row {
            Checkbox(
                checked = viewModel.verbose,
                onCheckedChange = { viewModel.verbose = it }
            )
            Text("Verbose")
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Model dropdown would go here

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Button(onClick = { /* Handle cancel */ }) {
                Text("Cancel")
            }
            Button(onClick = { viewModel.handleSubmit() }) {
                Text("Create")
            }
        }
    }
}

@Composable
fun CourseContentSection(viewModel: KnowledgeBaseViewModel) {
    Column(modifier = Modifier.padding(16.dp)) {
        Text("Course Content", style = MaterialTheme.typography.h6)

        Spacer(modifier = Modifier.height(8.dp))

        LazyColumn {
            items(viewModel.sections) { section ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    elevation = 4.dp
                ) {
                    Text(
                        text = section,
                        modifier = Modifier.padding(16.dp)
                    )
                }
            }
        }

        if (viewModel.isLoading) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                CircularProgressIndicator(modifier = Modifier.size(24.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text(viewModel.logs)
            }
        }

        if (viewModel.isHumanFeedbackRequired) {
            OutlinedTextField(
                value = viewModel.feedbackText,
                onValueChange = { viewModel.feedbackText = it },
                label = { Text("Enter your feedback") },
                modifier = Modifier.fillMaxWidth()
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Button(onClick = { viewModel.handleFeedback() }) {
                    Text("Submit Feedback")
                }
                Button(onClick = {
                    viewModel.feedbackText = ""
                    viewModel.handleFeedback()
                }) {
                    Text("No Feedback")
                }
            }
        }

        if (viewModel.isCompleted) {
            Text("Course creation completed!", style = MaterialTheme.typography.h6)
        }

        if (viewModel.sources.isNotEmpty()) {
            Text("Sources", style = MaterialTheme.typography.h6)
            LazyRow {
                items(viewModel.sources) { source ->
                    Card(
                        modifier = Modifier
                            .padding(end = 8.dp)
                            .width(120.dp),
                        elevation = 4.dp
                    ) {
                        Text(
                            text = getHostname(source),
                            modifier = Modifier.padding(8.dp)
                        )
                    }
                }
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