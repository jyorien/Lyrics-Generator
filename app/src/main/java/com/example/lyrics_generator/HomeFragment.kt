package com.example.lyrics_generator

import android.content.Context
import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.lyrics_generator.database.LyricsDB
import com.example.lyrics_generator.database.LyricsDao
import com.example.lyrics_generator.databinding.FragmentHomeBinding
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.lang.NullPointerException
import kotlin.random.Random


class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding
    // for graphical declarations, gets called after oncreate activity
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(layoutInflater)
        // set lifecycleowner for livedata to define scope of livedata object
        binding.lifecycleOwner = this

        val lyricsDao = LyricsDB.getInstance(requireActivity()).lyricsDao
        val viewModelFactory = HomeFragmentViewModelFactory(lyricsDao)
        // let activity be viewmodel owner
        val viewModel = ViewModelProvider(requireActivity(), viewModelFactory).get(HomeFragmentViewModel::class.java)
        binding.apply {
            homeFragmentViewModel = viewModel
            currentLyrics.movementMethod = ScrollingMovementMethod()

            btnRestart.setOnClickListener {
                GlobalScope.launch {
                    lyricsDao.deleteAllLyrics()
                    activity?.finish()
                }
            }

        }
        return binding.root
    }







}