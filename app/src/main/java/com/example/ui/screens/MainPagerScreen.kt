package com.example.ui.screens

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Assignment
import androidx.compose.material.icons.filled.Analytics
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.viewmodel.TodoViewModel
import kotlinx.coroutines.launch

@Composable
fun MainPagerScreen(
    viewModel: TodoViewModel,
    onNavigateToDetail: (Long) -> Unit,
    onNavigateToCategory: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    // We have 2 pages: 0 = Tasks/Todo list, 1 = Analytics dashboard
    val pagerState = rememberPagerState(pageCount = { 2 })

    Scaffold(
        modifier = modifier.fillMaxSize(),
        bottomBar = {
            // Synchronized bottom navigation representing the tabs for HorizontalPager (ViewPager)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(80.dp)
                    .background(Color(0xFFF3EDF7))
                    .border(width = 1.dp, color = Color(0xFFCAC4D0).copy(alpha = 0.5f)),
                horizontalArrangement = Arrangement.SpaceAround,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Page 0: Tasks Tab
                val isTasks = pagerState.currentPage == 0
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                    modifier = Modifier
                        .clip(RoundedCornerShape(16.dp))
                        .background(if (isTasks) Color(0xFFE8DEF8) else Color.Transparent)
                        .clickable {
                            scope.launch {
                                pagerState.animateScrollToPage(0)
                            }
                        }
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.Assignment,
                        contentDescription = "Tasks",
                        tint = if (isTasks) Color(0xFF1D192B) else Color(0xFF49454F),
                        modifier = Modifier.size(24.dp)
                    )
                    Text(
                        text = "Tasks",
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontWeight = if (isTasks) FontWeight.Bold else FontWeight.Medium,
                            color = if (isTasks) Color(0xFF1D192B) else Color(0xFF49454F),
                            fontSize = 11.sp
                        )
                    )
                }

                // Decorative calendar button
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                    modifier = Modifier
                        .alpha(0.6f)
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                        .clickable {
                            Toast.makeText(
                                context,
                                "Calendar overview is coming in a future update!",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                ) {
                    Icon(
                        imageVector = Icons.Default.CalendarToday,
                        contentDescription = "Calendar",
                        tint = Color(0xFF49454F),
                        modifier = Modifier.size(24.dp)
                    )
                    Text(
                        text = "Calendar",
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontWeight = FontWeight.Medium,
                            color = Color(0xFF49454F),
                            fontSize = 11.sp
                        )
                    )
                }

                // Page 1: Analytics/Stats Tab
                val isStats = pagerState.currentPage == 1
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                    modifier = Modifier
                        .clip(RoundedCornerShape(16.dp))
                        .background(if (isStats) Color(0xFFE8DEF8) else Color.Transparent)
                        .clickable {
                            scope.launch {
                                pagerState.animateScrollToPage(1)
                            }
                        }
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Analytics,
                        contentDescription = "Stats",
                        tint = if (isStats) Color(0xFF1D192B) else Color(0xFF49454F),
                        modifier = Modifier.size(24.dp)
                    )
                    Text(
                        text = "Stats",
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontWeight = if (isStats) FontWeight.Bold else FontWeight.Medium,
                            color = if (isStats) Color(0xFF1D192B) else Color(0xFF49454F),
                            fontSize = 11.sp
                        )
                    )
                }

                // Decorative settings button
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                    modifier = Modifier
                        .alpha(0.6f)
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                        .clickable {
                            Toast.makeText(
                                context,
                                "Settings overview is coming in a future update!",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                ) {
                    Icon(
                        imageVector = Icons.Default.Settings,
                        contentDescription = "Settings",
                        tint = Color(0xFF49454F),
                        modifier = Modifier.size(24.dp)
                    )
                    Text(
                        text = "Settings",
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontWeight = FontWeight.Medium,
                            color = Color(0xFF49454F),
                            fontSize = 11.sp
                        )
                    )
                }
            }
        }
    ) { paddingValues ->
        HorizontalPager(
            state = pagerState,
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) { page ->
            when (page) {
                0 -> TodoScreen(
                    viewModel = viewModel,
                    onNavigateToDetail = onNavigateToDetail,
                    modifier = Modifier.fillMaxSize()
                )
                1 -> StatsScreen(
                    viewModel = viewModel,
                    onNavigateToCategory = onNavigateToCategory,
                    modifier = Modifier.fillMaxSize()
                )
            }
        }
    }
}
