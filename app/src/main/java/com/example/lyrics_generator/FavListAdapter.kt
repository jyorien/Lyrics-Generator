package com.example.lyrics_generator

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.lyrics_generator.database.Lyrics
import com.example.lyrics_generator.database.LyricsDB
import com.example.lyrics_generator.database.LyricsDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class FavListAdapter(private var lyricsList: List<Lyrics>, val lyricsDao: LyricsDao): RecyclerView.Adapter<FavListAdapter.SongViewHolder>() {

    class SongViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

        val songArtiste: TextView = itemView.findViewById(R.id.songArtiste)
        val songLyrics: TextView = itemView.findViewById(R.id.songLyrics)
        val btnFav: ImageButton = itemView.findViewById(R.id.btnFav)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SongViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = layoutInflater.inflate(R.layout.song_list_item, parent, false)
        return SongViewHolder(view)
    }

    override fun onBindViewHolder(holder: SongViewHolder, position: Int) {
        holder.songArtiste.text = lyricsList[position].artiste
        holder.songLyrics.text = lyricsList[position].lyrics
        holder.btnFav.setOnClickListener {
            removeSong(lyricsList[position].lyricsId)
        }

    }

    override fun getItemCount(): Int = lyricsList.size

    private fun removeSong(index: Long) {
        GlobalScope.launch {
            withContext(Dispatchers.IO) {
                lyricsDao.deleteFavourite(index)
                lyricsList = lyricsDao.getFavourites()
            }
            withContext(Dispatchers.Main) {
                notifyDataSetChanged()
            }
        }

    }

}