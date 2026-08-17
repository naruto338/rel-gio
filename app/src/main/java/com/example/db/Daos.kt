package com.example.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface DeviceDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDevice(device: KnownDevice)

    @Query("SELECT * FROM KnownDevice WHERE address = :address LIMIT 1")
    suspend fun getDevice(address: String): KnownDevice?
}

@Dao
interface ServiceDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertService(service: BleServiceEntity)
}

@Dao
interface CharacteristicDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertChar(c: BleCharacteristicEntity)
}
