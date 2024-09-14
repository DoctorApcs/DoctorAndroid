package com.example.educhat

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavHostController
import com.example.educhat.ui.components.home.Montserrat
import com.example.educhat.ui.theme.CustomBackground
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.utils.ColorTemplate

val COLORFUL_COLORS = intArrayOf(
    android.graphics.Color.parseColor("#FF6B0ACE"),
    android.graphics.Color.parseColor("#FF360568"),
    android.graphics.Color.parseColor("#FF6B0ACE"),
    android.graphics.Color.parseColor("#FFC8ABE6"),
    android.graphics.Color.parseColor("#FF625b71")
)

@Composable
fun CourseDetailsScreen(navController: NavHostController, courseId: String, courseViewModel: CourseViewModel) {
    var selectedTab by remember { mutableIntStateOf(0) }
    val tabs = listOf("Lessons", "Uploads", "Chat")

    val course = courseViewModel.getCourseById(courseId)
    var showKnowledgeBaseModal by remember { mutableStateOf(false) }


    if (course == null) {
        Text("Course not found", style = MaterialTheme.typography.bodyLarge)
        return
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Background with Box layout
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
        ) {
            Image(
                painter = painterResource(id = course.imageResId),
                contentDescription = "Course Background",
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )



        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        ) {

            IconButton(
                onClick = { navController.popBackStack() },
                modifier = Modifier.align(Alignment.CenterStart)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_back),
                    contentDescription = "Back",
                    tint = Color.Black
                )
            }

            // Course title centered
            Text(
                text = course?.title ?: "Course Details",
                //style = MaterialTheme.typography.h4,
                style = MaterialTheme.typography.headlineMedium,
                color = Color.Black,
                fontFamily = Montserrat,
                modifier = Modifier.align(Alignment.Center) // Center the title
            )
        }
        //Spacer(modifier = Modifier.height(4.dp))

        // Tabs and content section below the background
//        TabRow(selectedTabIndex = selectedTab) {
//            tabs.forEachIndexed { index, title ->
//                Tab(
//                    text = { Text(title) },
//                    selected = selectedTab == index,
//                    onClick = { selectedTab = index }
//                )
//            }
//        }
// Tabs with custom text styling for highlight

        TabRow(
            selectedTabIndex = selectedTab,
            containerColor = Color.White,
            contentColor = Color.Black
        ) {
            tabs.forEachIndexed { index, title ->
                Tab(
                    selected = selectedTab == index,
                    onClick = { selectedTab = index },
                    text = {
                        Text(
                            text = title,
                            fontFamily = FontFamily(Font(R.font.montserrat_regular)),
//                            color = if (selectedTab == index) Color.White else Color.Black,
                            color = Color.Black,
                            fontWeight = if (selectedTab == index) FontWeight.Bold else FontWeight.Normal,
                            modifier = Modifier
                                .padding(horizontal = 2.dp, vertical = 8.dp)
                                .widthIn(min = 80.dp, max = 150.dp),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    },
                    modifier = Modifier
                        .padding(horizontal = 8.dp)
//                        .let {
//                            if (selectedTab == index) {
//                                it.background(
//                                    Brush.verticalGradient(
//                                        colors = listOf(Color(0xFF9C27B0), Color(0xFF673AB7))
//                                    ),
//                                    shape = RoundedCornerShape(16.dp)
//                                )
//                            } else {
//                                it
//                            }
//                        }
                )
            }
        }

        when (selectedTab) {
            0 -> course?.let { DashboardContent(course = it,navController) } // Pass the course object to DashboardContent
            1 -> course?.let { UploadsContent() }   // Assuming you'll pass course data to UploadsContent
            2 -> course?.let { ConversationsContent(navController,course) } // Assuming the same for ConversationsContent
        }
    }
}

@Composable
fun DashboardContent(course: Course,navController:NavHostController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(top = 16.dp)
    ) {
        // Lessons Section
        Text(
            text = "Lessons",
            style = MaterialTheme.typography.headlineMedium.copy(fontFamily = Montserrat),
            modifier = Modifier.padding(16.dp)
        )
//        Spacer(modifier = Modifier.height(8.dp))

        // List of Lessons
        val lessons = course.lessons
        LessonList(navController = navController, lessons = lessons, courseId = course.id)

        Spacer(modifier = Modifier.height(8.dp))
        // Dashboard for the course
        Text("Learning Chart:",fontFamily = FontFamily(Font(R.font.montserrat_regular)), style = MaterialTheme.typography.titleMedium)
        Spacer(modifier = Modifier.height(16.dp))
        BarChartComposable(barChartData = course.barChartData)

        Spacer(modifier = Modifier.height(8.dp))

        Text("Document Pie Chart:",fontFamily = FontFamily(Font(R.font.montserrat_regular)), style = MaterialTheme.typography.titleMedium)
        Spacer(modifier = Modifier.height(16.dp))
        PieChartComposable(pieChartData = course.pieChartData)
    }
}

@Composable
fun UploadsContent() {
    val context = LocalContext.current
    var selectedFileUri by remember { mutableStateOf<Uri?>(null) }
    var embedLink by remember { mutableStateOf("") }
    var showDialog by remember { mutableStateOf(false) }
    val uploadHistory = remember { mutableStateListOf<UploadHistoryItem>() }

    // File picker launcher
    val filePickerLauncher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        uri?.let {
            selectedFileUri = it
            // Add the file to history
            uploadHistory.add(UploadHistoryItem(UploadType.FILE, it.toString(), System.currentTimeMillis()))
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(top = 16.dp)
    ) {
        // Show recent uploads section
        Text(text = "Recent Uploads",fontFamily = FontFamily(Font(R.font.montserrat_regular)), style = MaterialTheme.typography.bodyLarge, color = Color.Black)
        Spacer(modifier = Modifier.height(8.dp))

        // Show history of uploads (sorted by time)
        val sortedHistory = uploadHistory.sortedByDescending { it.timestamp }
        sortedHistory.forEach { item ->
            UploadHistoryItemCard(item)
        }

        Spacer(modifier = Modifier.height(16.dp))

        // FloatingActionButton to upload or embed a link
        FloatingActionButton(
            onClick = { showDialog = true }, // Show dialog to choose between file upload or link embed
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(top = 16.dp),
            containerColor = Color.White
        ) {
            Icon(painter = painterResource(id = R.drawable.ic_upload), contentDescription = "Upload")
        }

        // Show dialog for file upload or link embed
        if (showDialog) {
            ActionDialog(
                onFileUpload = {
                    showDialog = false
                    filePickerLauncher.launch("*/*") // Launch file picker for any file type
                },
                onEmbedLink = {
                    showDialog = false
                    embedLink = "https://youtube.com" // You can prompt for link input here
                    // Add the link to history
                    uploadHistory.add(UploadHistoryItem(UploadType.LINK, embedLink, System.currentTimeMillis()))
                },
                onCancel = {
                    showDialog = false
                }
            )
        }
    }
}

@Composable
fun UploadHistoryItemCard(item: UploadHistoryItem) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        shape = RoundedCornerShape(8.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            val iconId = when (item.type) {
                UploadType.FILE -> R.drawable.ic_file
                UploadType.LINK -> R.drawable.ic_link
            }
            Image(
                painter = painterResource(id = iconId),
                contentDescription = "Upload Item Icon",
                modifier = Modifier.size(40.dp)
            )
            Spacer(modifier = Modifier.width(16.dp))

            Column {
                Text(text = if (item.type == UploadType.FILE) "File" else "Link", style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.Bold)
                Text(text = item.uri, style = MaterialTheme.typography.bodyMedium, color = Color.Gray)
                Text(text = "Uploaded at ${item.timestamp.toFormattedTime()}", style = MaterialTheme.typography.bodySmall, color = Color.Gray)
            }
        }
    }
}

// Utility to format timestamp into a readable date and time string
fun Long.toFormattedTime(): String {
    val sdf = java.text.SimpleDateFormat("dd/MM/yyyy HH:mm:ss", java.util.Locale.getDefault())
    return sdf.format(java.util.Date(this))
}

@Composable
fun ActionDialog(onFileUpload: () -> Unit, onEmbedLink: () -> Unit,onCancel: () -> Unit) {
    Dialog(onDismissRequest = { /* Handle dismiss */ }) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            shape = RoundedCornerShape(8.dp)
        ) {
            Column(
                modifier = Modifier
                    .background(Color.White)
                    .padding(16.dp)
            ) {
                Text(text = "Choose Action", fontFamily = FontFamily(Font(R.font.montserrat_regular)),style = MaterialTheme.typography.bodyLarge, color = Color.Black)

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = onFileUpload,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Upload File")
                }

                Spacer(modifier = Modifier.height(8.dp))

                Button(
                    onClick = onEmbedLink,
                    modifier = Modifier.fillMaxWidth()
                ) {

                    Text("Embed Link",fontFamily = FontFamily(Font(R.font.montserrat_regular)))
                }
                Spacer(modifier = Modifier.height(8.dp))
                // Cancel Button
                Button(
                    onClick = onCancel, // Dismiss the dialog when cancel is clicked
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Gray) // Optional: Change color for cancel button
                ) {
                    Text("Cancel", fontFamily = FontFamily(Font(R.font.montserrat_regular)))
                }
            }
        }
    }
}
@Composable
fun ConversationsContent(navController: NavHostController,course:Course) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()) // Enable scrolling
            .padding(top = 16.dp)
    ) {
        // Conversations list from course activities
        course.activities.forEach { activity ->
            ConversationItemCard(
                navController = navController,
                activity

            )
            Spacer(modifier = Modifier.height(8.dp))
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Centered floating action button at the bottom
        FloatingActionButton(
            onClick = { navController.navigate("chat") },
            modifier = Modifier
                .align(Alignment.CenterHorizontally),
            containerColor = Color.White
        ) {
            Icon(painter = painterResource(id = R.drawable.ic_chat), contentDescription = "Chat")
        }
    }
}

@Composable
fun ConversationItemCard(navController: NavHostController, activity: Activity) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(120.dp)
            .shadow(
                elevation = 6.dp,
                shape = RoundedCornerShape(20.dp)
            )
            .clickable { navController.navigate("chat") },
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        )
    ) {
        Row(
            modifier = Modifier.fillMaxHeight(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .width(75.dp)
                    .fillMaxHeight()
                    .background(
                        CustomBackground
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.chat_icon),
                    contentDescription = "Chat Icon",
                    tint = Color.White,
                    modifier = Modifier.size(30.dp)
                )
            }
            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .padding(16.dp),
            ) {
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .padding(start = 12.dp)
                ) {
                    Text(
                        text = activity.name,
                        style = TextStyle(
                            fontFamily = FontFamily(Font(R.font.montserrat_semi_bold)),
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 15.sp
                        )
                    )
                    Text(
                        text = activity.courseName,
                        style = TextStyle(
                            fontFamily = FontFamily(Font(R.font.montserrat_semi_bold)),
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 12.sp
                        )
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "me: What is derivatives",
                        style = TextStyle(
                            fontFamily = FontFamily(Font(R.font.montserrat_regular)),
                            fontWeight = FontWeight.Normal,
                            fontSize = 10.sp
                        ),
                        color = Color.Gray
                    )
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(
                        modifier = Modifier.height(30.dp),
                        horizontalAlignment = Alignment.End,
                        verticalArrangement = Arrangement.Top) {
//                        TextButton(
//                            onClick = { /* TODO: Implement back to chat action */ },
//                            colors = ButtonDefaults.textButtonColors(contentColor = Color(0xFF7E57C2))
//                        ) {
//                            Text("Back to chat", style = TextStyle(
//                                fontFamily = FontFamily(Font(R.font.montserrat_semi_bold)),
//                                fontWeight = FontWeight.SemiBold,
//                                fontSize = 15.sp,
//                                color = Color.Black,
//                            ))
//                        }
//                        Text(
//                            text = activity.timeAgo,
//                            style = TextStyle(
//                                fontFamily = FontFamily(Font(R.font.montserrat_regular)),
//                                fontWeight = FontWeight.Normal,
//                                fontSize = 10.sp,
//                                color = Color.Black
//                            ),
//                            color = Color.Gray,
//                        )
                    }
                    Box(
                        modifier = Modifier
                            .size(28.dp)
                            .background(
                                CustomBackground,
                                shape = CircleShape
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.arrow_forward),
                            contentDescription = "Go back",
                            tint = Color.White,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun BarChartComposable(barChartData: List<BarChartData>) {
    val context = LocalContext.current

    val barEntries = barChartData.mapIndexed { index, data ->
        BarEntry(index.toFloat(), data.value.toFloat())
    }

    val barDataSet = BarDataSet(barEntries, "Bar Chart Data")
    barDataSet.colors = COLORFUL_COLORS.toList()

    AndroidView(
        factory = { ctx ->
            BarChart(ctx).apply {
                data = BarData(barDataSet)
                description.isEnabled = false
                setFitBars(true) // makes the x-axis fit exactly all bars
                animateY(1000)
            }
        },
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
    )
}

@Composable
fun PieChartComposable(pieChartData: List<PieChartData>) {
    val context = LocalContext.current

    val pieEntries = pieChartData.map { data ->
        PieEntry(data.value.toFloat(), data.category)
    }

    val pieDataSet = PieDataSet(pieEntries, "Pie Chart Data")
    pieDataSet.colors = COLORFUL_COLORS.toList() // Set colors

    AndroidView(
        factory = { ctx ->
            PieChart(ctx).apply {
                data = PieData(pieDataSet)
                description.isEnabled = false
                isDrawHoleEnabled = false
                setUsePercentValues(true) // Show percentage values
                animateY(1000)
            }
        },
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
    )
}


@Composable
fun LessonList(navController: NavHostController, lessons: List<Lessonforcourse>, courseId: String) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        lessons.forEach { lesson ->
            LessonItem(navController, lesson, courseId) // Pass the navController and courseId
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

@Composable
fun LessonItem(navController: NavHostController, lesson: Lessonforcourse, courseId: String) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .clickable {
                navController.navigate("lessonDetail/${courseId}/${lesson.id}")
            },
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        shape = RoundedCornerShape(8.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = "${lesson.id}", fontFamily = FontFamily(Font(R.font.montserrat_regular)),style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(text = lesson.title, style = MaterialTheme.typography.bodyLarge)
                Text(text = lesson.description, style = MaterialTheme.typography.bodyMedium, color = Color.Gray)
            }
            Spacer(modifier = Modifier.weight(1f))
            Icon(
                imageVector = Icons.Default.ArrowForward,
                contentDescription = "Go to Lesson",
                tint = Color.Gray
            )
        }
    }
}

data class Lesson(val number: Int, val title: String, val description: String)