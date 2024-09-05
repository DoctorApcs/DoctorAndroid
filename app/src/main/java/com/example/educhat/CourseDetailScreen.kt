package com.example.educhat

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
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(top = 16.dp)
    ) {
        // Recent uploads section
        Text(text = "Recent upload", style = MaterialTheme.typography.bodyLarge, color = Color.Black)
        Spacer(modifier = Modifier.height(8.dp))
        // Upload items
        UploadItem(platform = "YouTube", link = "link", iconId = R.drawable.ic_youtube)
        Spacer(modifier = Modifier.height(8.dp))
        UploadItem(platform = "Google Drive", link = "link", iconId = R.drawable.ic_google_drive)
        Spacer(modifier = Modifier.height(8.dp))
        UploadItem(platform = "File", link = "link", iconId = R.drawable.ic_file)

        Spacer(modifier = Modifier.height(16.dp))

        FloatingActionButton(
            onClick = { /* Handle upload action */ },
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(top = 16.dp),
            containerColor = Color.White
        ) {
            Icon(painter = painterResource(id = R.drawable.ic_upload), contentDescription = "Upload")
        }
    }
}

@Composable
fun UploadItem(platform: String, link: String, iconId: Int) {
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
            Image(
                painter = painterResource(id = iconId), // Use the provided icon resource
                contentDescription = "$platform Icon",
                modifier = Modifier.size(40.dp)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(text = platform, style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.Bold)
                Text(text = link, style = MaterialTheme.typography.bodyMedium, color = Color.Gray)
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
