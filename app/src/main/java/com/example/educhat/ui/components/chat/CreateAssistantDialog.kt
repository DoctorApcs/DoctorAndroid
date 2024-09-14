package com.example.educhat.ui.components.chat

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.educhat.ui.components.home.Montserrat
import com.example.educhat.ui.theme.CustomPrimaryStart
import kotlinx.coroutines.launch

data class KnowledgeBase(
    val id: String,
    val name: String
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateAssistantDialog(
    isOpen: Boolean,
    onClose: () -> Unit,
    onCreateSuccess: () -> Unit,
    fetchKnowledgeBases: suspend () -> List<KnowledgeBase>,
    createAssistant: suspend (String, String, String, String, String, () -> Unit, (String) -> Unit) -> Unit
) {
    var assistantName by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var systemPrompt by remember { mutableStateOf("") }
    var selectedKnowledgeBase by remember { mutableStateOf("") }
    var model by remember { mutableStateOf("gpt-4o-mini") }
    var knowledgeBases by remember { mutableStateOf<List<KnowledgeBase>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }

    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(isOpen) {
        if (isOpen) {
            isLoading = true
            knowledgeBases = fetchKnowledgeBases()
            isLoading = false
        }
    }

    if (isOpen) {
        Dialog(
            onDismissRequest = onClose,
            properties = DialogProperties(usePlatformDefaultWidth = false)
        ) {
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .background(Color.White, shape = RoundedCornerShape(16.dp)),
                shape = RoundedCornerShape(16.dp),
                color = Color.White
            ) {
                Column(
                    modifier = Modifier
                        .padding(24.dp)
                        .fillMaxWidth()
                ) {
                    Text(
                        "Chat Configuration",
                        style = MaterialTheme.typography.headlineSmall.copy(fontFamily = Montserrat))
                    Spacer(modifier = Modifier.height(16.dp))

                    OutlinedTextField(
                        value = assistantName,
                        textStyle = TextStyle(fontFamily = Montserrat),
                        onValueChange = { assistantName = it },
                        label = { Text("Assistant name", fontFamily = Montserrat) },
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedTextField(
                        value = description,
                        textStyle = TextStyle(fontFamily = Montserrat),
                        onValueChange = { description = it },
                        label = { Text("Description", fontFamily = Montserrat) },
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedTextField(
                        value = systemPrompt,
                        textStyle = TextStyle(fontFamily = Montserrat),
                        onValueChange = { systemPrompt = it },
                        label = { Text("System Prompt", fontFamily = Montserrat) },
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    ExposedDropdownMenuBox(
                        expanded = false,
                        onExpandedChange = { },
                    ) {
                        OutlinedTextField(
                            value = model,
                            textStyle = TextStyle(fontFamily = Montserrat),
                            onValueChange = { },
                            readOnly = true,
                            label = { Text("Model", fontFamily = Montserrat) },
                            modifier = Modifier.fillMaxWidth()
                        )
                        ExposedDropdownMenu(
                            expanded = false,
                            onDismissRequest = { }
                        ) {
                            DropdownMenuItem(
                                text = { Text("GPT-4o mini", fontFamily = Montserrat) },
                                onClick = { model = "gpt-4o-mini" }
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    if (isLoading) {
                        CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
                    } else {
                        ExposedDropdownMenuBox(
                            expanded = false,
                            onExpandedChange = { },
                        ) {
                            OutlinedTextField(
                                value = selectedKnowledgeBase,
                                textStyle = TextStyle(fontFamily = Montserrat),
                                onValueChange = { },
                                readOnly = true,
                                label = { Text("Course", fontFamily = Montserrat) },
                                modifier = Modifier.fillMaxWidth()
                            )
                            ExposedDropdownMenu(
                                expanded = false,
                                onDismissRequest = { }
                            ) {
                                knowledgeBases.forEach { kb ->
                                    DropdownMenuItem(
                                        text = { Text(kb.name, fontFamily = Montserrat) },
                                        onClick = { selectedKnowledgeBase = kb.id }
                                    )
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End
                    ) {
                        TextButton(onClick = onClose) {
                            Text("Cancel", fontFamily = Montserrat)
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Button(
                            colors = ButtonDefaults.buttonColors(
                                containerColor = CustomPrimaryStart,
                                contentColor = Color.White
                            ),
                            onClick = {
                                coroutineScope.launch {
                                    createAssistant(
                                        assistantName,
                                        description,
                                        systemPrompt,
                                        selectedKnowledgeBase,
                                        model,
                                        {
                                            onCreateSuccess()
                                            onClose()
                                        },
                                        {
                                            // Handle error
                                        }
                                    )
                                }
                            }
                        ) {
                            Text("Create Assistant", fontFamily = Montserrat)
                        }
                    }
                }
            }
        }
    }
}