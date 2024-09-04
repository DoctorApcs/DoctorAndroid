package com.example.educhat
import android.net.wifi.hotspot2.pps.HomeSp
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.educhat.ui.components.AnalyticsSection
import com.example.educhat.ui.components.HomeActivityList
import com.example.educhat.ui.components.HomeCourseList
import com.example.educhat.ui.components.WelcomeSection

@Composable
fun HomeScreen(navController: NavController) {
    LazyColumn(
        modifier = Modifier.fillMaxSize().padding(16.dp)
    ) {
        item {
            WelcomeSection()
            AnalyticsSection()
        }
        item {
            Text(
                "Your Courses",
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier.padding(vertical = 16.dp)
            )
        }
        item { HomeCourseList() }
        item {
            Text(
                "Activities",
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier.padding(vertical = 16.dp)
            )
        }
        item { HomeActivityList() }
    }
}