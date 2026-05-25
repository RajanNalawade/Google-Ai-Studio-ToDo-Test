package com.example.ui.screens

import android.text.format.DateFormat
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Assignment
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Analytics
import androidx.compose.material.icons.filled.Assignment
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.Work
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SuggestionChip
import androidx.compose.material3.SuggestionChipDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.TodoEntity
import com.example.ui.viewmodel.SortOption
import com.example.ui.viewmodel.TodoViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TodoScreen(
    viewModel: TodoViewModel,
    onNavigateToDetail: (Long) -> Unit,
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = Color(0xFFFEF7FF),
        floatingActionButton = {
            FloatingActionButton(
                onClick = { viewModel.openAddDialog() },
                containerColor = Color(0xFFEADDFF),
                contentColor = Color(0xFF21005D),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier
                    .testTag("add_todo_fab")
                    .padding(8.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add Todo Item",
                    modifier = Modifier.size(28.dp)
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
            // Elegant Bespoke Header following requested HTML layout exactly
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp, end = 16.dp, top = 24.dp, bottom = 12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // User Initials Avatar badge
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .background(Color(0xFFEADDFF), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "JS",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF21005D),
                            fontSize = 16.sp
                        )
                    )
                }

                // Top Actions Header Button Icons
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .clip(CircleShape)
                            .clickable { /* Action option */ }
                            .padding(8.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = "Search",
                            tint = Color(0xFF1D1B20)
                        )
                    }
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .clip(CircleShape)
                            .clickable { /* Action option */ }
                            .padding(8.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.MoreVert,
                            contentDescription = "More options",
                            tint = Color(0xFF1D1B20)
                        )
                    }
                }
            }

            // Big Bold Header Title and Subtitle Info
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp, end = 16.dp, bottom = 16.dp)
            ) {
                Text(
                    text = "Today's Tasks",
                    style = MaterialTheme.typography.headlineLarge.copy(
                        fontWeight = FontWeight.Medium,
                        color = Color(0xFF1D1B20),
                        fontSize = 30.sp,
                        letterSpacing = (-0.5).sp
                    )
                )
                Text(
                    text = "May 25, 2026",
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = Color(0xFF49454F),
                        fontSize = 14.sp
                    ),
                    modifier = Modifier.padding(top = 2.dp)
                )
            }

            // Summary Stats Panel
            StatsSection(todos = uiState.todos)

            // Search Bar Component
            SearchBarSection(
                searchQuery = uiState.searchQuery,
                onSearchChanged = { viewModel.setSearchQuery(it) }
            )

            // Category & Sort Tabs Section
            FiltersAndSortingSection(
                selectedCategory = uiState.selectedCategory,
                onCategorySelected = { viewModel.setCategoryFilter(it) },
                selectedPriority = uiState.selectedPriority,
                onPrioritySelected = { viewModel.setPriorityFilter(it) },
                currentSortOption = uiState.sortBy,
                onSortOptionSelected = { viewModel.setSortOption(it) }
            )

            // Dynamic items list
            Box(modifier = Modifier.weight(1f)) {
                if (uiState.filteredTodos.isEmpty()) {
                    EmptyStateSection(
                        isSearching = uiState.searchQuery.isNotEmpty() ||
                                uiState.selectedCategory != "All" ||
                                uiState.selectedPriority != "All"
                    )
                } else {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(bottom = 16.dp)
                    ) {
                        items(
                            items = uiState.filteredTodos,
                            key = { it.id }
                        ) { todo ->
                            TodoItemCard(
                                todo = todo,
                                onToggleComplete = { viewModel.toggleTodoCompletion(todo) },
                                onDelete = { viewModel.deleteTodo(todo) },
                                onEdit = { viewModel.openEditDialog(todo) }, onCardClick = { onNavigateToDetail(todo.id) }
                            )
                        }
                    }
                }
            }
        }

        // Add/Edit Dialog popup
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

@Composable
fun StatsSection(todos: List<TodoEntity>) {
    val totalCount = todos.size
    val completedCount = todos.count { it.isCompleted }
    val remainingCount = totalCount - completedCount
    val progressPercent = if (totalCount > 0) (completedCount * 100) / totalCount else 0

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .background(Color(0xFFEADDFF), RoundedCornerShape(28.dp))
            .padding(24.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text(
                    text = "COMPLETION",
                    color = Color(0xFF21005D),
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.5.sp
                )
                Text(
                    text = "$progressPercent%",
                    color = Color(0xFF21005D),
                    fontSize = 36.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "$remainingCount tasks remaining",
                    color = Color(0xFF49454F),
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Normal
                )
            }

            // Dynamic circle showing completed ratio (e.g. 9/14)
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .size(64.dp)
                    .border(width = 4.dp, color = Color(0xFF21005D), shape = CircleShape)
            ) {
                Text(
                    text = "$completedCount/$totalCount",
                    color = Color(0xFF21005D),
                    fontWeight = FontWeight.Bold,
                    fontSize = 12.sp
                )
            }
        }
    }
}

@Composable
fun SearchBarSection(
    searchQuery: String,
    onSearchChanged: (String) -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        OutlinedTextField(
            value = searchQuery,
            onValueChange = onSearchChanged,
            placeholder = { Text("Search your planner...") },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Search Icon"
                )
            },
            trailingIcon = {
                if (searchQuery.isNotEmpty()) {
                    IconButton(onClick = { onSearchChanged("") }) {
                        Icon(
                            imageVector = Icons.Default.Clear,
                            contentDescription = "Clear Search"
                        )
                    }
                }
            },
            singleLine = true,
            modifier = Modifier
                .fillMaxWidth()
                .testTag("search_input_field"),
            shape = RoundedCornerShape(16.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedTextColor = Color(0xFF1D1B20),
                unfocusedTextColor = Color(0xFF1D1B20),
                focusedBorderColor = Color(0xFF6750A4),
                unfocusedBorderColor = Color(0xFFCAC4D0)
            )
        )
    }
}

@Composable
fun FiltersAndSortingSection(
    selectedCategory: String,
    onCategorySelected: (String) -> Unit,
    selectedPriority: String,
    onPrioritySelected: (String) -> Unit,
    currentSortOption: SortOption,
    onSortOptionSelected: (SortOption) -> Unit
) {
    val categories = listOf("All", "Personal", "Work", "Shopping", "Fitness", "Other")
    val priorities = listOf("All", "HIGH", "MEDIUM", "LOW")
    val sortOptions = listOf(
        SortOption.DATE_DESC to "Newest First",
        SortOption.DATE_ASC to "Oldest First",
        SortOption.PRIORITY_HIGH_LOW to "Priority",
        SortOption.ALPHABETICAL to "A-Z"
    )

    Column(modifier = Modifier.fillMaxWidth()) {
        // Categories Scroll
        LazyRow(
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 4.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            items(categories) { category ->
                val isSelected = category == selectedCategory
                SuggestionChip(
                    onClick = { onCategorySelected(category) },
                    label = { Text(category) },
                    modifier = Modifier.testTag("category_pill_$category"),
                    colors = SuggestionChipDefaults.suggestionChipColors(
                        containerColor = if (isSelected) Color(0xFF6750A4) else Color(0xFFF7F2FA),
                        labelColor = if (isSelected) Color.White else Color(0xFF1D1B20)
                    ),
                    border = if (isSelected) null else SuggestionChipDefaults.suggestionChipBorder(
                        enabled = true,
                        borderColor = Color(0xFFCAC4D0)
                    )
                )
            }
        }

        // Sub Bar: Priorities & SortOptions Scroll
        LazyRow(
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 4.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Priority tags
            items(priorities) { priority ->
                val isSelected = priority == selectedPriority
                val displayText = if (priority == "All") "P-All" else priority
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(
                            if (isSelected) Color(0xFF625B71) else Color(0xFFE8DEF8).copy(alpha = 0.4f)
                        )
                        .clickable { onPrioritySelected(priority) }
                        .padding(horizontal = 10.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = displayText,
                        style = MaterialTheme.typography.labelMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = if (isSelected) Color.White else Color(0xFF1D192B)
                        )
                    )
                }
            }

            item {
                Spacer(
                    modifier = Modifier
                        .width(1.dp)
                        .height(24.dp)
                        .background(Color(0xFFCAC4D0).copy(alpha = 0.5f))
                )
            }

            // Sort Options
            items(sortOptions) { (option, label) ->
                val isSelected = option == currentSortOption
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(
                            if (isSelected) Color(0xFFE8DEF8) else Color(0xFFFEF7FF)
                        )
                        .border(
                            width = 1.dp,
                            color = Color(0xFFCAC4D0).copy(alpha = 0.7f),
                            shape = RoundedCornerShape(8.dp)
                        )
                        .clickable { onSortOptionSelected(option) }
                        .padding(horizontal = 10.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = label,
                        style = MaterialTheme.typography.labelMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = if (isSelected) Color(0xFF1D192B) else Color(0xFF49454F)
                        )
                    )
                }
            }
        }
        Spacer(modifier = Modifier.height(4.dp))
    }
}

@Composable
fun TodoItemCard(
    todo: TodoEntity,
    onToggleComplete: () -> Unit,
    onDelete: () -> Unit,
    onEdit: () -> Unit,
    onCardClick: () -> Unit
) {
    val categoryIcon = when (todo.category.lowercase()) {
        "work" -> Icons.Default.Work
        "shopping" -> Icons.Default.ShoppingCart
        "fitness" -> Icons.Default.FitnessCenter
        else -> Icons.AutoMirrored.Filled.Assignment
    }

    val priorityColor = when (todo.priority.uppercase()) {
        "HIGH" -> Color(0xFFE57373) // soft red
        "MEDIUM" -> Color(0xFFFFB74D) // soft orange
        "LOW" -> Color(0xFF81C784) // soft green
        else -> Color(0xFF6750A4)
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 6.dp)
            .testTag("todo_card_${todo.id}")
            .background(
                color = if (todo.isCompleted) Color(0xFFF7F2FA).copy(alpha = 0.6f) else Color(0xFFF7F2FA),
                shape = RoundedCornerShape(16.dp)
            )
            .border(
                width = 1.dp,
                color = Color(0xFFCAC4D0),
                shape = RoundedCornerShape(16.dp)
            )
            .clickable { onCardClick() }
            .padding(14.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Highly robust, custom geometrically balanced checkmark checkbox action target
            Box(
                modifier = Modifier
                    .size(24.dp)
                    .clip(RoundedCornerShape(6.dp))
                    .border(
                        width = 2.dp,
                        color = Color(0xFF6750A4),
                        shape = RoundedCornerShape(6.dp)
                    )
                    .background(
                        color = if (todo.isCompleted) Color(0xFF6750A4) else Color.Transparent
                    )
                    .clickable { onToggleComplete() }
                    .testTag("todo_checkbox_${todo.id}"),
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
                        fontWeight = FontWeight.Medium,
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
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.padding(top = 2.dp)
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Meta Info Row
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Start
                ) {
                    Row(
                        modifier = Modifier
                            .background(
                                color = Color(0xFFE8DEF8).copy(alpha = if (todo.isCompleted) 0.4f else 1.0f),
                                shape = RoundedCornerShape(6.dp)
                            )
                            .padding(horizontal = 8.dp, vertical = 2.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = categoryIcon,
                            contentDescription = todo.category,
                            modifier = Modifier.size(12.dp),
                            tint = Color(0xFF1D192B)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = todo.category,
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF1D192B)
                            )
                        )
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    Box(
                        modifier = Modifier
                            .background(
                                color = priorityColor.copy(alpha = if (todo.isCompleted) 0.08f else 0.15f),
                                shape = RoundedCornerShape(6.dp)
                            )
                            .padding(horizontal = 8.dp, vertical = 2.dp)
                    ) {
                        Text(
                            text = todo.priority.uppercase(),
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontWeight = FontWeight.ExtraBold,
                                color = if (todo.isCompleted) priorityColor.copy(alpha = 0.6f) else priorityColor
                            )
                        )
                    }

                    if (todo.dueDate != null) {
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = DateFormat.format("MMM dd, yyyy", todo.dueDate).toString(),
                            style = MaterialTheme.typography.labelSmall.copy(
                                color = Color(0xFF49454F).copy(alpha = 0.6f)
                            )
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.width(8.dp))

            // Action Buttons
            Row {
                IconButton(onClick = onEdit) {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = "Edit Item",
                        tint = Color(0xFF6750A4).copy(alpha = if (todo.isCompleted) 0.4f else 0.8f)
                    )
                }
                IconButton(
                    onClick = onDelete,
                    modifier = Modifier.testTag("todo_delete_button_${todo.id}")
                ) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Delete Item",
                        tint = Color(0xFFE57373).copy(alpha = if (todo.isCompleted) 0.4f else 0.8f)
                    )
                }
            }
        }
    }
}

@Composable
fun EmptyStateSection(isSearching: Boolean) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = if (isSearching) Icons.Default.Info else Icons.AutoMirrored.Filled.Assignment,
            contentDescription = null,
            tint = Color(0xFFCAC4D0),
            modifier = Modifier.size(64.dp)
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = if (isSearching) "No matching tasks found." else "All caught up!",
            style = MaterialTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.Bold,
                color = Color(0xFF1D1B20)
            )
        )
        Spacer(modifier = Modifier.height(6.dp))
        Text(
            text = if (isSearching) "Try resetting the filters or modifying your search." else "Tap the '+' button down below to add a new task.",
            style = MaterialTheme.typography.bodyMedium.copy(
                color = Color(0xFF49454F)
            ),
            textAlign = androidx.compose.ui.text.style.TextAlign.Center
        )
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun AddEditTodoDialog(
    currentTodo: TodoEntity?,
    onDismiss: () -> Unit,
    onSave: (String, String, String, String, Long?) -> Unit
) {
    var title by remember { mutableStateOf(currentTodo?.title ?: "") }
    var description by remember { mutableStateOf(currentTodo?.description ?: "") }
    var priority by remember { mutableStateOf(currentTodo?.priority ?: "MEDIUM") }
    var category by remember { mutableStateOf(currentTodo?.category ?: "Personal") }

    val categories = listOf("Personal", "Work", "Shopping", "Fitness", "Other")
    val priorities = listOf("LOW", "MEDIUM", "HIGH")

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = if (currentTodo != null) "Update Task" else "Create Task",
                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
            )
        },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("Task Title *") },
                    singleLine = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("dialog_title_input"),
                    shape = RoundedCornerShape(12.dp)
                )

                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text("Description") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("dialog_desc_input"),
                    maxLines = 3,
                    shape = RoundedCornerShape(12.dp)
                )

                // Priority Selection
                Column {
                    Text(
                        text = "Priority Level",
                        style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold)
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        priorities.forEach { p ->
                            val isSelected = p == priority
                            val activeColor = when (p) {
                                "HIGH" -> Color(0xFFE57373)
                                "MEDIUM" -> Color(0xFFFFB74D)
                                else -> Color(0xFF81C784)
                            }
                            Button(
                                onClick = { priority = p },
                                modifier = Modifier.weight(1f),
                                shape = RoundedCornerShape(10.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = if (isSelected) activeColor else Color(0xFFF7F2FA),
                                    contentColor = if (isSelected) Color.White else Color(0xFF1D1B20)
                                )
                            ) {
                                Text(p)
                            }
                        }
                    }
                }

                // Category Selection
                Column {
                    Text(
                        text = "Category",
                        style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold)
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    FlowRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        categories.forEach { cat ->
                            val isSelected = cat == category
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(
                                        if (isSelected) Color(0xFF6750A4) else Color(0xFFF7F2FA)
                                    )
                                    .clickable { category = cat }
                                    .padding(horizontal = 12.dp, vertical = 6.dp)
                            ) {
                                Text(
                                    text = cat,
                                    style = MaterialTheme.typography.bodyMedium.copy(
                                        fontWeight = FontWeight.SemiBold,
                                        color = if (isSelected) Color.White else Color(0xFF49454F)
                                    )
                                )
                            }
                        }
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (title.isNotBlank()) {
                        onSave(title, description, priority, category, currentTodo?.dueDate)
                    }
                },
                enabled = title.isNotBlank(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF6750A4),
                    contentColor = Color.White
                ),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.testTag("dialog_save_button")
            ) {
                Text(if (currentTodo != null) "Update" else "Add Task")
            }
        },
        dismissButton = {
            OutlinedButton(
                onClick = onDismiss,
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.testTag("dialog_cancel_button")
            ) {
                Text("Cancel")
            }
        },
        shape = RoundedCornerShape(24.dp)
    )
}
