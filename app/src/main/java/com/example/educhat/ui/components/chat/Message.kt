package com.example.educhat.ui.components.chat

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.educhat.ui.components.home.Montserrat

data class Message(
    val content: String,
    val isUser: Boolean,
    val timestamp: String
)

data class Conversation(
    val id: String,
    val messages: List<Message>
)

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
            fontFamily = Montserrat,
            modifier = Modifier
                .background(Color(0xFF360568), RoundedCornerShape(12.dp))
                .padding(12.dp, 13.dp),
            color = Color.White,
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium
        )
        Text(
            text = message.timestamp,
            fontFamily = Montserrat,
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
            fontFamily = Montserrat,
            modifier = Modifier
                .background(Color(0xFFF2F7FB), RoundedCornerShape(12.dp))
                .padding(12.dp),
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium
        )
        Text(
            text = message.timestamp,
            fontFamily = Montserrat,

            fontSize = 10.sp,
            color = Color(0xFF797C7B),
            modifier = Modifier
                .padding(top = 8.dp)
                .align(Alignment.Start)
        )
    }
}