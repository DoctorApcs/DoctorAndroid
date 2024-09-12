package com.example.educhat

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
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
import com.aay.compose.donutChart.PieChart
import com.aay.compose.donutChart.model.PieChartData

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
        LazyColumn( // Changed to LazyColumn for scrollable content
            modifier = Modifier
                .fillMaxSize() // Use fillMaxSize to allow the LazyColumn to take full height
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            item {
                BarChartSample()
            }
            item {
                PieChartSample()
            }
        }
    }
}



// Bar Chart docs: https://github.com/TheChance101/AAY-chart?tab=readme-ov-file
@Composable
fun BarChartSample() {
    val testBarParameters: List<BarParameters> = listOf(
        BarParameters(
            dataName = "CS305",
            data = listOf(1.0, 2.0, 2.4, 1.2, 2.3, 3.0, 0.5),
            barColor = Color(0xFF6C3428)
        ),
        BarParameters(
            dataName = "Mobile",
            data = listOf(0.5, 0.1, 0.2, 1.0, 2.0, 1.0, 0.4),
            barColor = Color(0xFFBA704F),
        ),
        BarParameters(
            dataName = "Calculus 3",
            data = listOf(0.1, 2.1, 2.3, 1.0, 3.0, 2.0, 1.4),
            barColor = Color(0xFFDFA878),
        ),
    )

    Box(Modifier.fillMaxSize()) {
        BarChart(
            chartParameters = testBarParameters,
            gridColor = Color.DarkGray,
            xAxisData = listOf("Mon", "Tue", "Wed", "Thu", "Fri", "Sar", "Sun"),
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

@Composable
fun PieChartSample() {

    val testPieChartData: List<PieChartData> = listOf(
        PieChartData(
            partName = "part A",
            data = 500.0,
            color = Color(0xFF22A699),
        ),
        PieChartData(
            partName = "Part B",
            data = 700.0,
            color = Color(0xFFF2BE22),
        ),
        PieChartData(
            partName = "Part C",
            data = 500.0,
            color = Color(0xFFF29727),
        ),
        PieChartData(
            partName = "Part D",
            data = 100.0,
            color = Color(0xFFF24C3D),
        ),
    )

    PieChart(
        modifier = Modifier.fillMaxSize(),
        pieChartData = testPieChartData,
        ratioLineColor = Color.LightGray,
        textRatioStyle = TextStyle(color = Color.Gray),
    )
}