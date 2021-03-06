package com.example.lyrics_generator.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface LyricsDao {
    @Insert
    fun insertLyrics(lyrics: Lyrics)

    @Insert
    fun insertAllLyrics(lyrics: List<Lyrics>)

    @Query(value = "SELECT * FROM lyrics_table WHERE lyricsId = :key")
    fun getSingleLyrics(key: Long): Lyrics

    @Query(value = "SELECT count(*) FROM lyrics_table LIMIT 1")
    fun checkDB(): Int

    @Query(value = "DELETE FROM lyrics_table")
    fun deleteAllLyrics()

    @Query(value = "UPDATE lyrics_table SET isFavourite = 1 WHERE lyricsId = :key")
    fun addFavourite(key: Long)

    @Query(value = "UPDATE lyrics_table SET isFavourite = 0 WHERE lyricsId = :key")
    fun deleteFavourite(key: Long)

    @Query(value = "SELECT * FROM lyrics_table  WHERE isFavourite = 1")
    fun getFavourites(): List<Lyrics>

}