package com.example.educhat
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.educhat.ui.components.AnalyticsSection
import com.example.educhat.ui.components.home.HomeActivityList
import com.example.educhat.ui.components.home.HomeCourseList
import com.example.educhat.ui.components.WelcomeSection
import com.example.educhat.ui.components.home.KnowledgeBaseScreen
import com.example.educhat.ui.components.home.KnowledgeBaseViewModel
import com.example.educhat.ui.theme.CustomPrimaryStart

@Composable
fun HomeScreen(navController: NavController) {
    var showKnowledgeBaseModal by remember { mutableStateOf(false) }

    // Knowledge Base Modal
    if (showKnowledgeBaseModal) {
        KnowledgeBaseModal(
            onDismiss = { showKnowledgeBaseModal = false },
        )
    } else {

        Box(modifier = Modifier.fillMaxSize()) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                item {
                    WelcomeSection()
                    AnalyticsSection(navController)
                }
                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            "Your Courses",
                            style = TextStyle(
                                fontFamily = FontFamily(Font(R.font.montserrat_semi_bold)),
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 25.sp
                            ),
                            modifier = Modifier.padding(vertical = 16.dp)
                        )
                        Text(
                            "See all",
                            style = TextStyle(
                                fontFamily = FontFamily(Font(R.font.montserrat_regular)),
                                color = CustomPrimaryStart,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Medium
                            ),
                            modifier = Modifier
                                .padding(vertical = 16.dp)
                                .clickable {
                                    navController.navigate("chat")
                                }
                        )
                    }
                }
                item { HomeCourseList(onAddNewCourse = {showKnowledgeBaseModal = true}) }
                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            "Your Courses",
                            style = TextStyle(
                                fontFamily = FontFamily(Font(R.font.montserrat_semi_bold)),
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 25.sp
                            ),
                            modifier = Modifier.padding(vertical = 16.dp)
                        )
                        Text(
                            "See all",
                            style = TextStyle(
                                fontFamily = FontFamily(Font(R.font.montserrat_regular)),
                                color = CustomPrimaryStart,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Medium
                            ),
                            modifier = Modifier
                                .padding(vertical = 16.dp)
                                .clickable {
                                    navController.navigate("chat")
                                }
                        )
                    }
                }
                item { HomeActivityList { navController.navigate("chat") } }
            }


        }
    }
}

@Composable
fun KnowledgeBaseModal(onDismiss: () -> Unit) {
    val viewModel = KnowledgeBaseViewModel()
    KnowledgeBaseScreen(viewModel, { onDismiss() })
}