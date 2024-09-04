package com.example.educhat.ui.components
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.educhat.R
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items

data class Course(val name: String, val imageRes: Int, val docCount: Int)

@Composable
fun HomeCourseList() {
    val courses = listOf(
        Course("Calculus 3", R.drawable.calculus_bg, 2),
        Course("CS305", R.drawable.cs_bg, 10),
        Course("CS302", R.drawable.cs_bg, 22),
    )

    LazyRow(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(start = 16.dp, end = 16.dp)
    ) {
        items(courses) { course ->
            CourseCard(course)
        }
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
                spotColor = Color.Black.copy(alpha = 0.25f)
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
            )
        }
    }
}