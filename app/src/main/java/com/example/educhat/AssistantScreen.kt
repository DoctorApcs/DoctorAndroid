package com.example.educhat

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController


data class Assistant(
    val name: String,
    val description: String,
    val model: String
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AssistantCardsScreen(navController: NavController) {
    val assistants = listOf(
        Assistant("Claude", "A versatile AI assistant", "GPT-3.5"),
        Assistant("Ada", "Specialized in code generation", "Codex"),
        Assistant("Sage", "Expert in scientific topics", "GPT-4"),
        // Add more assistants as needed
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("AI Assistants") },
                colors = TopAppBarDefaults.smallTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                )
            )
        }
    ) { innerPadding ->
        LazyColumn(
            contentPadding = innerPadding,
            modifier = Modifier.fillMaxSize()
        ) {
            items(assistants) { assistant ->
                AssistantCard(assistant)
            }
        }
    }
}

@Composable
fun AssistantCard(assistant: Assistant) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
        ) {
            Text(
                text = assistant.name,
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = assistant.description,
                style = MaterialTheme.typography.bodyMedium
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Model: ${assistant.model}",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.secondary
            )
        }
    }
}