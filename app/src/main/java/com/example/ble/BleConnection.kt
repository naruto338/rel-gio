package com.example.ble

import android.annotation.SuppressLint
import android.bluetooth.*
import android.content.Context
import android.util.Log
import java.util.*

class BleConnection(private val context: Context, private val device: BluetoothDevice) {
    private var gatt: BluetoothGatt? = null

    fun connect(autoConnect: Boolean = false) {
        gatt = device.connectGatt(context, autoConnect, gattCallback)
    }

    fun disconnect() {
        try {
            gatt?.disconnect()
            gatt?.close()
            gatt = null
        } catch (e: Exception) {
            Log.e("BleConnection", "disconnect error: ${e.message}")
        }
    }

    private val gattCallback = object : BluetoothGattCallback() {
        override fun onConnectionStateChange(g: BluetoothGatt, status: Int, newState: Int) {
            super.onConnectionStateChange(g, status, newState)
            if (newState == BluetoothProfile.STATE_CONNECTED) {
                Log.i("BleConnection", "Connected, discovering services...")
                g.discoverServices()
            } else if (newState == BluetoothProfile.STATE_DISCONNECTED) {
                Log.i("BleConnection", "Disconnected")
            }
        }

        override fun onServicesDiscovered(g: BluetoothGatt, status: Int) {
            super.onServicesDiscovered(g, status)
            val services = g.services
            services.forEach { service ->
                Log.i("BleConnection", "Service ${service.uuid}")
                service.characteristics.forEach { c ->
                    Log.i("BleConnection", "  Char ${c.uuid} props=${c.properties}")
                }
            }
        }

        override fun onCharacteristicRead(g: BluetoothGatt, characteristic: BluetoothGattCharacteristic, status: Int) {
            if (status == BluetoothGatt.GATT_SUCCESS) {
                val value = characteristic.value
                Log.i("BleConnection", "Read ${characteristic.uuid} = ${value?.size ?: 0} bytes")
            }
        }

        override fun onCharacteristicChanged(g: BluetoothGatt, characteristic: BluetoothGattCharacteristic) {
            val value = characteristic.value
            Log.i("BleConnection", "Notify ${characteristic.uuid} bytes=${value?.size ?: 0}")
        }
    }

    fun characteristicPropertiesToSet(props: Int): Set<String> {
        val set = mutableSetOf<String>()
        if (props and BluetoothGattCharacteristic.PROPERTY_READ != 0) set.add("READ")
        if (props and BluetoothGattCharacteristic.PROPERTY_WRITE != 0) set.add("WRITE")
        if (props and BluetoothGattCharacteristic.PROPERTY_WRITE_NO_RESPONSE != 0) set.add("WRITE_NO_RESPONSE")
        if (props and BluetoothGattCharacteristic.PROPERTY_NOTIFY != 0) set.add("NOTIFY")
        if (props and BluetoothGattCharacteristic.PROPERTY_INDICATE != 0) set.add("INDICATE")
        return set
    }

    @SuppressLint("MissingPermission")
    fun enableNotifications(characteristic: BluetoothGattCharacteristic): Boolean {
        gatt ?: return false
        val res = gatt!!.setCharacteristicNotification(characteristic, true)
        val cccdUuid = UUID.fromString("00002902-0000-1000-8000-00805f9b34fb")
        val cccd = characteristic.getDescriptor(cccdUuid)
        if (cccd != null) {
            cccd.value = BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE
            return gatt!!.writeDescriptor(cccd)
        }
        return res
    }
}
