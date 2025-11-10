package com.devnunens.hearx

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.devnunens.hearx.ui.navigation.TestNavigation
import com.devnunens.hearx.ui.screens.HomeScreen
import com.devnunens.hearx.ui.screens.ResultsScreen
import com.devnunens.hearx.ui.screens.TestScreen
import com.devnunens.hearx.ui.theme.HearXTheme
import com.devnunens.hearx.viewmodel.TestViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            HearXTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()

                    val testViewModel: TestViewModel = hiltViewModel()

                    NavHost(
                        navController = navController,
                        startDestination = TestNavigation.Home.route
                    ) {
                        composable(route = TestNavigation.Home.route) {
                            HomeScreen(
                                viewModel = testViewModel,
                                navController = navController
                            )
                        }

                        composable(route = TestNavigation.Test.route) {
                            TestScreen(
                                navController = navController,
                                viewModel = testViewModel
                            )
                        }

                        composable(route = TestNavigation.Results.route) {
                            ResultsScreen(
                                viewModel = testViewModel,
                                navController = navController
                            )
                        }
                    }
                }
            }
        }
    }
}