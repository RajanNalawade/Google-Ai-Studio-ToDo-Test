package com.example.ui.screens

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ListAlt
import androidx.compose.material.icons.filled.Analytics
import androidx.compose.material.icons.filled.DoneAll
import androidx.compose.material.icons.filled.PendingActions
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.TodoEntity
import com.example.ui.viewmodel.TodoViewModel

@Composable
fun StatsScreen(
    viewModel: TodoViewModel,
    onNavigateToCategory: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsState()
    val todos = uiState.todos

    val totalCount = todos.size
    val completedCount = todos.count { it.isCompleted }
    val remainingCount = totalCount - completedCount
    val completionPercent = if (totalCount > 0) (completedCount * 100f) / totalCount else 0f

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFFFEF7FF))
            .statusBarsPadding()
    ) {
        // Geometric Balanced Header
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, end = 16.dp, top = 24.dp, bottom = 16.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .background(Color(0xFFEADDFF), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Analytics,
                        contentDescription = "Stats",
                        tint = Color(0xFF21005D),
                        modifier = Modifier.size(22.dp)
                    )
                }
                Text(
                    text = "Analytics Dashboard",
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF1D1B20),
                        fontSize = 20.sp
                    )
                )
            }
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Track your productivity, categories & priorities",
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = Color(0xFF49454F),
                    fontSize = 14.sp
                )
            )
        }

        LazyColumn(
            modifier = Modifier.fillMaxWidth(),
            contentPadding = PaddingValues(start = 16.dp, end = 16.dp, bottom = 24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // 1. Completion Radial Chart Arc card
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("stats_completed_card"),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFEADDFF)),
                    shape = RoundedCornerShape(28.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(24.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column(
                            verticalArrangement = Arrangement.spacedBy(4.dp),
                            modifier = Modifier.weight(1f)
                        ) {
                            Text(
                                text = "OVERALL PROGRESS",
                                color = Color(0xFF21005D),
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontWeight = FontWeight.Bold,
                                    letterSpacing = 1.5.sp
                                )
                            )
                            Text(
                                text = "%.0f%%".format(completionPercent),
                                color = Color(0xFF21005D),
                                fontSize = 42.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "$completedCount of $totalCount tasks completed",
                                color = Color(0xFF49454F),
                                style = MaterialTheme.typography.bodySmall
                            )
                        }

                        // Circular Progress Indicator inside custom Canvas
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier.size(90.dp)
                        ) {
                            Canvas(modifier = Modifier.size(80.dp)) {
                                drawCircle(
                                    color = Color(0xFFFEF7FF).copy(alpha = 0.6f),
                                    style = Stroke(width = 8.dp.toPx())
                                )
                                drawArc(
                                    color = Color(0xFF21005D),
                                    startAngle = -90f,
                                    sweepAngle = (completionPercent / 100f) * 360f,
                                    useCenter = false,
                                    style = Stroke(
                                        width = 8.dp.toPx(),
                                        cap = StrokeCap.Round
                                    )
                                )
                            }
                            Text(
                                text = "$completedCount/$totalCount",
                                color = Color(0xFF21005D),
                                fontWeight = FontWeight.ExtraBold,
                                fontSize = 14.sp
                            )
                        }
                    }
                }
            }

            // 2. Metrics grid columns
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    // Total Tasks
                    Card(
                        modifier = Modifier
                            .weight(1f)
                            .border(width = 1.dp, color = Color(0xFFCAC4D0), shape = RoundedCornerShape(16.dp)),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFFF7F2FA)),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.ListAlt,
                                    contentDescription = null,
                                    tint = Color(0xFF1D1B20),
                                    modifier = Modifier.size(16.dp)
                                )
                                Text(
                                    text = "Total",
                                    color = Color(0xFF49454F),
                                    style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold)
                                )
                            }
                            Text(
                                text = "$totalCount",
                                color = Color(0xFF1D1B20),
                                fontSize = 32.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    // Remaining
                    Card(
                        modifier = Modifier
                            .weight(1f)
                            .border(width = 1.dp, color = Color(0xFFCAC4D0), shape = RoundedCornerShape(16.dp)),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFFF7F2FA)),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.PendingActions,
                                    contentDescription = null,
                                    tint = Color(0xFF1D1B20),
                                    modifier = Modifier.size(16.dp)
                                )
                                Text(
                                    text = "Pending",
                                    color = Color(0xFF49454F),
                                    style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold)
                                )
                            }
                            Text(
                                text = "$remainingCount",
                                color = Color(0xFF1D1B20),
                                fontSize = 32.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }

            // 3. Category Breakdown Section
            item {
                Text(
                    text = "Category Performance",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF1D1B20)
                    ),
                    modifier = Modifier.padding(top = 8.dp, bottom = 4.dp)
                )
            }

            val categories = listOf("Personal", "Work", "Shopping", "Fitness", "Other")
            items(categories) { cat ->
                val catTasks = todos.filter { it.category.equals(cat, ignoreCase = true) }
                val catTotal = catTasks.size
                val catCompleted = catTasks.count { it.isCompleted }
                val catProgress = if (catTotal > 0) (catCompleted.toFloat() / catTotal) else 0f

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp))
                        .clickable { onNavigateToCategory(cat) }
                        .border(width = 1.dp, color = Color(0xFFCAC4D0), shape = RoundedCornerShape(16.dp)),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFF7F2FA)),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = cat,
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF1D1B20)
                                )
                            )
                            Text(
                                text = "$catCompleted of $catTotal completed",
                                style = MaterialTheme.typography.bodyMedium.copy(
                                    color = Color(0xFF49454F),
                                    fontWeight = FontWeight.Medium
                                )
                            )
                        }

                        // Linear Progress indicator
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(8.dp)
                                .clip(RoundedCornerShape(4.dp))
                                .background(Color(0xFFE8DEF8))
                        ) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth(if (catProgress > 0f) catProgress else 0.001f)
                                    .height(8.dp)
                                    .clip(RoundedCornerShape(4.dp))
                                    .background(Color(0xFF6750A4))
                            )
                        }
                    }
                }
            }

            // 4. Priority Distributions Section
            item {
                Text(
                    text = "Priority Breakdown",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF1D1B20)
                    ),
                    modifier = Modifier.padding(top = 8.dp, bottom = 4.dp)
                )
            }

            item {
                val highPriorityCount = todos.count { it.priority.equals("HIGH", ignoreCase = true) }
                val mediumPriorityCount = todos.count { it.priority.equals("MEDIUM", ignoreCase = true) }
                val lowPriorityCount = todos.count { it.priority.equals("LOW", ignoreCase = true) }

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(width = 1.dp, color = Color(0xFFCAC4D0).copy(alpha = 0.5f), shape = RoundedCornerShape(16.dp)),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFF7F2FA)),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceAround
                    ) {
                        // High
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(12.dp)
                                    .clip(CircleShape)
                                    .background(Color(0xFFE57373))
                            )
                            Text(
                                text = "HIGH",
                                style = MaterialTheme.typography.labelMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF49454F)
                                )
                            )
                            Text(
                                text = "$highPriorityCount",
                                style = MaterialTheme.typography.titleLarge.copy(
                                    fontWeight = FontWeight.ExtraBold,
                                    color = Color(0xFF1D1B20)
                                )
                            )
                        }

                        // Medium
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(12.dp)
                                    .clip(CircleShape)
                                    .background(Color(0xFFFFB74D))
                            )
                            Text(
                                text = "MEDIUM",
                                style = MaterialTheme.typography.labelMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF49454F)
                                )
                            )
                            Text(
                                text = "$mediumPriorityCount",
                                style = MaterialTheme.typography.titleLarge.copy(
                                    fontWeight = FontWeight.ExtraBold,
                                    color = Color(0xFF1D1B20)
                                )
                            )
                        }

                        // Low
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(12.dp)
                                    .clip(CircleShape)
                                    .background(Color(0xFF81C784))
                            )
                            Text(
                                text = "LOW",
                                style = MaterialTheme.typography.labelMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF49454F)
                                )
                            )
                            Text(
                                text = "$lowPriorityCount",
                                style = MaterialTheme.typography.titleLarge.copy(
                                    fontWeight = FontWeight.ExtraBold,
                                    color = Color(0xFF1D1B20)
                                )
                            )
                        }
                    }
                }
            }
        }
    }
}
