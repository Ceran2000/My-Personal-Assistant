package com.example.mypersonalassistant.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.mypersonalassistant.data.local.dao.NoteDao
import com.example.mypersonalassistant.data.local.entities.LocalNote

@Database(entities = [LocalNote::class], version = 3, exportSchema = false)
abstract class MpaDatabase : RoomDatabase() {

    abstract fun noteDao(): NoteDao

}