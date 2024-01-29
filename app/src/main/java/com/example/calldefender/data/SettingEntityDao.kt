package com.example.calldefender.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface SettingEntityDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(settingEntity: SettingEntity)

    @Query("SELECT * FROM settingentity")
    suspend fun getAll(): List<SettingEntity>

    @Query("SELECT * FROM settingentity WHERE id LIKE :type")
    suspend fun findByType(type: Int): SettingEntity?
}