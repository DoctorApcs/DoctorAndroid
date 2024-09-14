package com.example.educhat

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.compose.material3.Text
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.educhat.ui.theme.EduChatTheme

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun MyApp(modifier: Modifier = Modifier) {
    val navController = rememberNavController()
    val courseViewModel = CourseViewModel()

    Scaffold(
        modifier = Modifier.fillMaxSize(),
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = "home",
            modifier = Modifier.padding(innerPadding)
        ) {
            // A composable that is used to navigate to all the screens
            // Here I called the AnalyticsScreen in the "home" router for simplicity, it should be called in the "analytics" router
            composable("home") { HomeScreen(navController = navController,courseViewModel) }
            composable("analytics") { AnalyticsScreen(navController = navController) }
            composable("welcome") { WelcomeBackScreen(navController = navController) }
            composable("chat") { ChatScreen(navController = navController) }
            composable("courses") { CourseScreen(navController, courseViewModel) }
            composable("course_detail/{courseId}") { backStackEntry ->
                val courseId = backStackEntry.arguments?.getString("courseId")
                if (courseId != null) {
                    CourseDetailsScreen(navController, courseId, courseViewModel)
                } else {
                    Text("Course ID is missing", color = Color.Red, modifier = Modifier.padding(16.dp))
                }
            }
            composable("lessonDetail/{courseId}/{lessonId}") { backStackEntry ->
                val courseId = backStackEntry.arguments?.getString("courseId") ?: return@composable
                val lessonId = backStackEntry.arguments?.getString("lessonId")?.toIntOrNull() ?: return@composable
                LessonDetailScreen(navController, courseId, lessonId, courseViewModel)
            }
        }
    }
}
