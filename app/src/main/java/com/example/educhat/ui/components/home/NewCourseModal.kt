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
import android.util.Log
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.ui.text.style.TextOverflow

// Define Montserrat FontFamily
val Montserrat = FontFamily(
    Font(R.font.montserrat_regular),
    Font(R.font.montserrat_semi_bold, FontWeight.Bold),
    Font(R.font.montserrat_semi_bold, FontWeight.SemiBold)
)

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

    // New state to manage Course Options visibility
    var showCourseOptions by mutableStateOf(true)

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
                    Log.e("ERROR", "WebSocket connection failed: ${t.message}")
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
                    showCourseOptions = false // Hide Course Options when done
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
        showCourseOptions = false // Hide Course Options when submitting
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

@OptIn(ExperimentalMaterial3Api::class)
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
                title = { Text("Your Assistants", fontWeight = FontWeight.Bold, fontFamily = Montserrat)},
                navigationIcon = {
                    IconButton(onClick = { onDismiss() }) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                backgroundColor = Color.White,
                modifier = Modifier.shadow(elevation = 4.dp)
            )

            // Main content using LazyColumn for efficient scrolling
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                // Conditionally add CourseOptionsSection as a header
                if (viewModel.showCourseOptions && !viewModel.isLoading) {
                    item {
                        CourseOptionsSection(viewModel, onDismiss = { onDismiss() })
                        Spacer(modifier = Modifier.height(16.dp))
                    }
                }

                // Add Spinner as an item behind the Course Content Title
                if (viewModel.isLoading) {
                    item {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 16.dp)
                        ) {
                            CircularProgressIndicator(color = CustomPrimaryStart, modifier = Modifier.size(24.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Generating your course...",
                                color = CustomPrimaryStart,
                                style = MaterialTheme.typography.body1
                            )
                        }
                    }
                }

                // Course Content Items
                items(viewModel.sections) { section ->
                    CourseContentItem(section)
                }

                item { CourseContentSection(viewModel) }

                // Display Completion Screen as an Overlay using Box
                if (viewModel.isCompleted) {
                    item {
                        CompletionScreen(onDismiss = onDismiss)
                    }
                }
            }
        }
    }
}

@Composable
fun CourseContentItem(section: String) {
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
                tint = CustomPrimaryStart,
                modifier = Modifier.padding(start = 16.dp)
            )
            Text(
                text = section,
                modifier = Modifier
                    .padding(16.dp)
                    .weight(1f),
                style = MaterialTheme.typography.body1.copy(fontFamily = Montserrat)
            )
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
                    fontFamily = Montserrat,
                    fontSize = 20.sp
                ),
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
                    border = BorderStroke(1.dp, CustomPrimaryStart),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = CustomPrimaryStart)
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
        Spacer(modifier = Modifier.height(8.dp))

        if (viewModel.isHumanFeedbackRequired) {
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "We value your feedback! Please provide any suggestions:",
                style = MaterialTheme.typography.subtitle1.copy(fontFamily = Montserrat),
                color = Color.White
            )
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = viewModel.feedbackText,
                onValueChange = { viewModel.feedbackText = it },
                label = { Text("Your Feedback", fontFamily = Montserrat) },
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
                ),
                textStyle = TextStyle(fontFamily = Montserrat)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // No Feedback Button
                OutlinedButton(
                    onClick = {
                        viewModel.feedbackText = "No feedback"
                        viewModel.handleFeedback()
                    },
                    border = BorderStroke(1.dp, CustomPrimaryStart),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = CustomPrimaryStart)
                ) {
                    Text("No Feedback", fontFamily = Montserrat)
                }

                Row {
                    // Skip Button
                    OutlinedButton(
                        onClick = {
                            viewModel.feedbackText = ""
                            viewModel.handleFeedback()
                        },
                        border = BorderStroke(1.dp, Color.White),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.White)
                    ) {
                        Text("Skip", fontFamily = Montserrat)
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    // Submit Button
                    Button(
                        onClick = { viewModel.handleFeedback() },
                        colors = ButtonDefaults.buttonColors(backgroundColor = Color.White)
                    ) {
                        Icon(Icons.Default.Send, contentDescription = "Submit Feedback", tint = CustomPrimaryStart)
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Submit", color = CustomPrimaryStart, fontFamily = Montserrat)
                    }
                }
            }
        }

        if (viewModel.sources.isNotEmpty()) {
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                "Sources",
                style = MaterialTheme.typography.h6.copy(fontWeight = FontWeight.Bold, fontFamily = Montserrat),
                color = Color.White
            )
            Spacer(modifier = Modifier.height(8.dp))

            // Grid layout for sources
            val chunkedSources = viewModel.sources.chunked(3)
            chunkedSources.forEach { rowSources ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    rowSources.forEach { source ->
                        SourceCard(source)
                    }
                    // Add empty boxes to fill the row if needed
                    repeat(3 - rowSources.size) {
                        Box(modifier = Modifier.weight(1f))
                    }
                }
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }
}

@Composable
fun SourceCard(source: String) {
    Card(
        modifier = Modifier
            .padding(end = 8.dp)
            .size(100.dp),
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
                painter = painterResource(id = R.drawable.ic_link),
                contentDescription = "Section Icon",
                tint = CustomPrimaryStart,
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = getHostname(source),
                style = MaterialTheme.typography.caption.copy(fontFamily = Montserrat),
                textAlign = TextAlign.Center,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
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
                Text("Back to Home", color = CustomPrimaryStart)
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