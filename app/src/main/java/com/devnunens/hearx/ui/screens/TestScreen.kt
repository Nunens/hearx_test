package com.devnunens.hearx.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.devnunens.hearx.viewmodel.TestViewModel

@Composable
fun TestScreen(viewModel: TestViewModel, navController: NavController) {
    val state by viewModel.uiState.collectAsState()

    LaunchedEffect(key1 = Unit) {
        viewModel.startTest()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            if (viewModel.currentRound < 10) "Round ${viewModel.currentRound + 1}/10" else "Test completed",
            style = MaterialTheme.typography.headlineSmall
        )
        Text("Difficulty: ${viewModel.difficulty}", style = MaterialTheme.typography.bodyLarge)
        Spacer(modifier = Modifier.height(32.dp))
        OutlinedTextField(
            value = state.userInput,
            onValueChange = { if (it.length <= 3) viewModel.onUserInputChanged(it) },
            label = { Text("Enter 3 digits") },
            modifier = Modifier.fillMaxWidth(0.5f)
        )
        Spacer(modifier = Modifier.height(16.dp))
        Row {
            Button(onClick = {
                viewModel.exitTest()
                navController.popBackStack()
            }) { Text("Exit Test") }
            Spacer(modifier = Modifier.width(16.dp))
            Button(onClick = { viewModel.submitAnswer() }, enabled = state.userInput.length == 3) {
                Text("Submit")
            }
        }
        if (viewModel.currentRound == 10 && !state.isTesting) {
            navController.popBackStack()
        }
    }
}