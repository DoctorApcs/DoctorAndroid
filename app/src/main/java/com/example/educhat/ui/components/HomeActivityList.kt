package com.example.educhat.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.material3.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.educhat.R

data class Activity(val name: String, val courseName: String, val timeAgo: String)

@Composable
fun HomeActivityList(onClickAction: () -> Unit) {
    val activities = listOf(
        Activity("Final Revision", "Calculus 3", "45mins ago"),
        Activity("Final Revision", "CS305", "1hr ago")
    )

    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        activities.forEach { activity ->
            ActivityCard(activity, onClickAction)
        }
    }
}



@Composable
fun ActivityCard(activity: Activity, onClickAction: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(120.dp)
            .shadow(
                elevation = 6.dp,
                shape = RoundedCornerShape(20.dp)
            ).clickable { onClickAction() },
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        )
    ) {
        Row(
            modifier = Modifier.fillMaxHeight(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .width(75.dp)
                    .fillMaxHeight()
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                Color(0xFF360568),
                                Color(0xFF6B0ACE),
                            )
                        )
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.chat_icon),
                    contentDescription = "Chat Icon",
                    tint = Color.White,
                    modifier = Modifier.size(30.dp)
                )
            }
            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .padding(16.dp),
            ) {
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .padding(start = 12.dp)
                ) {
                    Text(
                        text = activity.name,
                        style = TextStyle(
                            fontFamily = FontFamily(Font(R.font.montserrat_semi_bold)),
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 15.sp
                        )
                    )
                    Text(
                        text = activity.courseName,
                        style = TextStyle(
                            fontFamily = FontFamily(Font(R.font.montserrat_semi_bold)),
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 12.sp
                        )
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "me: What is derivatives",
                        style = TextStyle(
                            fontFamily = FontFamily(Font(R.font.montserrat_regular)),
                            fontWeight = FontWeight.Normal,
                            fontSize = 10.sp
                        ),
                        color = Color.Gray
                    )
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(
                        modifier = Modifier.height(30.dp),
                        horizontalAlignment = Alignment.End,
                        verticalArrangement = Arrangement.Top) {
//                        TextButton(
//                            onClick = { /* TODO: Implement back to chat action */ },
//                            colors = ButtonDefaults.textButtonColors(contentColor = Color(0xFF7E57C2))
//                        ) {
//                            Text("Back to chat", style = TextStyle(
//                                fontFamily = FontFamily(Font(R.font.montserrat_semi_bold)),
//                                fontWeight = FontWeight.SemiBold,
//                                fontSize = 15.sp,
//                                color = Color.Black,
//                            ))
//                        }
//                        Text(
//                            text = activity.timeAgo,
//                            style = TextStyle(
//                                fontFamily = FontFamily(Font(R.font.montserrat_regular)),
//                                fontWeight = FontWeight.Normal,
//                                fontSize = 10.sp,
//                                color = Color.Black
//                            ),
//                            color = Color.Gray,
//                        )
                    }
                    Box(
                        modifier = Modifier
                            .size(28.dp)
                            .background(
                                Brush.verticalGradient(
                                    colors = listOf(
                                        Color(0xFF360568),
                                        Color(0xFF6B0ACE),
                                    )
                                ),
                        shape = CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.arrow_forward),
                            contentDescription = "Go back",
                            tint = Color.White,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }
            }
        }
    }
}