package com.example.ui.screens

import android.text.format.DateFormat
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ChevronLeft
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.TodoEntity
import com.example.ui.viewmodel.TodoViewModel
import java.util.Calendar
import java.util.Locale

@Composable
fun CalendarScreen(
    viewModel: TodoViewModel,
    onNavigateToDetail: (Long) -> Unit,
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsState()

    // Interactive Month State
    var currentMonthCalendar by remember { mutableStateOf(Calendar.getInstance()) }
    var selectedDate by remember { mutableStateOf(Calendar.getInstance()) }

    // Re-trigger calculation if month shifts
    val year = currentMonthCalendar.get(Calendar.YEAR)
    val month = currentMonthCalendar.get(Calendar.MONTH) // 0-indexed

    val monthName = currentMonthCalendar.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.getDefault()) ?: ""

    // Calculate grid items: support month start offsets and pad up to full rows (7 cols * weeks)
    val calendarDays = remember(year, month) {
        val days = mutableListOf<CalendarDate>()
        val cal = currentMonthCalendar.clone() as Calendar
        cal.set(Calendar.DAY_OF_MONTH, 1)

        // Starting column (Sunday is 1, Monday is 2, etc.)
        val startDayOfWeek = cal.get(Calendar.DAY_OF_WEEK)
        // We want Monday as index 0, so convert accordingly
        // USA: Sun (1), Mon (2) -> Mon starts week
        val offset = if (startDayOfWeek == Calendar.SUNDAY) 6 else startDayOfWeek - 2

        // Backfill previous month days
        cal.add(Calendar.DAY_OF_MONTH, -offset)
        val totalCells = 42 // 6 weeks * 7 days
        for (i in 0 until totalCells) {
            days.add(
                CalendarDate(
                    day = cal.get(Calendar.DAY_OF_MONTH),
                    month = cal.get(Calendar.MONTH),
                    year = cal.get(Calendar.YEAR),
                    timestamp = cal.timeInMillis,
                    isCurrentMonth = cal.get(Calendar.MONTH) == month
                )
            )
            cal.add(Calendar.DAY_OF_MONTH, 1)
        }
        days
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFFFEF7FF))
                    .padding(horizontal = 16.dp, vertical = 14.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Calendar Schedules",
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.ExtraBold,
                        color = Color(0xFF1D1B20)
                    )
                )

                Icon(
                    imageVector = Icons.Default.CalendarToday,
                    contentDescription = null,
                    tint = Color(0xFF6750A4),
                    modifier = Modifier.size(24.dp)
                )
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(Color(0xFFFEF7FF))
        ) {
            // Month Header Selector Card
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFF7F2FA)),
                shape = RoundedCornerShape(20.dp),
                border = BorderStroke(1.dp, Color(0xFFCAC4D0).copy(alpha = 0.5f))
            ) {
                Column(
                    modifier = Modifier.padding(14.dp)
                ) {
                    // Month title navigation
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        IconButton(
                            onClick = {
                                val nextCal = currentMonthCalendar.clone() as Calendar
                                nextCal.add(Calendar.MONTH, -1)
                                currentMonthCalendar = nextCal
                            }
                        ) {
                            Icon(Icons.Default.ChevronLeft, "Previous Month", tint = Color(0xFF6750A4))
                        }

                        Text(
                            text = "$monthName $year",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF21005D),
                                fontSize = 18.sp
                            )
                        )

                        IconButton(
                            onClick = {
                                val nextCal = currentMonthCalendar.clone() as Calendar
                                nextCal.add(Calendar.MONTH, 1)
                                currentMonthCalendar = nextCal
                            }
                        ) {
                            Icon(Icons.Default.ChevronRight, "Next Month", tint = Color(0xFF6750A4))
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // Weekdays headers row
                    val weekdays = listOf("M", "T", "W", "T", "F", "S", "S")
                    Row(modifier = Modifier.fillMaxWidth()) {
                        weekdays.forEach { dayName ->
                            Text(
                                text = dayName,
                                modifier = Modifier.weight(1f),
                                textAlign = TextAlign.Center,
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF49454F).copy(alpha = 0.8f)
                                )
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(6.dp))

                    // Days grid
                    val chunkedWeeks = calendarDays.chunked(7)
                    chunkedWeeks.forEach { week ->
                        Row(modifier = Modifier.fillMaxWidth()) {
                            week.forEach { date ->
                                val isSelected = isSameDay(date.timestamp, selectedDate.timeInMillis)
                                val hasTasks = uiState.todos.any { todo ->
                                    todo.dueDate != null && isSameDay(todo.dueDate, date.timestamp)
                                }

                                Box(
                                    modifier = Modifier
                                        .weight(1f)
                                        .aspectRatio(1f)
                                        .padding(2.dp)
                                        .clip(CircleShape)
                                        .background(
                                            if (isSelected) Color(0xFF6750A4)
                                            else Color.Transparent
                                        )
                                        .clickable {
                                            val newSelected = Calendar.getInstance().apply {
                                                timeInMillis = date.timestamp
                                            }
                                            selectedDate = newSelected
                                        },
                                    contentAlignment = Alignment.Center
                                ) {
                                    Column(
                                        horizontalAlignment = Alignment.CenterHorizontally,
                                        verticalArrangement = Arrangement.Center
                                    ) {
                                        Text(
                                            text = date.day.toString(),
                                            style = MaterialTheme.typography.bodyMedium.copy(
                                                fontWeight = if (isSelected) FontWeight.ExtraBold else FontWeight.Medium,
                                                color = when {
                                                    isSelected -> Color.White
                                                    !date.isCurrentMonth -> Color.Gray.copy(alpha = 0.4f)
                                                    else -> Color(0xFF1D1B20)
                                                }
                                            )
                                        )
                                        
                                        // Visual Dot Indicator for tasks
                                        if (hasTasks) {
                                            Box(
                                                modifier = Modifier
                                                    .size(5.dp)
                                                    .padding(top = 1.dp)
                                                    .background(
                                                        color = if (isSelected) Color.White else Color(0xFFFFB74D),
                                                        shape = CircleShape
                                                    )
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }

            // Tasks corresponding to the selected date (Compose RecyclerView / LazyColumn)
            val selectedDateStr = DateFormat.format("MMMM dd, yyyy", selectedDate.timeInMillis).toString()
            val dayTodos = remember(uiState.todos, selectedDate) {
                uiState.todos.filter { todo ->
                    todo.dueDate != null && isSameDay(todo.dueDate, selectedDate.timeInMillis)
                }
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 10.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Agenda: $selectedDateStr",
                    style = MaterialTheme.typography.titleSmall.copy(
                        fontWeight = FontWeight.ExtraBold,
                        color = Color(0xFF21005D)
                    )
                )

                Text(
                    text = "${dayTodos.size} tasks",
                    fontSize = 12.sp,
                    color = Color.Gray,
                    fontWeight = FontWeight.SemiBold
                )
            }

            if (dayTodos.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .padding(24.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.CheckCircle,
                            contentDescription = null,
                            tint = Color(0xFF81C784).copy(alpha = 0.7f),
                            modifier = Modifier.size(64.dp)
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = "No tasks set on this date!",
                            color = Color(0xFF49454F),
                            fontSize = 14.sp,
                            fontWeight = FontWeight.SemiBold,
                            textAlign = TextAlign.Center
                        )
                        Text(
                            text = "You're completely clear to enjoy your day.",
                            color = Color.Gray,
                            fontSize = 12.sp,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    contentPadding = PaddingValues(start = 16.dp, end = 16.dp, bottom = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(
                        items = dayTodos,
                        key = { it.id }
                    ) { todo ->
                        val priorityColor = when (todo.priority.uppercase()) {
                            "HIGH" -> Color(0xFFE57373)
                            "MEDIUM" -> Color(0xFFFFB74D)
                            "LOW" -> Color(0xFF81C784)
                            else -> Color(0xFF6750A4)
                        }

                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .border(1.dp, Color(0xFFCAC4D0).copy(alpha = 0.7f), RoundedCornerShape(16.dp))
                                .clickable { onNavigateToDetail(todo.id) },
                            colors = CardDefaults.cardColors(
                                containerColor = if (todo.isCompleted) Color(0xFFEADDFF).copy(alpha = 0.2f) else Color(0xFFF7F2FA)
                            ),
                            shape = RoundedCornerShape(16.dp)
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(14.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                // Mini checkbox
                                Box(
                                    modifier = Modifier
                                        .size(24.dp)
                                        .clip(RoundedCornerShape(6.dp))
                                        .border(2.dp, Color(0xFF6750A4), RoundedCornerShape(6.dp))
                                        .background(if (todo.isCompleted) Color(0xFF6750A4) else Color.Transparent)
                                        .clickable { viewModel.toggleTodoCompletion(todo) },
                                    contentAlignment = Alignment.Center
                                ) {
                                    if (todo.isCompleted) {
                                        Icon(
                                            imageVector = Icons.Default.Check,
                                            contentDescription = "Checked",
                                            tint = Color.White,
                                            modifier = Modifier.size(16.dp)
                                        )
                                    }
                                }

                                Spacer(modifier = Modifier.width(14.dp))

                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = todo.title,
                                        style = MaterialTheme.typography.titleMedium.copy(
                                            fontWeight = FontWeight.Bold,
                                            textDecoration = if (todo.isCompleted) TextDecoration.LineThrough else TextDecoration.None,
                                            color = if (todo.isCompleted) Color(0xFF1D1B20).copy(alpha = 0.5f) else Color(0xFF1D1B20)
                                        ),
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis
                                    )

                                    if (todo.description.isNotEmpty()) {
                                        Text(
                                            text = todo.description,
                                            style = MaterialTheme.typography.bodyMedium.copy(
                                                textDecoration = if (todo.isCompleted) TextDecoration.LineThrough else TextDecoration.None,
                                                color = Color(0xFF49454F).copy(alpha = if (todo.isCompleted) 0.5f else 1.0f)
                                            ),
                                            maxLines = 1,
                                            overflow = TextOverflow.Ellipsis
                                        )
                                    }
                                }

                                Spacer(modifier = Modifier.width(8.dp))

                                Column(horizontalAlignment = Alignment.End) {
                                    Box(
                                        modifier = Modifier
                                            .background(priorityColor.copy(alpha = 0.15f), RoundedCornerShape(6.dp))
                                            .padding(horizontal = 8.dp, vertical = 2.dp)
                                    ) {
                                        Text(
                                            text = todo.priority.uppercase(),
                                            fontSize = 9.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = priorityColor
                                        )
                                    }
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(
                                        text = todo.category,
                                        fontSize = 11.sp,
                                        color = Color(0xFF6750A4),
                                        fontWeight = FontWeight.Medium
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

// Model data class for the calendar layout cells
data class CalendarDate(
    val day: Int,
    val month: Int,
    val year: Int,
    val timestamp: Long,
    val isCurrentMonth: Boolean
)

// Helper to determine if two timestamp values are on the same calendar day
fun isSameDay(t1: Long, t2: Long): Boolean {
    val cal1 = Calendar.getInstance().apply { timeInMillis = t1 }
    val cal2 = Calendar.getInstance().apply { timeInMillis = t2 }
    return cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) &&
           cal1.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR)
}
