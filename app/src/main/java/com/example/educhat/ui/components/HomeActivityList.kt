package com.example.educhat.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.material3.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import com.example.educhat.R

data class Activity(val name: String, val courseName: String, val timeAgo: String)

@Composable
fun HomeActivityList() {
    val activities = listOf(
        Activity("Final Revision", "Calculus 3", "45mins ago"),
        Activity("Final Revision", "CS305", "1hr ago")
    )

    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        activities.forEach { activity ->
            ActivityCard(activity)
        }
    }
}



@Composable
fun ActivityCard(activity: Activity) {
    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(id = R.drawable.chat_icon),
                contentDescription = "Chat Icon",
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(24.dp)
            )
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 16.dp)
            ) {
                Text(
                    activity.name,
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    activity.courseName,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
            Text(
                activity.timeAgo,
                style = MaterialTheme.typography.bodySmall,
                color = Color.Gray
            )
        }
    }
}