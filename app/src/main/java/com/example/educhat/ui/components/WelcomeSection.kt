package com.example.educhat.ui.components
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.educhat.R

@Composable
fun WelcomeSection() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            "Welcome back, Admin!",
            style = TextStyle(
                fontFamily = FontFamily(Font(R.font.montserrat_semi_bold)),
                fontWeight = FontWeight.SemiBold,
                fontSize = 25.sp
            ),
            modifier = Modifier.weight(1f)
        )
        Image(
            painter = painterResource(id = R.drawable.admin_avatar),
            contentDescription = "Admin Avatar",
            modifier = Modifier.size(48.dp)
        )
    }
}
