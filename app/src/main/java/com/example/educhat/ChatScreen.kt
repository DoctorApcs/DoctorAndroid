package com.example.educhat

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.educhat.R
import kotlinx.coroutines.launch

data class Message(
    val content: String,
    val isUser: Boolean,
    val timestamp: String
)

@Composable
fun ChatScreen() {
    val messages = remember {
        mutableStateListOf(
            Message("Hello! John abraham", true, "09:25 AM"),
            Message("Hello! Nazrul How are you?", false, "09:26 AM"),
            Message("You did your job well!", true, "09:27 AM"),
            Message("Have a great working week!!", false, "09:28 AM"),
            Message("Hope you like it", false, "09:28 AM"),
            Message("Have a great working week!!", false, "09:28 AM"),
            Message("Have a great working week!!", false, "09:28 AM"),
            Message("Have a great working week!!", false, "09:28 AM"),

        )
    }

    var inputText by remember { mutableStateOf("") }
    val listState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        ChatHeader()
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
            }
        }
        MessageInput(
            value = inputText,
            onValueChange = { inputText = it },
            onSend = {
                if (inputText.isNotBlank()) {
                    messages.add(Message(inputText, true, "Now"))
                    inputText = ""
                    coroutineScope.launch {
                        listState.animateScrollToItem(messages.size - 1)
                    }
                }
            }
        )
    }
}

@Composable
fun ChatHeader() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .padding(horizontal = 30.dp, vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(id = R.drawable.return_icon),
            contentDescription = "Return Icon",
            modifier = Modifier.size(34.dp)
        )
        Spacer(modifier = Modifier.width(20.dp))
        Column {
            Text(
                text = "Calculus 3",
                fontSize = 30.sp,
                fontWeight = FontWeight.SemiBold
            )
            Text(
                text = "Final review",
                fontSize = 20.sp,
                fontWeight = FontWeight.Medium,
                color = Color.Gray
            )
        }
    }
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
            modifier = Modifier
                .background(Color(0xFF360568), RoundedCornerShape(12.dp))
                .padding(12.dp, 13.dp),
            color = Color.White,
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium
        )
        Text(
            text = message.timestamp,
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
        Row(verticalAlignment = Alignment.CenterVertically) {
            Image(
                painter = painterResource(id = R.drawable.tutor_avatar),
                contentDescription = "Tutor Avatar",
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
            )
            Spacer(modifier = Modifier.width(11.dp))
            Text(
                text = "Tutor",
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium
            )
        }
        Spacer(modifier = Modifier.height(12.dp))
        Text(
            text = message.content,
            modifier = Modifier
                .background(Color(0xFFF2F7FB), RoundedCornerShape(12.dp))
                .padding(12.dp),
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium
        )
        Text(
            text = message.timestamp,
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
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(Color.White)
            .padding(16.dp),
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
            )
        )
        Spacer(modifier = Modifier.width(8.dp))
        Button(
            onClick = onSend,
            modifier = Modifier.size(48.dp),
            shape = CircleShape,
            colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFF360568))
        ) {
            Text("→", color = Color.White)
        }
    }
}