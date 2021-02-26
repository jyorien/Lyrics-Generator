package com.example.lyrics_generator

import android.content.Context
import android.content.Intent
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import com.example.lyrics_generator.database.Lyrics
import com.example.lyrics_generator.database.LyricsDB
import com.example.lyrics_generator.database.LyricsDao
import com.example.lyrics_generator.databinding.ActivityMainBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.io.BufferedReader
import java.io.InputStream

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController
    private lateinit var bottomNavigationView: BottomNavigationView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        GlobalScope.launch { checkDB(this@MainActivity) }

    }

    private fun checkDB(context: Context) {
        val lyricsDB = LyricsDB.getInstance(context)
        if (lyricsDB.lyricsDao.checkDB() != MAX) {
            val intent = Intent(this,LoadingActivity::class.java)
            finish()
            startActivity(intent)
        }
        else {
            navController = findNavController(R.id.host_fragment)
            bottomNavigationView = binding.bottomNav
            NavigationUI.setupWithNavController(bottomNavigationView, navController)
        }
    }


}
