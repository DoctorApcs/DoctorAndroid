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

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun MyApp(modifier: Modifier = Modifier) {
    val navController = rememberNavController()
    val courses = listOf(
        Course(
            id = "1",
            title = "Calculus 3",
            description = "Advanced calculus course",
            duration = "1h 20m",
            documentsCount = 5,
            imageResId = R.drawable.ic_course_icon, // Replace with your actual image resource
            barChartData = listOf(
                BarChartData("Mon", 4),
                BarChartData("Tue", 6),
                BarChartData("Wed", 5),
                BarChartData("Thu", 7),
                BarChartData("Fri", 9),
                BarChartData("Sat", 6),
                BarChartData("Sun", 3)
            ),
            pieChartData = listOf(
                PieChartData("Documents", 30),
                PieChartData("Videos", 40),
                PieChartData("Images", 20),
                PieChartData("Misc", 10)
            ),
            progressPercentage = 75 // Example: 75% completed
        ),
        Course(
            id = "2",
            title = "Linear Algebra",
            description = "Introduction to linear algebra",
            duration = "2h 10m",
            documentsCount = 3,
            imageResId = R.drawable.ic_course_icon, // Replace with your actual image resource
            barChartData = listOf(
                BarChartData("Mon", 5),
                BarChartData("Tue", 4),
                BarChartData("Wed", 6),
                BarChartData("Thu", 8),
                BarChartData("Fri", 7),
                BarChartData("Sat", 5),
                BarChartData("Sun", 2)
            ),
            pieChartData = listOf(
                PieChartData("Documents", 50),
                PieChartData("Videos", 30),
                PieChartData("Images", 10),
                PieChartData("Misc", 10)
            ),
            progressPercentage = 50 // Example: 50% completed
        )
        // Add more courses here
    )
    Scaffold(
        modifier = Modifier.fillMaxSize(),
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = "courses",
            modifier = Modifier.padding(innerPadding)
        ) {
            // A composable that is used to navigate to all the screens
            // Here I called the AnalyticsScreen in the "home" router for simplicity, it should be called in the "analytics" router
            composable("home") { HomeScreen(navController = navController) }
            composable("analytics") { AnalyticsScreen(navController = navController) }
            composable("courses") { CourseScreen(navController, courses) }
            composable("course_detail/{courseId}") { backStackEntry ->
                val courseId = backStackEntry.arguments?.getString("courseId")
                if (courseId != null) {
                    CourseDetailsScreen(navController, courseId, courses)
                } else {
                    Text("Course ID is missing", color = Color.Red, modifier = Modifier.padding(16.dp))
                }
            }
        }
    }
}
