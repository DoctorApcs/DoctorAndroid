package com.example.educhat
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.foundation.clickable
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.navigation.NavHostController
import androidx.compose.foundation.Canvas
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily

@Composable
fun CourseScreen(navController: NavHostController, courseViewModel: CourseViewModel) {
    // Retrieve the courses from the ViewModel
    val courses = courseViewModel.courses

    var showKnowledgeBaseModal by remember { mutableStateOf(false) }

    if (showKnowledgeBaseModal) {
        KnowledgeBaseModal(
            onDismiss = { showKnowledgeBaseModal = false },
        )
    } else {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .padding(16.dp)
        ) {
            // Custom app bar with back button and title
            CustomAppBar(navController)

            Spacer(modifier = Modifier.height(16.dp))

            // New Course Creation Section
            CreateNewCourseSection(onCreateNewCourse = {
                // Handle new course creation logic here
                showKnowledgeBaseModal = true
            })

            // LazyColumn to display courses
            LazyColumn(
                contentPadding = PaddingValues(vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(courses) { course ->
                    CourseItem(navController, course)
                }
            }
        }
    }
}

@Composable
fun CreateNewCourseSection(onCreateNewCourse: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(120.dp)  // Height for the dashed box
            .clickable { onCreateNewCourse() }  // Make it clickable for course creation
            .padding(16.dp)
    ) {
        Canvas(modifier = Modifier.matchParentSize()) {
            drawRoundRect(
                color = Color(0xFF6200EE),  // Purple color for the dashed border
                style = Stroke(
                    width = 2.dp.toPx(),
                    pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 10f), 0f)
                ),
                cornerRadius = androidx.compose.ui.geometry.CornerRadius(16.dp.toPx())
            )
        }

        // Plus icon in the center
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_add),
                contentDescription = "Add New Course",
                tint = Color(0xFF6200EE),  // Purple color for the icon
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)  // Rounded plus button
            )
        }
    }
}

@Composable
fun CustomAppBar(navController: NavHostController) {
    // Custom Row-based AppBar
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start
    ) {
        // Back button
        IconButton(onClick = {
            navController.navigate("home") {
                popUpTo(navController.graph.startDestinationId) {
                    inclusive = true
                }
            }
        }
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_back),
                contentDescription = "Back",
                tint = Color.Black
            )
        }
        Spacer(modifier = Modifier.width(8.dp))
        // Title
        Text(
            text = "Your Courses",
            style = MaterialTheme.typography.titleLarge,
            fontFamily = FontFamily(Font(R.font.montserrat_regular)),
            color = Color.Black
        )
    }
}

@Composable
fun CourseItem(navController: NavHostController, course: Course) {
    Card(
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp)
    ) {
        Column(
            modifier = Modifier
                .background(Color.White)
                .padding(16.dp)
        ) {
            // Top row with image and title + document count
            Row(verticalAlignment = Alignment.CenterVertically) {
                Image(
                    painter = painterResource(id = course.imageResId),
                    contentDescription = course.title,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(64.dp)
                        .clip(RoundedCornerShape(8.dp))
                )
                Spacer(modifier = Modifier.width(16.dp))

                // Course Title and Document Count
                Column {
                    Text(course.title, fontSize = 18.sp, fontWeight = FontWeight.Bold, fontFamily = FontFamily(Font(R.font.montserrat_regular)),color = Color.Black)
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_document),
                            contentDescription = "Documents",
                            tint = Color.Gray,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("${course.documentsCount} Documents", fontSize = 14.sp, color = Color.Gray)
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Course progress bar and text
            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "${course.progressPercentage}% Complete",
                    fontSize = 14.sp,
                    color = Color.Gray,
                    fontFamily = FontFamily(Font(R.font.montserrat_regular)),
                    modifier = Modifier.padding(bottom = 4.dp)
                )
                LinearProgressIndicator(
                    progress = { course.progressPercentage / 100f }, // Progress percentage (0f to 1f)
                    color = Color(0xFF6200EE), // Purple progress bar
                    trackColor = Color.LightGray,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(8.dp)
                        .clip(RoundedCornerShape(4.dp))
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Duration and Action Buttons
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                // Duration Text
                Text(course.duration, fontSize = 14.sp,fontFamily = FontFamily(Font(R.font.montserrat_regular)), color = Color.Gray)

                // View and Learn Buttons
                Row {
                    // View Button with Download Icon
                    OutlinedButton(
                        onClick = { navController.navigate("course_detail/${course.id}") },
                        shape = CircleShape,
                        colors = ButtonDefaults.outlinedButtonColors(
                            containerColor = Color(0xFF6200EE), // Purple background
                            contentColor = Color.White
                        ),
                        modifier = Modifier.padding(end = 8.dp)
                    ) {
                        Text("View")
                        Spacer(modifier = Modifier.width(4.dp))
                        Icon(
                            painter = painterResource(id = R.drawable.ic_upload),
                            contentDescription = "Download",
                            modifier = Modifier.size(16.dp)
                        )
                    }

                    // Learn Button with Play Icon
                    Button(
                        onClick = { navController.navigate("learn/${course.id}") },
                        shape = CircleShape,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF6200EE), // Purple background
                            contentColor = Color.White
                        )
                    ) {
                        Text("Learn",fontFamily = FontFamily(Font(R.font.montserrat_regular)))
                        Spacer(modifier = Modifier.width(4.dp))
                        Icon(
                            painter = painterResource(id = R.drawable.ic_play_arrow),
                            contentDescription = "Play",
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }
            }
        }
    }
}