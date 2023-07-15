package com.example.calldefender.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import io.reactivex.Completable
import io.reactivex.Single

@Dao
interface CallEntityDao {

    @Insert
    fun insert(callEntity: CallEntity): Completable

    @Query("SELECT * FROM callentity")
    fun getAll(): Single<List<CallEntity>>

    @Query("SELECT * FROM callentity WHERE callNumber LIKE :callNumber")
    fun findByCallNumber(callNumber: String): Single<CallEntity>
}