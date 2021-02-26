package com.example.lyrics_generator

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import androidx.annotation.UiThread
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import com.example.lyrics_generator.database.Lyrics
import com.example.lyrics_generator.database.LyricsDB
import com.example.lyrics_generator.database.LyricsDao
import com.example.lyrics_generator.databinding.ActivityLoadingBinding
import kotlinx.coroutines.*
import java.io.BufferedReader
import java.io.InputStream

val MAX: Int = 83085

class LoadingActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoadingBinding
    private lateinit var lyricsDao: LyricsDao
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_loading)
        binding.progressBar.max = MAX
        lyricsDao = LyricsDB.getInstance(this).lyricsDao
        GlobalScope.launch {
            lyricsDao.deleteAllLyrics()
            loadDatabase(this@LoadingActivity)
            val intent: Intent = Intent(this@LoadingActivity, MainActivity::class.java)
            finish()
            startActivity(intent)
        }

    }

    private fun loadDatabase(context: Context) {
        val inputStream: InputStream = context.resources.openRawResource(R.raw.lyrics)
        val reader: BufferedReader = inputStream.bufferedReader()
        val iterator = reader.lineSequence().iterator()
        iterator.next()
        val lyricsList = mutableListOf<Lyrics>()
        var counter: Long = 1
        GlobalScope.launch {
            while (counter.toInt() != MAX) {
                delay(1500L)
                binding.progressBar.progress = counter.toInt()
            }
        }

        while(iterator.hasNext()) {
            val line = iterator.next()
            val lineList = line.split(",")
            val lyrics = Lyrics(counter,lineList[0],lineList[1])
            lyricsDao.insertLyrics(lyrics)
            lyricsList.add(lyrics)
            counter+=1
        }
        reader.close()

    }

}