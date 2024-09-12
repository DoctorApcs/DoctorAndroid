package com.example.educhat

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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

class LearningData(
    val courseName: String,
    val data: List<Double>,
    val color: Color
)

val learningData: List<LearningData> = listOf(
    LearningData("CS305", listOf(1.0, 2.0, 2.4, 1.2, 2.3, 3.0, 0.5), Color(0xFF6C3428)),
    LearningData("Mobile", listOf(0.5, 0.1, 0.2, 1.0, 2.0, 1.0, 0.4), Color(0xFFBA704F)),
    LearningData("Calculus 3", listOf(0.1, 2.1, 2.3, 1.0, 3.0, 2.0, 1.4), Color(0xFFDFA878)),
)


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AnalyticsScreen(navController: NavController) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Analytics",
                        fontSize = 30.sp,
                        color = Color.Black
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            painter = painterResource(id = R.drawable.return_icon), // Replace with your drawable resource ID
                            contentDescription = "Return",
                            modifier = Modifier.size(30.dp)
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            item {
                BarChartSample(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(400.dp) // Set a specific height
                )
            }
            item {
                PieChartSample(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(400.dp) // Set a specific height
                )
            }
        }
    }
}

@Composable
fun BarChartSample(modifier: Modifier = Modifier) {
    val testBarParameters = learningData.map { learningData ->
        BarParameters(
            dataName = learningData.courseName,
            data = learningData.data,
            barColor = learningData.color
        )
    }

    Box(modifier = modifier) {
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
fun PieChartSample(modifier: Modifier = Modifier) {
    val testPieChartData = learningData.map { learningData ->
        PieChartData(
            partName = learningData.courseName,
            data = learningData.data.sum(),
            color = learningData.color
        )
    }

    PieChart(
        modifier = modifier,
        pieChartData = testPieChartData,
        ratioLineColor = Color.LightGray,
        textRatioStyle = TextStyle(color = Color.Gray),
    )
}
