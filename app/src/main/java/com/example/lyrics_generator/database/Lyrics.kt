package com.example.lyrics_generator.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity (tableName = "lyrics_table")
data class Lyrics (
        @PrimaryKey (autoGenerate = true)
        val lyricsId: Long,
        @ColumnInfo (name = "lyrics")
        val lyrics: String,
        @ColumnInfo (name = "artiste")
        val artiste: String)