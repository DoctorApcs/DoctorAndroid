// WelcomeBackScreen.kt
package com.example.educhat

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.educhat.ui.components.Visibility
import com.example.educhat.ui.components.Visibility_off

@Composable
fun WelcomeBackScreen(navController: NavHostController) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }

    val customTextStyle = TextStyle(
        fontFamily = FontFamily(Font(R.font.montserrat_regular)),
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        color = Color.Black
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 10.dp, vertical = 5.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 137.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Welcome back",
                fontSize = 25.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color.Black,
                modifier = Modifier.padding(bottom = 41.dp)
            )

            Card(
                modifier = Modifier
                    .width(320.dp)
                    .padding(24.dp)
                    .shadow(elevation = 20.dp, shape = RoundedCornerShape(8.dp)),
                shape = RoundedCornerShape(8.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                border = BorderStroke(1.dp, Color(0xFFD9D9D9))
            ) {
                Column(
                    modifier = Modifier
                        .padding(top = 20.dp)
                        .padding(horizontal = 20.dp),
                    verticalArrangement = Arrangement.spacedBy(20.dp)
                ) {
                    OutlinedTextField(
                        value = email,
                        onValueChange = { email = it },
                        label = { Text("Email") },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color.Gray,
                            unfocusedBorderColor = Color.LightGray,
                            cursorColor = Color.DarkGray
                        ),
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(8.dp)
                    )

//                    Spacer(modifier = Modifier.height(2.dp))

                    OutlinedTextField(
                        value = password,
                        onValueChange = { password = it },
                        label = { Text("Password") },
                        visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                        trailingIcon = {
                            val icon = if (passwordVisible) Visibility else Visibility_off
                            IconButton(onClick = { passwordVisible = !passwordVisible }) {
                                Icon(imageVector = icon, contentDescription = null)
                            }
                        },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color.Gray,
                            unfocusedBorderColor = Color.LightGray,
                            cursorColor = Color.DarkGray
                        ),
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(8.dp)
                    )

                    Button(
                        onClick = {},
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(44.dp),
                        shape = RoundedCornerShape(8.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2C2C2C))
                    ) {
                        Text("Sign In", color = Color.White)
                    }

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 15.dp),
                        contentAlignment = Alignment.CenterEnd
                    ) {
                        Text(
                            text = "Forgot password?",
                            textDecoration = TextDecoration.Underline,
                            color = Color.Black.copy(alpha = 0.5f)
                        )
                    }
                }
            }

            Image(
                painter = painterResource(id = R.drawable.google),
                contentDescription = "Logo",
                modifier = Modifier
                    .size(80.dp)
                    .padding(top = 35.dp)
            )
        }
    }
}

@Composable
@Preview(showBackground = true)
fun WelcomeBackScreenPreview() {
    WelcomeBackScreen(rememberNavController())
}