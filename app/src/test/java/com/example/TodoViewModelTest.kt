package com.example

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.example.data.local.TodoDatabase
import com.example.data.model.TodoEntity
import com.example.data.repository.TodoRepository
import com.example.ui.viewmodel.SortOption
import com.example.ui.viewmodel.TodoViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36])
class TodoViewModelTest {

    private lateinit var database: TodoDatabase
    private lateinit var repository: TodoRepository
    private lateinit var viewModel: TodoViewModel
    private val testDispatcher: TestDispatcher = StandardTestDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        val context = ApplicationProvider.getApplicationContext<Context>()
        database = Room.inMemoryDatabaseBuilder(
            context,
            TodoDatabase::class.java
        ).allowMainThreadQueries()
         .setQueryExecutor { it.run() }
         .setTransactionExecutor { it.run() }
         .build()
        
        repository = TodoRepository(database.todoDao)
        viewModel = TodoViewModel(repository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        database.close()
    }

    @Test
    fun initialUiState_isEmpty() = runTest {
        val collectJob = launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.uiState.collect {}
        }

        testDispatcher.scheduler.advanceUntilIdle()
        
        val state = viewModel.uiState.value
        assertTrue(state.todos.isEmpty())
        assertTrue(state.filteredTodos.isEmpty())
        assertEquals("", state.searchQuery)
        assertEquals("All", state.selectedCategory)
        assertEquals("All", state.selectedPriority)

        collectJob.cancel()
    }

    @Test
    fun insertAndVerifySaveTodo() = runTest {
        val collectJob = launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.uiState.collect {}
        }

        viewModel.saveTodo(
            title = "Task 1",
            description = "Details 1",
            priority = "HIGH",
            category = "Work",
            dueDate = null
        )
        testDispatcher.scheduler.advanceUntilIdle()

        val state = viewModel.uiState.value
        assertEquals(1, state.todos.size)
        assertEquals("Task 1", state.todos[0].title)
        assertEquals("HIGH", state.todos[0].priority)
        assertEquals("Work", state.todos[0].category)

        collectJob.cancel()
    }

    @Test
    fun deleteTodo_removesItem() = runTest {
        val collectJob = launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.uiState.collect {}
        }

        viewModel.saveTodo("Task to Delete", "Details", "LOW", "Personal", null)
        testDispatcher.scheduler.advanceUntilIdle()

        var state = viewModel.uiState.value
        assertEquals(1, state.todos.size)
        val todoToDelete = state.todos[0]

        viewModel.deleteTodo(todoToDelete)
        testDispatcher.scheduler.advanceUntilIdle()

        state = viewModel.uiState.value
        assertTrue(state.todos.isEmpty())

        collectJob.cancel()
    }

    @Test
    fun toggleCompletion_updatesState() = runTest {
        val collectJob = launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.uiState.collect {}
        }

        viewModel.saveTodo("Toggle Task", "Details", "MEDIUM", "Personal", null)
        testDispatcher.scheduler.advanceUntilIdle()

        var state = viewModel.uiState.value
        val todo = state.todos[0]
        assertFalse(todo.isCompleted)

        viewModel.toggleTodoCompletion(todo)
        testDispatcher.scheduler.advanceUntilIdle()

        state = viewModel.uiState.value
        assertTrue(state.todos[0].isCompleted)

        collectJob.cancel()
    }

    @Test
    fun searchAndFiltersCombineProperly() = runTest {
        val collectJob = launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.uiState.collect {}
        }

        // Insert tasks for searching and sorting
        viewModel.saveTodo("Buy groceries", "Milk and Eggs", "LOW", "Shopping", null)
        viewModel.saveTodo("Submit report", "Q2 reports", "HIGH", "Work", null)
        viewModel.saveTodo("Morning jogging", "Run 5k", "MEDIUM", "Fitness", null)
        testDispatcher.scheduler.advanceUntilIdle()

        // 1. Search filter
        viewModel.setSearchQuery("groceries")
        testDispatcher.scheduler.advanceUntilIdle()
        var state = viewModel.uiState.value
        assertEquals(1, state.filteredTodos.size)
        assertEquals("Buy groceries", state.filteredTodos[0].title)

        // Reset search, apply category filter
        viewModel.setSearchQuery("")
        viewModel.setCategoryFilter("Work")
        testDispatcher.scheduler.advanceUntilIdle()
        state = viewModel.uiState.value
        assertEquals(1, state.filteredTodos.size)
        assertEquals("Submit report", state.filteredTodos[0].title)

        // Reset category, apply priority filter
        viewModel.setCategoryFilter("All")
        viewModel.setPriorityFilter("MEDIUM")
        testDispatcher.scheduler.advanceUntilIdle()
        state = viewModel.uiState.value
        assertEquals(1, state.filteredTodos.size)
        assertEquals("Morning jogging", state.filteredTodos[0].title)

        collectJob.cancel()
    }

    @Test
    fun sortOption_ordersResultsCorrectly() = runTest {
        val collectJob = launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.uiState.collect {}
        }

        viewModel.saveTodo("A item", "Desc", "LOW", "Other", null)
        viewModel.saveTodo("Z item", "Desc", "HIGH", "Other", null)
        testDispatcher.scheduler.advanceUntilIdle()

        viewModel.setSortOption(SortOption.ALPHABETICAL)
        testDispatcher.scheduler.advanceUntilIdle()

        val state = viewModel.uiState.value
        assertEquals(2, state.filteredTodos.size)
        assertEquals("A item", state.filteredTodos[0].title)
        assertEquals("Z item", state.filteredTodos[1].title)

        collectJob.cancel()
    }
}
