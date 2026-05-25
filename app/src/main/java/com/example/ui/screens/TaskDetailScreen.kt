package com.example.ui.screens

import android.text.format.DateFormat
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.ui.viewmodel.TodoViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskDetailScreen(
    taskId: Long,
    viewModel: TodoViewModel,
    navController: NavController,
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsState()
    val todo = uiState.todos.find { it.id == taskId }

    if (todo == null) {
        Scaffold(
            modifier = modifier.fillMaxSize(),
            topBar = {
                TopAppBar(
                    title = { Text("Task Details") },
                    navigationIcon = {
                        IconButton(onClick = { navController.popBackStack() }) {
                            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFFFEF7FF))
                )
            }
        ) { paddingValues ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                Text("Task not found or has been deleted.", color = Color.Gray)
            }
        }
        return
    }

    val priorityColor = when (todo.priority.uppercase()) {
        "HIGH" -> Color(0xFFE57373)
        "MEDIUM" -> Color(0xFFFFB74D)
        "LOW" -> Color(0xFF81C784)
        else -> Color(0xFF6750A4)
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = { Text("Task Details", fontWeight = FontWeight.Bold, color = Color(0xFF1D1B20)) },
                navigationIcon = {
                    IconButton(
                        onClick = { navController.popBackStack() },
                        modifier = Modifier.testTag("detail_back_button")
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = Color(0xFF1D1B20)
                        )
                    }
                },
                actions = {
                    IconButton(
                        onClick = {
                            viewModel.openEditDialog(todo)
                        }
                    ) {
                        Icon(Icons.Default.Edit, contentDescription = "Edit Task", tint = Color(0xFF6750A4))
                    }
                    IconButton(
                        onClick = {
                            viewModel.deleteTodo(todo)
                            navController.popBackStack()
                        },
                        modifier = Modifier.testTag("detail_delete_button")
                    ) {
                        Icon(Icons.Default.Delete, contentDescription = "Delete Task", tint = Color.Red)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFFFEF7FF))
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(Color(0xFFFEF7FF))
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            // Priority & Category Status Header Card
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Card(
                    modifier = Modifier.weight(1f),
                    colors = CardDefaults.cardColors(containerColor = priorityColor.copy(alpha = 0.15f)),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Text("PRIORITY", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = priorityColor)
                        Text(todo.priority.uppercase(), fontSize = 16.sp, fontWeight = FontWeight.ExtraBold, color = priorityColor)
                    }
                }

                Card(
                    modifier = Modifier.weight(1f),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFE8DEF8)),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Text("CATEGORY", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = Color(0xFF21005D))
                        Text(todo.category.uppercase(), fontSize = 16.sp, fontWeight = FontWeight.ExtraBold, color = Color(0xFF21005D))
                    }
                }
            }

            // Task Body Content Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFF7F2FA)),
                border = BorderStroke(1.dp, Color(0xFFCAC4D0)),
                shape = RoundedCornerShape(24.dp)
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    // Task Title with Checkbox status
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = todo.title,
                            style = MaterialTheme.typography.titleLarge.copy(
                                fontWeight = FontWeight.SemiBold,
                                textDecoration = if (todo.isCompleted) TextDecoration.LineThrough else TextDecoration.None,
                                color = if (todo.isCompleted) Color(0xFF1D1B20).copy(alpha = 0.5f) else Color(0xFF1D1B20)
                            ),
                            modifier = Modifier.weight(1f)
                        )

                        // Big interactive toggler
                        Box(
                            modifier = Modifier
                                .size(28.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .border(2.dp, Color(0xFF6750A4), RoundedCornerShape(8.dp))
                                .background(if (todo.isCompleted) Color(0xFF6750A4) else Color.Transparent)
                                .clickable { viewModel.toggleTodoCompletion(todo) }
                                .testTag("detail_checkbox"),
                            contentAlignment = Alignment.Center
                        ) {
                            if (todo.isCompleted) {
                                Icon(Icons.Default.Check, "Completed", tint = Color.White, modifier = Modifier.size(18.dp))
                            }
                        }
                    }

                    // Task Description
                    if (todo.description.isNotEmpty()) {
                        Column {
                            Text("Description", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color.Gray)
                            Spacer(Modifier.height(4.dp))
                            Text(
                                text = todo.description,
                                style = MaterialTheme.typography.bodyLarge.copy(
                                    color = Color(0xFF49454F),
                                    textDecoration = if (todo.isCompleted) TextDecoration.LineThrough else TextDecoration.None
                                )
                            )
                        }
                    } else {
                        Text("No description provided.", color = Color.Gray, style = MaterialTheme.typography.bodyMedium)
                    }

                    Spacer(modifier = Modifier.height(4.dp))

                    // Dates and Metadata Section
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Schedule, "Created At", modifier = Modifier.size(16.dp), tint = Color.Gray)
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                "Created " + DateFormat.format("MMM dd, yyyy", todo.createdAt).toString(),
                                fontSize = 11.sp,
                                color = Color.Gray
                            )
                        }

                        if (todo.dueDate != null) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.CalendarToday, "Due Date", modifier = Modifier.size(16.dp), tint = Color(0xFF6750A4))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    "Due " + DateFormat.format("MMM dd, yyyy", todo.dueDate).toString(),
                                    fontSize = 11.sp,
                                    color = Color(0xFF6750A4),
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            // Footer Quick Action controls
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OutlinedButton(
                    onClick = { navController.popBackStack() },
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFF6750A4))
                ) {
                    Text("Close")
                }

                Button(
                    onClick = { viewModel.toggleTodoCompletion(todo) },
                    modifier = Modifier.weight(1f).testTag("detail_mark_completed_button"),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (todo.isCompleted) Color(0xFF79747E) else Color(0xFF6750A4)
                    )
                ) {
                    Text(if (todo.isCompleted) "Mark Active" else "Mark Done")
                }
            }
        }

        // Add/Edit Dialog inside detail as well for convenience
        if (uiState.showAddEditDialog) {
            AddEditTodoDialog(
                currentTodo = uiState.currentTodoEdit,
                onDismiss = { viewModel.closeAddEditDialog() },
                onSave = { title, desc, priority, category, dueDate ->
                    viewModel.saveTodo(title, desc, priority, category, dueDate)
                }
            )
        }
    }
}

// BorderStroke replacement helper to prevent potential androidx.compose.foundation.BorderStroke import errors
@Composable
fun BorderStroke(width: androidx.compose.ui.unit.Dp, color: Color) = androidx.compose.foundation.BorderStroke(width, color)
