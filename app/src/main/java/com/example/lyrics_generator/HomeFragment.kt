package com.example.lyrics_generator

import android.content.Context
import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.lyrics_generator.databinding.FragmentHomeBinding


class HomeFragment : Fragment() {
    private val lyricsList = arrayListOf<String>("Hello, it's me", "I was wondering if after all these years you'd like to meet", "To go over everything","Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, qu")
    private lateinit var binding: FragmentHomeBinding
    private var lyricsListCounter: Int = 0
    // for graphical declarations, gets called after oncreate activity
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(layoutInflater)
        binding.apply {
            currentLyrics.text = lyricsList[0]
            currentLyrics.movementMethod = ScrollingMovementMethod()
            btnRandom.setOnClickListener {
                displayNextLyrics()
            }

        }
        return binding.root
    }

    fun displayNextLyrics() {
        binding.apply {
            if (lyricsListCounter == lyricsList.size-1) {
                currentLyrics.text = lyricsList[0]
                lyricsListCounter = 0
            }
            else {
                currentLyrics.text = lyricsList[lyricsListCounter+1]
                lyricsListCounter+=1
            }
        }
    }




}