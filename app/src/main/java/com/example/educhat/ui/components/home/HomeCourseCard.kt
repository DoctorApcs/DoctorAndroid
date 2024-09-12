package com.example.educhat.ui.components.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.educhat.R
import com.example.educhat.ui.theme.CustomPrimaryStart

@Composable
fun AddCourseCard() {
    Box(
        modifier = Modifier
            .width(180.dp)
            .height(180.dp)
            .background(Color.Transparent, RoundedCornerShape(25.dp))
            .dashedBorder(CustomPrimaryStart, 4.dp, 25.dp, 8.dp, 8.dp),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = Icons.Default.Add,
            contentDescription = "Add Course",
            tint = CustomPrimaryStart,
            modifier = Modifier.size(48.dp)
        )
    }
}

@Composable
fun CourseCard(course: Course) {
    Card(
        modifier = Modifier
            .width(180.dp)
            .height(180.dp)
            .shadow(
                elevation = 4.dp,
                shape = RoundedCornerShape(25.dp),
                spotColor = Color.Black.copy(alpha = 0.8f)
            ),
        shape = RoundedCornerShape(25.dp)
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            // Background image
            Image(
                painter = painterResource(id = course.imageRes),
                contentDescription = "${course.name} background",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )

            // Gradient overlay
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                Color(0xFFDDDEDE).copy(alpha = 0.1f),
                                Color(0xFF8A8A8A).copy(alpha = 0.25f),
                                Color(0xFF515151).copy(alpha = 0.7f),
                                Color(0xFF414141)
                            )
                        )
                    )
            )

            // Content
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.Bottom
            ) {
                Text(
                    course.name,
                    style = MaterialTheme.typography.titleMedium,
                    color = Color.White
                )
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.document_icon),
                        contentDescription = "Document icon",
                        tint = Color.White,
                        modifier = Modifier.size(16.dp)
                    )
                    Text(
                        "${course.docCount} Docs",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.White
                    )
                }
            }

            // MoreButton

            Image(
                painter = painterResource(id = R.drawable.more_button),
                contentDescription = "More options",
                modifier = Modifier
                    .size(50.dp)
                    .align(Alignment.TopEnd)
                    .offset(x = (-8).dp, y = 8.dp)
                    .clip(CircleShape)
                    .shadow(
                        elevation = 4.dp,
                        shape = RoundedCornerShape(25.dp),
                        spotColor = Color.Black.copy(alpha = 0.8f)
                    ),
            )
        }
    }
}