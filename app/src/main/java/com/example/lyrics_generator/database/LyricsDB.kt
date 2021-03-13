package com.example.lyrics_generator.database

import android.content.Context
import android.util.Log
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.lyrics_generator.R
import java.io.BufferedInputStream
import java.io.BufferedReader
import java.io.InputStream
import java.util.concurrent.Executors

@Database (entities = [Lyrics::class], version = 3, exportSchema = false)
abstract class LyricsDB: RoomDatabase() {
    abstract val lyricsDao: LyricsDao

    companion object {
        @Volatile
        private var INSTANCE: LyricsDB? = null
        fun getInstance(context: Context): LyricsDB {
            synchronized(this) {
                var instance = INSTANCE
                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context, LyricsDB::class.java,
                        "lyrics_database"
                    )
                        .fallbackToDestructiveMigration()
                        .build()
                }
                return instance
            }
        }
    }
}