package com.devnunens.hearx.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.devnunens.hearx.ui.navigation.TestNavigation
import com.devnunens.hearx.viewmodel.TestViewModel

@Composable
fun HomeScreen(viewModel: TestViewModel, navController: NavController) {
    val state by viewModel.uiState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("Digit in Noise Test", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(32.dp))
        Button(onClick = { navController.navigate(TestNavigation.Test.route) }) {
            Text("Start Test")
        }
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = { navController.navigate(TestNavigation.Results.route) }) {
            Text("View Results")
        }
    }

    if (state.showResults) {
        AlertDialog(
            onDismissRequest = { viewModel.setShowResults(false) },
            title = { Text("Final Score") },
            text = { Text("Your score: ${state.score}/100") },
            confirmButton = {
                Button(onClick = { viewModel.setShowResults(false) }) {
                    Text("OK")
                }
            }
        )
    }
}