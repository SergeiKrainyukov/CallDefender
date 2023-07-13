package com.example.calldefender.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import io.reactivex.Completable
import io.reactivex.Maybe
import io.reactivex.Single

@Dao
interface CallEntityDao {

    @Insert
    fun insert(callEntity: CallEntity): Completable

    @Query("SELECT * FROM callentity")
    fun getAll(): Maybe<List<CallEntity>>

    @Query("SELECT * FROM callentity WHERE call_number LIKE :callNumber")
    fun findByCallNumber(callNumber: String): Single<CallEntity>
}