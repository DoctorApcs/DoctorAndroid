package com.example.educhat
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.google.gson.JsonObject
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import okhttp3.*
import okio.ByteString
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import com.google.gson.Gson

data class Message(
    val content: String,
    val isUser: Boolean,
    val timestamp: String
)

data class Assistant(
    val id: String,
    val name: String
)

data class Conversation(
    val id: String,
    val messages: List<Message>
)

class WebSocketClient(url: String, private val listener: WebSocketListener) {
    private val client = OkHttpClient()
    private var webSocket: WebSocket? = null

    init {
        val request = Request.Builder().url(url).build()
        webSocket = client.newWebSocket(request, listener)
    }

    fun send(message: String) {
        webSocket?.send(message)
    }

    fun close() {
        webSocket?.close(1000, "Closing connection")
    }
}

interface ApiService {
    @GET("api/assistant")
    suspend fun getAssistants(): List<Assistant>

    @GET("api/assistant/{assistantId}/conversations")
    suspend fun getConversations(@Path("assistantId") assistantId: String): List<Conversation>

    @POST("api/assistant/{assistantId}/conversations")
    suspend fun createConversation(@Path("assistantId") assistantId: String): Conversation
}

val apiService: ApiService = Retrofit.Builder()
    .baseUrl("http://10.0.2.2:8000/")
    .addConverterFactory(GsonConverterFactory.create())
    .build()
    .create(ApiService::class.java)

@Composable
fun ChatScreen(navController: NavController, assistantId: String) {
    var assistants by remember { mutableStateOf<List<Assistant>>(emptyList()) }
    var conversations by remember { mutableStateOf<List<Conversation>>(emptyList()) }
    var selectedAssistant by remember { mutableStateOf<Assistant?>(null) }
    var selectedConversation by remember { mutableStateOf<Conversation?>(null) }
    var inputText by remember { mutableStateOf("") }
    var messages by remember { mutableStateOf<List<Message>>(emptyList()) }
    var isLoading by remember { mutableStateOf(false) }
    var streamingMessage by remember { mutableStateOf<Message?>(null) }
    var isAssistantTyping by remember { mutableStateOf(false) }
    var error by remember { mutableStateOf("") }

    val listState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()
    val webSocketClient = remember { mutableStateOf<WebSocketClient?>(null) }
    val gson = remember { Gson() }

    LaunchedEffect(assistantId) {
        assistants = apiService.getAssistants()
        selectedAssistant = assistants.find { it.id == assistantId }
        conversations = apiService.getConversations(assistantId)
        selectedConversation = conversations.maxByOrNull { it.id }

        selectedConversation?.let { conversation ->
            val url = "ws://10.0.2.2:8000/api/assistant/$assistantId/conversations/${conversation.id}/ws"
            webSocketClient.value = WebSocketClient(url, object : WebSocketListener() {
                override fun onMessage(webSocket: WebSocket, text: String) {
                    val data = gson.fromJson(text, JsonObject::class.java)
                    when (data.get("type").asString) {
                        "message" -> {
                            isAssistantTyping = false
                            if (data.get("media_type").asString == "text") {
                                streamingMessage = streamingMessage?.copy(
                                    content = (streamingMessage?.content
                                        ?: "") + data.get("content").asString
                                ) ?: Message(data.get("content").asString, false, "Now")
                            }
                            //                            } else if (data.get("media_type").asString == "video") {
//                                streamingMessage = Message(data.get("content").asString, false, "Now", type = "video")
//                            }
                        }
                        "status" -> {
                            // Handle status updates
                        }
                        "error" -> {
                            error = data.get("content").asString
                        }
                        "end" -> {
                            isAssistantTyping = false
                            streamingMessage?.let { message ->
                                messages = messages + message
                                streamingMessage = null
                            }
                        }
                    }
                }
            })
        }
    }

    DisposableEffect(Unit) {
        onDispose {
            webSocketClient.value?.close()
        }
    }

    Scaffold(
        topBar = {
            ChatHeader(navController, selectedAssistant?.name ?: "")
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .padding(paddingValues)
        ) {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
            ) {
                LazyColumn(
                    state = listState,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 16.dp)
                ) {
                    item { DateIndicator() }
                    items(messages) { message ->
                        if (message.isUser) {
                            UserMessage(message)
                        } else {
                            TutorMessage(message)
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                    }
                    streamingMessage?.let { message ->
                        item {
                            TutorMessage(message)
                            Spacer(modifier = Modifier.height(16.dp))
                        }
                    }
                    if (isAssistantTyping) {
                        item {
                            CircularProgressIndicator(modifier = Modifier.size(24.dp))
                        }
                    }
                    if (error.isNotEmpty()) {
                        item {
                            Text(error, color = Color.Red)
                        }
                    }
                }
            }
            MessageInput(
                value = inputText,
                onValueChange = { inputText = it },
                onSend = {
                    if (inputText.isNotBlank()) {
                        val newMessage = Message(inputText, true, "Now")
                        messages = messages + newMessage
                        webSocketClient.value?.send(gson.toJson(mapOf("content" to inputText)))
                        inputText = ""
                        isAssistantTyping = true
                        error = ""
                        coroutineScope.launch {
                            listState.animateScrollToItem(messages.size - 1)
                        }
                    }
                },
                isLoading = isLoading
            )
        }
    }
}

@Composable
fun ChatHeader(navController: NavController, name: String) {
    TopAppBar(
        title = {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(id = R.drawable.return_icon),
                    contentDescription = "Return Icon",
                    modifier = Modifier
                        .size(34.dp)
                        .clickable { navController.popBackStack() }
                )
                Spacer(modifier = Modifier.width(20.dp))
                Column {
                    Text(
                        text = name,
                        fontFamily = FontFamily(Font(R.font.montserrat_semi_bold)),
                        fontSize = 20.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
        },
        backgroundColor = Color.White,
        elevation = 4.dp
    )
}

@Composable
fun DateIndicator() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "Today",
            modifier = Modifier
                .background(Color(0xFFF8FBF9), RoundedCornerShape(6.dp))
                .padding(horizontal = 12.dp, vertical = 6.dp),
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium,
            color = Color(0xFF000E08)
        )
    }
}

@Composable
fun UserMessage(message: Message) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.End
    ) {
        Text(
            text = message.content,
            fontFamily = FontFamily(Font(R.font.montserrat_regular)),
            modifier = Modifier
                .background(Color(0xFF360568), RoundedCornerShape(12.dp))
                .padding(12.dp, 13.dp),
            color = Color.White,
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium
        )
        Text(
            text = message.timestamp,
            fontFamily = FontFamily(Font(R.font.montserrat_regular)),
            fontSize = 10.sp,
            color = Color(0xFF797C7B),
            modifier = Modifier.padding(top = 8.dp)
        )
    }
}

@Composable
fun TutorMessage(message: Message) {
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
//        Row(verticalAlignment = Alignment.CenterVertically) {
//            Image(
//                painter = painterResource(id = R.drawable.tutor_avatar),
//                contentDescription = "Tutor Avatar",
//                modifier = Modifier
//                    .size(40.dp)
//                    .clip(CircleShape)
//            )
//            Spacer(modifier = Modifier.width(11.dp))
//            Text(
//                text = "Tutor",
//                fontFamily = FontFamily(Font(R.font.montserrat_semi_bold)),
//                fontSize = 14.sp,
//                fontWeight = FontWeight.Medium
//            )
//        }
//        Spacer(modifier = Modifier.height(12.dp))
        Text(
            text = message.content,
            fontFamily = FontFamily(Font(R.font.montserrat_regular)),
            modifier = Modifier
                .background(Color(0xFFF2F7FB), RoundedCornerShape(12.dp))
                .padding(12.dp),
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium
        )
        Text(
            text = message.timestamp,
            fontFamily = FontFamily(Font(R.font.montserrat_regular)),

            fontSize = 10.sp,
            color = Color(0xFF797C7B),
            modifier = Modifier
                .padding(top = 8.dp)
                .align(Alignment.Start)
        )
    }
}

@Composable
fun MessageInput(
    value: String,
    onValueChange: (String) -> Unit,
    onSend: () -> Unit,
    isLoading: Boolean,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(Color.White),
        verticalAlignment = Alignment.CenterVertically
    ) {
        TextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier
                .weight(1f)
                .background(Color(0xFFF3F6F6), RoundedCornerShape(12.dp)),
            placeholder = { Text("Write your message") },
            colors = TextFieldDefaults.textFieldColors(
                backgroundColor = Color.Transparent,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent
            ),
            enabled = !isLoading
        )
        Spacer(modifier = Modifier.width(8.dp))
        Button(
            onClick = onSend,
            modifier = Modifier.size(48.dp),
            shape = CircleShape,
            colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFF360568)),
            enabled = !isLoading
        ) {
            if (isLoading) {
                CircularProgressIndicator(
                    color = Color.White,
                    modifier = Modifier.size(24.dp)
                )
            } else {
                Text("→", color = Color.White)
            }
        }
    }
}