package com.example.calldefender.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import io.reactivex.Completable
import io.reactivex.Single

@Dao
interface SettingEntityDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(settingEntity: SettingEntity): Completable

    @Query("SELECT * FROM settingentity")
    fun getAll(): Single<List<SettingEntity>>

    @Query("SELECT * FROM settingentity WHERE id LIKE :type")
    fun findByType(type: Int): Single<SettingEntity?>
}