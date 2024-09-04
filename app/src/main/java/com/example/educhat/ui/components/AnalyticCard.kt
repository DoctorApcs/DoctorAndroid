package com.example.educhat.ui.components
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.aay.compose.barChart.BarChart
import com.aay.compose.barChart.model.BarParameters
import com.aay.compose.baseComponents.model.LegendPosition
import com.example.educhat.R

@Composable
fun AnalyticsSection() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 30.dp)
            .shadow(
                elevation = 8.dp,
                shape = RoundedCornerShape(25.dp),
                spotColor = Color.Black.copy(alpha = 0.8f)
            )
            .clip(RoundedCornerShape(25.dp))
            .background(MaterialTheme.colorScheme.surface)
    ) {
        Column(
            modifier = Modifier
                .padding(30.dp)
                .height(200.dp),
            verticalArrangement = Arrangement.spacedBy(30.dp)
        ) {
            Text(
                "Analytics",
                style = TextStyle(
                    fontFamily = FontFamily(Font(R.font.montserrat_semi_bold)),
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 20.sp
                )
            )
            ActivityChart()
        }
        Image(
            painter = painterResource(id = R.drawable.more_button),
            contentDescription = "More options",
            modifier = Modifier
                .size(50.dp)
                .align(Alignment.TopEnd)
                .offset(x = (-8).dp, y = 8.dp)
                .clip(CircleShape)
                .shadow(
                    elevation = 4.dp,
                    shape = RoundedCornerShape(25.dp),
                    spotColor = Color.Black.copy(alpha = 0.8f)
                ),
        )
    }
}

@Composable
fun ActivityChart() {
    val testBarParameters: List<BarParameters> = listOf(
        BarParameters(
            dataName = "Completed",
            data = listOf(3.6, 10.6, 10.0, 9.6, 8.0, 8.6, 7.0),
            barColor = Color(0xFF00A3D4)
        ),
    )

    Box(Modifier.fillMaxSize()) {
        BarChart(
            chartParameters = testBarParameters,
            gridColor = Color.DarkGray,
            xAxisData = listOf("Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun"),
            isShowGrid = true,
            animateChart = true,
            showGridWithSpacer = true,
            yAxisStyle = TextStyle(
                fontFamily = FontFamily(Font(R.font.montserrat_regular)),
                fontWeight = FontWeight.Normal,
                fontSize = 14.sp
            ),
            xAxisStyle = TextStyle(
                fontSize = 14.sp,
                color = Color.DarkGray,
                fontWeight = FontWeight.W400
            ),
            yAxisRange = 4,
            barWidth = 30.dp,
            barCornerRadius=6.dp,
            spaceBetweenGroups=2.dp,
            legendPosition = LegendPosition.DISAPPEAR,
            horizontalArrangement = Arrangement.End
        )
    }
}