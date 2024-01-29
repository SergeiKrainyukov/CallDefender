package com.example.calldefender.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface CallEntityDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(callEntity: CallEntity)

    @Query("SELECT * FROM callentity ORDER BY date DESC")
    suspend fun getAll(): List<CallEntity>

    @Query("SELECT * FROM callentity WHERE callNumber LIKE :callNumber")
    suspend fun findByCallNumber(callNumber: String): CallEntity
}