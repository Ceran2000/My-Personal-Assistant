package com.example.mypersonalassistant.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.example.mypersonalassistant.data.local.entities.LocalNote
import kotlinx.coroutines.flow.Flow

@Dao
interface NoteDao {

    @Query("SELECT * FROM note")
    fun getAll(): Flow<List<LocalNote>>

    @Query("SELECT * FROM note WHERE addedAtMillis = (SELECT MAX(addedAtMillis) FROM note)")
    fun getLatest(): Flow<LocalNote?>

    @Query("SELECT * FROM note WHERE id LIKE :id")
    suspend fun findById(id: String): LocalNote?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(note: LocalNote)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(notes: List<LocalNote>)

    @Delete
    suspend fun delete(note: LocalNote)

    @Query("DELETE FROM note")
    suspend fun clearAll()

    @Transaction
    suspend fun syncAll(notes: List<LocalNote>) {
        clearAll()
        insertAll(notes)
    }

}