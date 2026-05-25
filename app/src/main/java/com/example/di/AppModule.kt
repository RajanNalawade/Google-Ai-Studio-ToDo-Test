package com.example.di

import com.example.data.local.TodoDatabase
import com.example.data.repository.TodoRepository
import com.example.ui.viewmodel.TodoViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    // Single instance of database
    single { TodoDatabase.getInstance(androidContext()) }
    
    // Single instance of DAO
    single { get<TodoDatabase>().todoDao }
    
    // Single instance of repository
    single { TodoRepository(get()) }
    
    // ViewModel factory declaration
    viewModel { TodoViewModel(get()) }
}
