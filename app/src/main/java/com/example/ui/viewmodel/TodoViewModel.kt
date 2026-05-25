package com.example.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.data.model.TodoEntity
import com.example.data.repository.TodoRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

enum class SortOption {
    DATE_ASC,
    DATE_DESC,
    PRIORITY_HIGH_LOW,
    ALPHABETICAL
}

data class TodoFilterParams(
    val searchQuery: String = "",
    val selectedCategory: String = "All", // "All", "Work", "Personal", "Shopping", "Fitness", "Other"
    val selectedPriority: String = "All", // "All", "HIGH", "MEDIUM", "LOW"
    val sortBy: SortOption = SortOption.DATE_DESC,
    val currentTodoEdit: TodoEntity? = null,
    val showAddEditDialog: Boolean = false,
    val isLoading: Boolean = false
)

data class TodoUiState(
    val todos: List<TodoEntity> = emptyList(),
    val filteredTodos: List<TodoEntity> = emptyList(),
    val searchQuery: String = "",
    val selectedCategory: String = "All",
    val selectedPriority: String = "All",
    val sortBy: SortOption = SortOption.DATE_DESC,
    val currentTodoEdit: TodoEntity? = null,
    val isLoading: Boolean = false,
    val showAddEditDialog: Boolean = false
)

class TodoViewModel(private val repository: TodoRepository) : ViewModel() {

    private val _filterParams = MutableStateFlow(TodoFilterParams())

    // Combine just two flows: repository item stream and filter state parameters
    val uiState: StateFlow<TodoUiState> = combine(
        repository.allTodos,
        _filterParams
    ) { todos, params ->
        
        // Filter elements
        val filtered = todos.filter { todo ->
            val matchesQuery = todo.title.contains(params.searchQuery, ignoreCase = true) || 
                               todo.description.contains(params.searchQuery, ignoreCase = true)
            val matchesCategory = params.selectedCategory == "All" || todo.category.equals(params.selectedCategory, ignoreCase = true)
            val matchesPriority = params.selectedPriority == "All" || todo.priority.equals(params.selectedPriority, ignoreCase = true)
            matchesQuery && matchesCategory && matchesPriority
        }.sortedWith { a, b ->
            when (params.sortBy) {
                SortOption.DATE_ASC -> a.createdAt.compareTo(b.createdAt)
                SortOption.DATE_DESC -> b.createdAt.compareTo(a.createdAt)
                SortOption.PRIORITY_HIGH_LOW -> {
                    val pA = getPriorityValue(a.priority)
                    val pB = getPriorityValue(b.priority)
                    if (pA != pB) pB.compareTo(pA) else b.createdAt.compareTo(a.createdAt)
                }
                SortOption.ALPHABETICAL -> a.title.lowercase().compareTo(b.title.lowercase())
            }
        }

        TodoUiState(
            todos = todos,
            filteredTodos = filtered,
            searchQuery = params.searchQuery,
            selectedCategory = params.selectedCategory,
            selectedPriority = params.selectedPriority,
            sortBy = params.sortBy,
            currentTodoEdit = params.currentTodoEdit,
            showAddEditDialog = params.showAddEditDialog,
            isLoading = params.isLoading
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = TodoUiState(isLoading = true)
    )

    private fun getPriorityValue(priority: String): Int {
        return when (priority.uppercase()) {
            "HIGH" -> 3
            "MEDIUM" -> 2
            "LOW" -> 1
            else -> 0
        }
    }

    fun setSearchQuery(query: String) {
        _filterParams.update { it.copy(searchQuery = query) }
    }

    fun setCategoryFilter(category: String) {
        _filterParams.update { it.copy(selectedCategory = category) }
    }

    fun setPriorityFilter(priority: String) {
        _filterParams.update { it.copy(selectedPriority = priority) }
    }

    fun setSortOption(sortOption: SortOption) {
        _filterParams.update { it.copy(sortBy = sortOption) }
    }

    fun openAddDialog() {
        _filterParams.update { it.copy(currentTodoEdit = null, showAddEditDialog = true) }
    }

    fun openEditDialog(todo: TodoEntity) {
        _filterParams.update { it.copy(currentTodoEdit = todo, showAddEditDialog = true) }
    }

    fun closeAddEditDialog() {
        _filterParams.update { it.copy(currentTodoEdit = null, showAddEditDialog = false) }
    }

    fun deleteTodo(todo: TodoEntity) {
        viewModelScope.launch {
            repository.deleteTodo(todo)
        }
    }

    fun toggleTodoCompletion(todo: TodoEntity) {
        viewModelScope.launch {
            repository.updateTodo(todo.copy(isCompleted = !todo.isCompleted))
        }
    }

    fun saveTodo(
        title: String,
        description: String,
        priority: String,
        category: String,
        dueDate: Long?
    ) {
        if (title.isBlank()) return

        viewModelScope.launch {
            val current = _filterParams.value.currentTodoEdit
            if (current != null) {
                // Updating existing
                repository.updateTodo(
                    current.copy(
                        title = title,
                        description = description,
                        priority = priority,
                        category = category,
                        dueDate = dueDate
                    )
                )
            } else {
                // Adding new
                val newTodo = TodoEntity(
                    title = title,
                    description = description,
                    priority = priority,
                    category = category,
                    dueDate = dueDate
                )
                repository.insertTodo(newTodo)
            }
            closeAddEditDialog()
        }
    }
}

class TodoViewModelFactory(private val repository: TodoRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TodoViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return TodoViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
