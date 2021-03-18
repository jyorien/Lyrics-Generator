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

    private val _favState = MutableLiveData<Boolean>()
    val favState: LiveData<Boolean>
        get() = _favState

    init {
        displayNextLyrics()
    }

    fun displayNextLyrics() {
        val randomNum = Random.nextLong(1,MAX+1.toLong())

        // launch coroutine in viewModelScope so that it cancels with viewmodel
        viewModelScope.launch {
            lateinit var newLyrics: String
            var newState by Delegates.notNull<Boolean>()
            withContext(Dispatchers.IO) {
                // call database from BACKGROUND THREAD
                if (database.checkDB() > 0) {
                    lyricsObject = database.getSingleLyrics(randomNum)
                    newLyrics = lyricsObject.lyrics
                    newState = database.getSingleLyrics(lyricsObject.lyricsId).isFavourite

                }
                else
                    displayNextLyrics()
                    //newLyrics = "Press random to start!"
            }
            withContext(Dispatchers.Main) {
                // update UI on MAIN THREAD
                _lyrics.value = newLyrics
                _favState.value = newState
                setFavString(_favState.value!!)
            }
        }
    }

    fun favourite() {
        viewModelScope.launch {
            var newState by Delegates.notNull<Boolean>()
            withContext(Dispatchers.IO) {
                if (lyricsObject.isFavourite) {
                    database.deleteFavourite(lyricsObject.lyricsId)
                }
                else {
                    database.addFavourite(lyricsObject.lyricsId)
                }
                newState = database.getSingleLyrics(lyricsObject.lyricsId).isFavourite

            }
            withContext(Dispatchers.Main) {
                _favState.value = newState
                setFavString(_favState.value!!)
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