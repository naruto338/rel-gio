package com.example.repository

import android.content.Context
import com.example.db.AppDatabase
import com.example.db.BleCharacteristicEntity
import com.example.db.BleServiceEntity
import com.example.db.KnownDevice

class DeviceRepository(context: Context) {
    private val db = AppDatabase.get(context)
    private val deviceDao = db.deviceDao()
    private val serviceDao = db.serviceDao()
    private val charDao = db.charDao()

    suspend fun saveDevice(address: String, name: String, lastSeen: Long) {
        deviceDao.insertDevice(KnownDevice(address, name, lastSeen))
    }

    suspend fun saveService(deviceAddress: String, uuid: String) {
        val id = "$deviceAddress|$uuid"
        serviceDao.insertService(BleServiceEntity(id, deviceAddress, uuid))
    }

    suspend fun saveCharacteristic(deviceAddress: String, serviceUuid: String, charUuid: String, properties: Int) {
        val id = "$deviceAddress|$serviceUuid|$charUuid"
        charDao.insertChar(BleCharacteristicEntity(id, deviceAddress, serviceUuid, charUuid, properties))
    }
}
