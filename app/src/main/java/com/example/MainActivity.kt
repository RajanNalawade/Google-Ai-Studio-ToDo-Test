package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.ui.screens.CategoryBreakdownScreen
import com.example.ui.screens.MainPagerScreen
import com.example.ui.screens.TaskDetailScreen
import com.example.ui.theme.MyApplicationTheme
import com.example.ui.viewmodel.TodoViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : ComponentActivity() {

    // Inject our shared ViewModel via Koin
    private val todoViewModel: TodoViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            MyApplicationTheme {
                val navController = rememberNavController()

                // Nested Navigation Graphs (MAD multi-graph system)
                // Root Graph starts at "tasks_graph" flow
                NavHost(
                    navController = navController,
                    startDestination = "tasks_graph",
                    modifier = Modifier.fillMaxSize()
                ) {
                    // 1. Tasks Flow Navigation Graph
                    navigation(
                        startDestination = "main_pager",
                        route = "tasks_graph"
                    ) {
                        // The primary ViewPager screen swiping between Todo list and Stats
                        composable("main_pager") {
                            MainPagerScreen(
                                viewModel = todoViewModel,
                                onNavigateToDetail = { taskId ->
                                    navController.navigate("task_detail/$taskId")
                                },
                                onNavigateToCategory = { categoryName ->
                                    navController.navigate("category_detail/$categoryName")
                                }
                            )
                        }

                        // Task Details Screen
                        composable(
                            route = "task_detail/{taskId}",
                            arguments = listOf(navArgument("taskId") { type = NavType.LongType })
                        ) { backStackEntry ->
                            val taskId = backStackEntry.arguments?.getLong("taskId") ?: -1L
                            TaskDetailScreen(
                                taskId = taskId,
                                viewModel = todoViewModel,
                                navController = navController
                            )
                        }
                    }

                    // 2. Metrics & Analytics Flow Navigation Graph
                    navigation(
                        startDestination = "category_detail/{categoryName}",
                        route = "analytics_graph"
                    ) {
                        // Specific category breakdown analysis screen
                        composable(
                            route = "category_detail/{categoryName}",
                            arguments = listOf(navArgument("categoryName") { type = NavType.StringType })
                        ) { backStackEntry ->
                            val categoryName = backStackEntry.arguments?.getString("categoryName") ?: ""
                            CategoryBreakdownScreen(
                                categoryName = categoryName,
                                viewModel = todoViewModel,
                                navController = navController
                            )
                        }
                    }
                }
            }
        }
    }
}
