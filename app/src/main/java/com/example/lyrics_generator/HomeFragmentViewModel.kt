package com.example.lyrics_generator

import android.util.Log
import androidx.lifecycle.*
import com.example.lyrics_generator.database.Lyrics
import com.example.lyrics_generator.database.LyricsDao
import kotlinx.coroutines.*
import java.lang.NullPointerException
import kotlin.properties.Delegates
import kotlin.random.Random

class HomeFragmentViewModel(val database: LyricsDao): ViewModel() {

    private val _lyrics = MutableLiveData<String>()
    val lyrics: LiveData<String>
            get() = _lyrics

    private val _favStateString = MutableLiveData<String>()
    val favStateString: LiveData<String>
            get() = _favStateString

    private lateinit var lyricsObject: Lyrics
    private var favState by Delegates.notNull<Boolean>()

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
                    favState = database.getSingleLyrics(lyricsObject.lyricsId).isFavourite
                }
                else
                    displayNextLyrics()
                    //newLyrics = "Press random to start!"
            }
            withContext(Dispatchers.Main) {
                // update UI on MAIN THREAD
                _lyrics.value = newLyrics
                setFavString(favState)
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

                favState = database.getSingleLyrics(lyricsObject.lyricsId).isFavourite
            }
            withContext(Dispatchers.Main) {
                setFavString(favState)
            }
        }

    }

    private fun setFavString(state: Boolean) {
        if (!state)
            _favStateString.value = "Favourite"
        else
            _favStateString.value = "Unfavourite"
    }

}