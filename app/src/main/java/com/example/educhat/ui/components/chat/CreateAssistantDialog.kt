package com.example.educhat.ui.components.chat

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
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
        Dialog(onDismissRequest = onClose) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Column(
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxWidth()
                ) {
                    Text("Chat Configuration", style = MaterialTheme.typography.headlineSmall)
                    Spacer(modifier = Modifier.height(16.dp))

                    OutlinedTextField(
                        value = assistantName,
                        onValueChange = { assistantName = it },
                        label = { Text("Assistant name") },
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedTextField(
                        value = description,
                        onValueChange = { description = it },
                        label = { Text("Description") },
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedTextField(
                        value = systemPrompt,
                        onValueChange = { systemPrompt = it },
                        label = { Text("System Prompt") },
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    ExposedDropdownMenuBox(
                        expanded = false,
                        onExpandedChange = { },
                    ) {
                        OutlinedTextField(
                            value = model,
                            onValueChange = { },
                            readOnly = true,
                            label = { Text("Model") },
                            modifier = Modifier.fillMaxWidth()
                        )
                        ExposedDropdownMenu(
                            expanded = false,
                            onDismissRequest = { }
                        ) {
                            DropdownMenuItem(
                                text = { Text("GPT-4o mini") },
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
                                onValueChange = { },
                                readOnly = true,
                                label = { Text("Knowledgebases") },
                                modifier = Modifier.fillMaxWidth()
                            )
                            ExposedDropdownMenu(
                                expanded = false,
                                onDismissRequest = { }
                            ) {
                                knowledgeBases.forEach { kb ->
                                    DropdownMenuItem(
                                        text = { Text(kb.name) },
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
                            Text("Cancel")
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Button(
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
                            Text("Create Assistant")
                        }
                    }
                }
            }
        }
    }
}