package com.example.educhat
import android.net.wifi.hotspot2.pps.HomeSp
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
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
            AnalyticsSection(navController)
        }
        item {
            Text(
                "Your Courses",
                style = TextStyle(
                    fontFamily = FontFamily(Font(R.font.montserrat_semi_bold)),
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 25.sp
                ),
                modifier = Modifier.padding(vertical = 16.dp)
            )
        }
        item { HomeCourseList() }
        item {
            Text(
                "Activities",
                style = TextStyle(
                    fontFamily = FontFamily(Font(R.font.montserrat_semi_bold)),
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 25.sp
                ),
                modifier = Modifier.padding(vertical = 16.dp)
            )
        }
        item { HomeActivityList() }
    }
}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    HomeScreen(rememberNavController())
}