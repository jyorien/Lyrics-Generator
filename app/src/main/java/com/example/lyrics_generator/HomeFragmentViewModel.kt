package com.example.lyrics_generator

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.lyrics_generator.database.Lyrics
import com.example.lyrics_generator.database.LyricsDao
import kotlinx.coroutines.*
import java.lang.NullPointerException
import kotlin.random.Random

class HomeFragmentViewModel(val database: LyricsDao): ViewModel() {

    private val _lyrics = MutableLiveData<String>()
    val lyrics: LiveData<String>
            get() = _lyrics
    private lateinit var lyricsObject: Lyrics

    init {
        displayNextLyrics()
    }

    fun displayNextLyrics() {
        val randomNum = Random.nextLong(1,MAX+1.toLong())

        // launch coroutine in viewModelScope so that it cancels with viewmodel
        viewModelScope.launch {
            lateinit var newLyrics: String
            withContext(Dispatchers.IO) {
                // call database from BACKGROUND THREAD
                if (database.checkDB() > 0) {
                    lyricsObject = database.getSingleLyrics(randomNum)
                    newLyrics = lyricsObject.lyrics
                }
                else
                    newLyrics = "Press random to start!"
            }
            withContext(Dispatchers.Main) {
                // update UI on MAIN THREAD
                _lyrics.value = newLyrics
            }
        }
    }

    fun favourite() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                if (lyricsObject.isFavourite) {
                    database.deleteFavourite(lyricsObject.lyricsId)
                    lyricsObject.isFavourite = false
                }
                else {
                    database.addFavourite(lyricsObject.lyricsId)
                    lyricsObject.isFavourite = true
                }

            }
        }

    }

}