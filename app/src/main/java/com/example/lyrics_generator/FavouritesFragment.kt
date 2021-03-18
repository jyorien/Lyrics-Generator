package com.example.lyrics_generator

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.lyrics_generator.database.Lyrics
import com.example.lyrics_generator.database.LyricsDB
import com.example.lyrics_generator.database.LyricsDao
import com.example.lyrics_generator.databinding.FragmentFavouritesBinding
import kotlinx.coroutines.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [FavouritesFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class FavouritesFragment : Fragment() {
    private lateinit var binding: FragmentFavouritesBinding
    private lateinit var li: List<Lyrics>
    private lateinit var lyricsDao: LyricsDao

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFavouritesBinding.inflate(layoutInflater)
        lyricsDao = LyricsDB.getInstance(requireActivity()).lyricsDao

        runBlocking {
            withContext(Dispatchers.IO) {
                li = lyricsDao.getFavourites()
            }
        }
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val adapter = FavListAdapter(li, lyricsDao)
        binding.favList.adapter = adapter
        adapter.notifyDataSetChanged()

    }
}