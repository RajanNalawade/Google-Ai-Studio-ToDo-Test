package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "todo_items")
data class TodoEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0L,
    val title: String,
    val description: String = "",
    val isCompleted: Boolean = false,
    val priority: String = "MEDIUM", // HIGH, MEDIUM, LOW
    val category: String = "Personal", // Work, Personal, Shopping, etc.
    val dueDate: Long? = null,
    val createdAt: Long = System.currentTimeMillis()
)
