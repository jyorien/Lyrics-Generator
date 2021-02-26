package com.example.lyrics_generator

import android.content.Context
import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.lyrics_generator.database.LyricsDB
import com.example.lyrics_generator.database.LyricsDao
import com.example.lyrics_generator.databinding.FragmentHomeBinding
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.lang.NullPointerException
import kotlin.random.Random


class HomeFragment : Fragment() {
    private lateinit var lyricsDao: LyricsDao
    private lateinit var binding: FragmentHomeBinding
    // for graphical declarations, gets called after oncreate activity
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(layoutInflater)
        lyricsDao = LyricsDB.getInstance(requireActivity()).lyricsDao
        binding.apply {
            GlobalScope.launch {
                displayNextLyrics()
            }
            currentLyrics.movementMethod = ScrollingMovementMethod()

            btnRandom.setOnClickListener {
                displayNextLyrics()
            }

            btnRestart.setOnClickListener {
                GlobalScope.launch {
                    lyricsDao.deleteAllLyrics()
                    activity?.finish()
                }
            }



        }
        return binding.root
    }

    private fun displayNextLyrics() {
        GlobalScope.launch {
            val randomNum = Random.nextLong(1,MAX+1.toLong())
            try {
                binding.currentLyrics.text = lyricsDao.getSingleLyrics(randomNum).lyrics
            }
            catch (e: NullPointerException) {
                displayNextLyrics()
            }
        }
    }




}