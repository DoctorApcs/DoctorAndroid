package com.example.educhat

import androidx.annotation.DrawableRes
data class BarChartData(val day: String, val value: Int)
data class PieChartData(val category: String, val value: Int)
data class Course(
    val id: String,
    val title: String,
    val description: String,
    val duration: String,
    val documentsCount: Int,
    @DrawableRes val imageResId: Int, // Resource ID for the course image
    val barChartData: List<BarChartData>,
    val pieChartData: List<PieChartData>,
    val progressPercentage: Int // Progress percentage (0 to 100)
)

data class UploadHistoryItem(
    val type: UploadType,  // Either FILE or LINK
    val uri: String,       // The URI of the file or link
    val timestamp: Long    // Time when the item was added
)

enum class UploadType {
    FILE,
    LINK
}
