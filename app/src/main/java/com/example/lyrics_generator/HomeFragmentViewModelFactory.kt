package com.example.lyrics_generator

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.lyrics_generator.database.LyricsDao
import java.lang.IllegalArgumentException

class HomeFragmentViewModelFactory(private val database: LyricsDao): ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HomeFragmentViewModel::class.java)) {
            return HomeFragmentViewModel(database) as T
        }
        throw IllegalArgumentException("Unknown ViewModelClass")
    }
}