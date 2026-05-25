package com.example

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.example.data.local.TodoDao
import com.example.data.local.TodoDatabase
import com.example.data.model.TodoEntity
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36])
class TodoDatabaseTest {

    private lateinit var database: TodoDatabase
    private lateinit var todoDao: TodoDao

    @Before
    fun setUp() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        database = Room.inMemoryDatabaseBuilder(
            context,
            TodoDatabase::class.java
        ).allowMainThreadQueries() // allow main thread queries for simple tests
         .build()
        todoDao = database.todoDao
    }

    @After
    fun tearDown() {
        database.close()
    }

    @Test
    fun insertAndGetTodoItem() = runBlocking {
        val todo = TodoEntity(
            title = "Test Task",
            description = "This is a test task details",
            priority = "HIGH",
            category = "Work"
        )
        
        val id = todoDao.insertTodo(todo)
        val loaded = todoDao.getTodoById(id)

        assertNotNull(loaded)
        assertEquals(id, loaded?.id)
        assertEquals("Test Task", loaded?.title)
        assertEquals("HIGH", loaded?.priority)
        assertEquals("Work", loaded?.category)
    }

    @Test
    fun updateTodoItem() = runBlocking {
        val todo = TodoEntity(
            title = "Task to Update",
            description = "Some description",
            isCompleted = false
        )
        val id = todoDao.insertTodo(todo)
        val inserted = todoDao.getTodoById(id)!!

        val updatedTodo = inserted.copy(isCompleted = true, title = "Updated Title")
        todoDao.updateTodo(updatedTodo)

        val retrieved = todoDao.getTodoById(id)
        assertNotNull(retrieved)
        assertEquals(true, retrieved?.isCompleted)
        assertEquals("Updated Title", retrieved?.title)
    }

    @Test
    fun deleteTodoItem() = runBlocking {
        val todo = TodoEntity(
            title = "Task to Delete",
            description = "To be deleted"
        )
        val id = todoDao.insertTodo(todo)
        assertNotNull(todoDao.getTodoById(id))

        todoDao.deleteTodoById(id)

        val retrieved = todoDao.getTodoById(id)
        assertNull(retrieved)
    }

    @Test
    fun getAllTodosFlow() = runBlocking {
        val todo1 = TodoEntity(title = "Task 1", createdAt = 1000L)
        val todo2 = TodoEntity(title = "Task 2", createdAt = 2000L)

        todoDao.insertTodo(todo1)
        todoDao.insertTodo(todo2)

        val todos = todoDao.getAllTodos().first()
        assertEquals(2, todos.size)
        // Check order (Newest first, meaning createdAt descending or whatever order SQL Query states)
        assertEquals("Task 2", todos[0].title)
        assertEquals("Task 1", todos[1].title)
    }
}
