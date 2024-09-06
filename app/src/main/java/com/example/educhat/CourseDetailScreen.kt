package com.example.educhat

import android.content.Intent
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.compose.ui.viewinterop.AndroidView
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.utils.ColorTemplate
import androidx.compose.ui.platform.LocalContext
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.clickable
import androidx.compose.ui.window.Dialog
@Composable
fun CourseDetailsScreen(navController: NavHostController, courseId: String, courses: List<Course>) {
    var selectedTab by remember { mutableIntStateOf(0) }
    val tabs = listOf("Dashboard", "Uploads", "Conversations")

    val course = courses.find { it.id == courseId }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Background with Box layout
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp) // Adjust the height as per your design
        ) {
            Image(
                painter = painterResource(id = R.drawable.default_background), // Replace with your actual image resource
                contentDescription = "Course Background",
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )

            // Add the navigation button and course title on top of the background image

        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        ) {
            // Back button aligned to the left
            IconButton(
                onClick = { navController.popBackStack() },
                modifier = Modifier.align(Alignment.CenterStart) // Aligns to the start (left)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_back), // Replace with your back icon
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
                modifier = Modifier.align(Alignment.Center) // Center the title
            )
        }
        //Spacer(modifier = Modifier.height(4.dp))

        // Tabs and content section below the background
        TabRow(selectedTabIndex = selectedTab) {
            tabs.forEachIndexed { index, title ->
                Tab(
                    text = { Text(title) },
                    selected = selectedTab == index,
                    onClick = { selectedTab = index }
                )
            }
        }

        when (selectedTab) {
            0 -> course?.let { DashboardContent(course = it) } // Pass the course object to DashboardContent
            1 -> course?.let { UploadsContent() }   // Assuming you'll pass course data to UploadsContent
            2 -> course?.let { ConversationsContent(navController) } // Assuming the same for ConversationsContent
        }
    }
}

@Composable
fun DashboardContent(course: Course) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(top = 16.dp)
    ) {
        // Dashboard for the course
        Text("Bar Chart:", style = MaterialTheme.typography.titleMedium)
        Spacer(modifier = Modifier.height(16.dp))
        BarChartComposable(barChartData = course.barChartData)

        Spacer(modifier = Modifier.height(32.dp))

        Text("Pie Chart:", style = MaterialTheme.typography.titleMedium)
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
        Text(text = "Recent Uploads", style = MaterialTheme.typography.bodyLarge, color = Color.Black)
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
fun ActionDialog(onFileUpload: () -> Unit, onEmbedLink: () -> Unit) {
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
                Text(text = "Choose Action", style = MaterialTheme.typography.bodyLarge, color = Color.Black)

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
                    Text("Embed Link")
                }
            }
        }
    }
}
@Composable
fun ConversationsContent(navController: NavHostController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()) // Enable scrolling
            .padding(top = 16.dp)
    ) {
        // Conversations list
        ConversationItemCard(navController,title = "Final Revision", description = "What is derivatives", timeAgo = "45mins ago")
        Spacer(modifier = Modifier.height(8.dp))
        ConversationItemCard(navController,title = "Final Revision", description = "What is derivatives", timeAgo = "45mins ago")
        Spacer(modifier = Modifier.height(8.dp))
        ConversationItemCard(navController,title = "Final Revision", description = "What is derivatives", timeAgo = "45mins ago")

        Spacer(modifier = Modifier.height(16.dp))

        // Centered floating action button at the bottom
        FloatingActionButton(
            onClick = { /* Handle chat action */ },
            modifier = Modifier
                .align(Alignment.CenterHorizontally),
            containerColor = Color.White
        ) {
            Icon(painter = painterResource(id = R.drawable.ic_chat), contentDescription = "Chat")
        }
    }
}

@Composable
fun ConversationItemCard(navController: NavHostController,title: String, description: String, timeAgo: String) {
    Card(
        modifier = Modifier
            .fillMaxWidth()

            .padding(vertical = 8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        shape = RoundedCornerShape(16.dp) // Rounded corners for the card
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Left icon section with a gradient background
            Box(
                modifier = Modifier
                    .size(70.dp)
                    .clip(RoundedCornerShape(topStart = 16.dp, bottomStart = 16.dp)) // Rounded left side
                    .background(
                        brush = Brush.horizontalGradient(
                            colors = listOf(Color(0xFF6200EE), Color(0xFF9C27B0)) // Gradient colors (purple)
                        )
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_conversation), // Replace with your conversation icon
                    contentDescription = "Conversation Icon",
                    tint = Color.White,
                    modifier = Modifier.size(32.dp) // Icon size
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            // Middle text section
            Column(modifier = Modifier.weight(1f)) {
                Text(text = title, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                Text(text = "Calculus 3", fontSize = 14.sp, color = Color.Gray)
                Text(text = "me: $description", fontSize = 14.sp, color = Color.Gray)
            }

            Spacer(modifier = Modifier.width(16.dp))

            // Right side "Back to chat" section
            Column(
                horizontalAlignment = Alignment.End,
                verticalArrangement = Arrangement.Center
            ) {
                // "Back to chat" text and time
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Column(
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.Start
                    ) {
                        Text(text = "Back to chat", fontSize = 14.sp, fontWeight = FontWeight.SemiBold, color = Color.Black)
                        Text(text = timeAgo, fontSize = 12.sp, color = Color.Gray)
                    }
                    Spacer(modifier = Modifier.width(8.dp))

                    // Circular arrow button
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .clip(CircleShape)
                            .background(Color(0xFF6200EE)) // Purple background
                            .clickable { navController.navigate("chat_screen") }, // add later
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_right_button),
                            contentDescription = "Arrow Icon",
                            tint = Color.White,
                            modifier = Modifier.size(24.dp) // Icon size
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
    barDataSet.colors = ColorTemplate.COLORFUL_COLORS.toList() // Use pre-defined colors

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
    pieDataSet.colors = ColorTemplate.COLORFUL_COLORS.toList() // Set colors

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
