package com.example.mypersonalassistant.di

import android.content.Context
import androidx.room.Room
import com.example.mypersonalassistant.data.local.MpaDatabase
import com.example.mypersonalassistant.data.local.dao.NoteDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Singleton
    @Provides
    fun provideDatabase(@ApplicationContext context: Context): MpaDatabase = Room.databaseBuilder(
        context.applicationContext, MpaDatabase::class.java, "MyPersonalAssistant"
    ).fallbackToDestructiveMigration().build()

    @Provides
    fun provideNoteDao(database: MpaDatabase): NoteDao = database.noteDao()
}