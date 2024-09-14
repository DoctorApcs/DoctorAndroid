package com.example.educhat.ui.components.chat

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.educhat.ui.components.home.Montserrat
import java.util.*


data class Assistant(
    val id: String,
    val name: String,
    val description: String,
    val configuration: Map<String, String>,
    val created_at: String
)

@Composable
fun AssistantCard(
    assistant: Assistant,
    onSelect: (Assistant) -> Unit,
    onDelete: (Assistant) -> Unit
) {
    var isMenuOpen by remember { mutableStateOf(false) }
    val randomGradient = remember { getRandomGradient() }
    val badgeText = remember(assistant.created_at) { getBadgeText(assistant.created_at) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(24.dp)
            .clickable { onSelect(assistant) },
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column {
            // Gradient header
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(128.dp)
                    .background(brush = randomGradient)
            ) {
                // Badge
                badgeText?.let {
                    Text(
                        text = it,
                        color = Color.White,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold,
                        fontFamily = Montserrat,
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .padding(8.dp)
                            .background(Color(0xFF6366F1), shape = CircleShape)
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
            }

            // Content
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = assistant.name,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    fontFamily = Montserrat,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = assistant.description,
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Gray,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(16.dp))

                // Model chip
                assistant.configuration["model"]?.let { model ->
                    Surface(
                        shape = RoundedCornerShape(16.dp),
                        color = Color.LightGray.copy(alpha = 0.3f),
                        modifier = Modifier.padding(end = 8.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Build,
                                contentDescription = "Model",
                                tint = Color.Gray,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = model,
                                style = MaterialTheme.typography.bodySmall,
                                fontFamily = Montserrat,
                                color = Color.Gray
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))
            }
        }

        // Menu
        Box(modifier = Modifier.fillMaxSize()) {
            IconButton(
                onClick = { isMenuOpen = !isMenuOpen },
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(8.dp)
            ) {
                Icon(Icons.Default.MoreVert, contentDescription = "More options")
            }

            DropdownMenu(
                expanded = isMenuOpen,
                onDismissRequest = { isMenuOpen = false },
                modifier = Modifier.align(Alignment.BottomEnd)
            ) {
                DropdownMenuItem(
                    text = { Text("Delete", color = Color.Red) },
                    onClick = {
                        isMenuOpen = false
                        onDelete(assistant)
                    }
                )
            }
        }
    }
}

fun getRandomGradient(): Brush {
    val colors = listOf(
        Color(0xFFFCA5A5),
        Color(0xFFFBBF24),
        Color(0xFF34D399),
        Color(0xFF60A5FA),
        Color(0xFFA78BFA)
    )
    val color1 = colors.random()
    val color2 = colors.random()

    return Brush.linearGradient(listOf(color1, color2))
}

fun getBadgeText(createdAt: String): String? {
//    val now = System.currentTimeMillis()
//    val created = Date(createdAt).time
//    val daysSinceCreation = (now - created) / (1000 * 60 * 60 * 24)

//    return when {
//        daysSinceCreation <= 7 -> "New"
//        else -> null
//    }
    return "New"
}