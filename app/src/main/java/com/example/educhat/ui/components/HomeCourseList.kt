package com.example.educhat.ui.components
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.educhat.R
import androidx.compose.foundation.Image
import androidx.compose.material3.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource

data class Course(val name: String, val imageRes: Int, val docCount: Int)

@Composable
fun HomeCourseList() {
    val courses = listOf(
        Course("Calculus 3", R.drawable.calculus_bg, 2),
        Course("CS305", R.drawable.cs_bg, 10)
    )

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        courses.forEach { course ->
            CourseCard(course)
        }
    }
}

@Composable
fun CourseCard(course: Course) {
    Card(
        modifier = Modifier
            .width(160.dp)
            .height(120.dp)
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            Image(
                painter = painterResource(id = course.imageRes),
                contentDescription = "${course.name} background",
                modifier = Modifier.fillMaxSize()
            )
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    course.name,
                    style = MaterialTheme.typography.titleMedium,
                    color = Color.White
                )
                Text(
                    "${course.docCount} Docs",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.White
                )
            }
        }
    }
}