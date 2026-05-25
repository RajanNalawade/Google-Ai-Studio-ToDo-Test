package com.example

import android.content.Context
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onRoot
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.example.data.local.TodoDatabase
import com.example.data.repository.TodoRepository
import com.example.ui.screens.TodoScreen
import com.example.ui.theme.MyApplicationTheme
import com.example.ui.viewmodel.TodoViewModel
import com.github.takahirom.roborazzi.RobolectricDeviceQualifiers
import com.github.takahirom.roborazzi.captureRoboImage
import kotlinx.coroutines.runBlocking
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import org.robolectric.annotation.GraphicsMode

@RunWith(RobolectricTestRunner::class)
@GraphicsMode(GraphicsMode.Mode.NATIVE)
@Config(qualifiers = RobolectricDeviceQualifiers.Pixel8, sdk = [36])
class GreetingScreenshotTest {

  @get:Rule val composeTestRule = createComposeRule()

  @Test
  fun greeting_screenshot() {
    val context = ApplicationProvider.getApplicationContext<Context>()
    val database = Room.inMemoryDatabaseBuilder(
        context,
        TodoDatabase::class.java
    ).allowMainThreadQueries().build()
    
    val repository = TodoRepository(database.todoDao)
    val viewModel = TodoViewModel(repository)

    // Prepopulate some items for a beautiful dashboard screenshot
    runBlocking {
        viewModel.saveTodo("Submit design proposal", "With high priority", "HIGH", "Work", null)
        viewModel.saveTodo("Pick up groceries", "Milk, veggies", "LOW", "Shopping", null)
        viewModel.saveTodo("Complete cardio warmup", "30 minutes treadmill", "MEDIUM", "Fitness", null)
    }

    composeTestRule.setContent {
      MyApplicationTheme {
        TodoScreen(
          viewModel = viewModel,
          onNavigateToDetail = {},
          modifier = Modifier.fillMaxSize()
        )
      }
    }

    // Wait for idle to complete drawing
    composeTestRule.waitForIdle()

    // Capture the screenshot view
    composeTestRule.onRoot().captureRoboImage(filePath = "src/test/screenshots/todo_dashboard.png")
  }
}
