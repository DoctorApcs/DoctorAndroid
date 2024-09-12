package com.example.educhat.ui.components.home
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.educhat.R
import androidx.compose.ui.graphics.Color
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.Dp

data class Course(val name: String, val imageRes: Int, val docCount: Int)

@Composable
fun HomeCourseList() {
    val courses = listOf(
        Course("Calculus 3", R.drawable.calculus_bg, 2),
        Course("CS305", R.drawable.cs_bg, 10),
        Course("CS302", R.drawable.cs_bg, 22),
    )

    LazyRow(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(start = 16.dp, end = 16.dp)
    ) {
        item{ AddCourseCard() }

        items(courses) { course ->
            CourseCard(course)
        }
    }
}

fun Modifier.dashedBorder(
    color: Color,
    strokeWidth: Dp,
    cornerRadius: Dp,
    dashWidth: Dp,
    gapWidth: Dp
) = this.then(
    Modifier.drawBehind {
        val strokeWidthPx = strokeWidth.toPx()
        val cornerRadiusPx = cornerRadius.toPx()
        val dashWidthPx = dashWidth.toPx()
        val gapWidthPx = gapWidth.toPx()

        val pathEffect = PathEffect.dashPathEffect(floatArrayOf(dashWidthPx, gapWidthPx), 0f)

        drawRoundRect(
            color = color,
            style = Stroke(
                width = strokeWidthPx,
                pathEffect = pathEffect
            ),
            cornerRadius = CornerRadius(cornerRadiusPx)
        )
    }
)
