package com.devnunens.hearx.ui.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.devnunens.hearx.viewmodel.TestViewModel

@SuppressLint("SimpleDateFormat")
@Composable
fun ResultsScreen(viewModel: TestViewModel, navController: NavController) {
    val state by viewModel.uiState.collectAsState()
    val results = state.resultsList

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text("Previous Results", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(16.dp))
        LazyColumn {
            items(results.size) { index ->
                Card(modifier = Modifier.padding(4.dp)) {
                    Row(modifier = Modifier.padding(8.dp)) {
                        Text(
                            "Score: ${results[index].score}",
                            style = MaterialTheme.typography.bodyLarge
                        )
                        Spacer(modifier = Modifier.weight(1f))
                        Text(
                            java.text.SimpleDateFormat("yyyy-MM-dd HH:mm")
                                .format(results[index].timestamp),
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = { navController.popBackStack() }) {
            Text("Back to Home")
        }
    }
}