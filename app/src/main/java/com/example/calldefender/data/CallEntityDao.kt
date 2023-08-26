package com.example.calldefender.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import io.reactivex.Completable
import io.reactivex.Single

@Dao
interface CallEntityDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(callEntity: CallEntity): Completable

    @Query("SELECT * FROM callentity ORDER BY date DESC")
    fun getAll(): Single<List<CallEntity>>

    @Query("SELECT * FROM callentity WHERE callNumber LIKE :callNumber")
    fun findByCallNumber(callNumber: String): Single<CallEntity>
}