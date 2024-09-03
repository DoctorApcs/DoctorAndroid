package com.example.educhat

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.aay.compose.barChart.BarChart
import com.aay.compose.barChart.model.BarParameters

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AnalyticsScreen(navController: NavController) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Analytics",
                        fontSize = 20.sp,
                        color = Color.Black
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            painter = painterResource(id = R.drawable.return_icon), // Replace with your drawable resource ID
                            contentDescription = "Return"
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            BarChartSample()
        }
    }
}


// Bar Chart docs: https://github.com/TheChance101/AAY-chart?tab=readme-ov-file
@Composable
fun BarChartSample() {
    val testBarParameters: List<BarParameters> = listOf(
        BarParameters(
            dataName = "Completed",
            data = listOf(0.6, 10.6, 80.0, 50.6, 44.0, 100.6, 10.0),
            barColor = Color(0xFF6C3428)
        ),
        BarParameters(
            dataName = "Completed",
            data = listOf(50.0, 30.6, 77.0, 69.6, 50.0, 30.6, 80.0),
            barColor = Color(0xFFBA704F),
        ),
        BarParameters(
            dataName = "Completed",
            data = listOf(100.0, 99.6, 60.0, 80.6, 10.0, 100.6, 55.99),
            barColor = Color(0xFFDFA878),
        ),
    )

    Box(Modifier.fillMaxSize()) {
        BarChart(
            chartParameters = testBarParameters,
            gridColor = Color.DarkGray,
            xAxisData = listOf("2016", "2017", "2018", "2019", "2020", "2021", "2022"),
            isShowGrid = true,
            animateChart = true,
            showGridWithSpacer = true,
            yAxisStyle = TextStyle(
                fontSize = 14.sp,
                color = Color.DarkGray,
            ),
            xAxisStyle = TextStyle(
                fontSize = 14.sp,
                color = Color.DarkGray,
                fontWeight = FontWeight.W400
            ),
            yAxisRange = 15,
            barWidth = 20.dp
        )
    }
}
