package com.example.educhat


import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.foundation.clickable

@Composable
fun LessonDetailScreen(navController: NavHostController, courseId: String, lessonId: Int, courseViewModel: CourseViewModel) {
    val course = courseViewModel.getCourseById(courseId) // Get the course by ID
    val lesson = course?.lessons?.find { it.id == lessonId } // Find the specific lesson

    // If lesson is not found, show a fallback message
    if (lesson == null) {
        Text("Lesson not found", style = MaterialTheme.typography.bodyLarge)
        return
    }

    // Now display the lesson content
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        // Back to Lessons Button
        Text(
            text = "← Back to Lessons",
            style = MaterialTheme.typography.bodyLarge,
            color = Color.Blue,
            modifier = Modifier.clickable { navController.popBackStack() } // Navigate back
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Big Title (Lesson Title)
        Text(
            text = lesson.title,
            style = MaterialTheme.typography.displaySmall,
            color = Color.Black,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Iterate through each section in the lesson
        lesson.sections.forEach { section ->
            // Medium Title for Each Section
            Text(
                text = section.sectionTitle,
                style = MaterialTheme.typography.titleMedium,
                color = Color.Black,
                fontWeight = FontWeight.Medium
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Content of Each Section
            Text(
                text = section.sectionContent,
                style = MaterialTheme.typography.bodyLarge,
                color = Color.DarkGray
            )

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}
