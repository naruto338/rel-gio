package com.example.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class KnownDevice(
    @PrimaryKey val address: String,
    val name: String?,
    val lastSeen: Long
)

@Entity
data class BleServiceEntity(
    @PrimaryKey val id: String,
    val deviceAddress: String,
    val uuid: String
)

@Entity
data class BleCharacteristicEntity(
    @PrimaryKey val id: String,
    val deviceAddress: String,
    val serviceUuid: String,
    val charUuid: String,
    val properties: Int
)
