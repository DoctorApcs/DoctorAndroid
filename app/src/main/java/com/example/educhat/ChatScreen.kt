package com.example.educhat

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.educhat.R

@Composable
fun ChatScreen() {
    Column(
        modifier = Modifier
            .background(Color.White)
            .fillMaxWidth()
            .padding(top = 41.dp, bottom = 23.dp)
    ) {
        ChatHeader()
        Spacer(modifier = Modifier.height(82.dp))
        DateIndicator()
        Spacer(modifier = Modifier.height(20.dp))
        UserMessage("Hello! Jhon abraham")
        Spacer(modifier = Modifier.height(22.dp))
        TutorMessage("Hello ! Nazrul How are you?")
        Spacer(modifier = Modifier.height(30.dp))
        UserMessage("You did your job well!")
        Spacer(modifier = Modifier.height(30.dp))
        TutorMessages()
        Spacer(modifier = Modifier.height(30.dp))
        Spacer(modifier = Modifier.height(130.dp))
        MessageInput()
    }
}

@Composable
fun ChatHeader() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 30.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(id = R.drawable.chat_icon),
            contentDescription = "Chat Icon",
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
    Text(
        text = "Today",
        modifier = Modifier
            .background(Color(0xFFF8FBF9), RoundedCornerShape(6.dp))
            .padding(5.dp, 8.dp),
//            .align(Alignment.CenterHorizontally),
        fontSize = 12.sp,
        fontWeight = FontWeight.Medium,
        color = Color(0xFF000E08)
    )
}

@Composable
fun UserMessage(message: String) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.End
    ) {
        Text(
            text = message,
            modifier = Modifier
                .background(Color(0xFF360568))
                .padding(12.dp, 13.dp),
            color = Color.White,
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium
        )
        Text(
            text = "09:25 AM",
            fontSize = 10.sp,
            color = Color(0xFF797C7B),
            modifier = Modifier.padding(top = 8.dp)
        )
    }
}

@Composable
fun TutorMessage(message: String) {
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
            text = message,
            modifier = Modifier
                .background(Color(0xFFF2F7FB))
                .padding(12.dp),
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium
        )
        Text(
            text = "09:25 AM",
            fontSize = 10.sp,
            color = Color(0xFF797C7B),
            modifier = Modifier
                .padding(top = 8.dp)
                .align(Alignment.CenterHorizontally)
        )
    }
}

@Composable
fun TutorMessages() {
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(verticalAlignment = Alignment.Top) {
            Image(
                painter = painterResource(id = R.drawable.tutor_avatar),
                contentDescription = "Tutor Avatar",
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
            )
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                Text(
                    text = "Tutor",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium
                )
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = "Have a great working week!!",
                    modifier = Modifier
                        .background(Color(0xFFF2F7FB))
                        .padding(12.dp, 13.dp),
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium
                )
            }
        }
        Spacer(modifier = Modifier.height(10.dp))
        Text(
            text = "Hope you like it",
            modifier = Modifier
                .background(Color(0xFFF2F7FB))
                .padding(12.dp, 13.dp)
                .padding(start = 62.dp),
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium
        )
        Text(
            text = "09:25 AM",
            fontSize = 10.sp,
            color = Color(0xFF797C7B),
            modifier = Modifier
                .padding(top = 8.dp)
                .align(Alignment.CenterHorizontally)
        )
    }
}

@Composable
fun MessageInput() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 23.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Spacer(modifier = Modifier.width(15.dp))
        TextField(
            value = "",
            onValueChange = {},
            modifier = Modifier
                .weight(1f)
                .background(Color(0xFFF3F6F6), RoundedCornerShape(12.dp)),
            placeholder = { Text("Write your message") },
        )
        Spacer(modifier = Modifier.width(15.dp))

        Spacer(modifier = Modifier.width(15.dp))

    }
}