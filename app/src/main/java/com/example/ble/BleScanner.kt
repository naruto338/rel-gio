package com.example.ble

import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.le.*
import android.content.Context
import android.util.Log

class BleScanner(
    private val context: Context,
    private val onDeviceFound: (address: String, name: String?, rssi: Int) -> Unit
) {
    private val bluetoothAdapter: BluetoothAdapter? =
        (context.getSystemService(Context.BLUETOOTH_SERVICE) as? android.bluetooth.BluetoothManager)?.adapter
    private val scanner: BluetoothLeScanner? = bluetoothAdapter?.bluetoothLeScanner

    private val scanCallback = object : ScanCallback() {
        override fun onScanResult(callbackType: Int, result: ScanResult) {
            super.onScanResult(callbackType, result)
            val device: BluetoothDevice = result.device
            val rssi = result.rssi
            Log.i("BleScanner", "Found ${device.address} name=${device.name} rssi=$rssi")
            onDeviceFound(device.address, device.name, rssi)
        }

        override fun onBatchScanResults(results: MutableList<ScanResult>) {
            results.forEach { onScanResult(ScanSettings.CALLBACK_TYPE_ALL_MATCHES, it) }
        }

        override fun onScanFailed(errorCode: Int) {
            Log.e("BleScanner", "Scan failed: $errorCode")
        }
    }

    @SuppressLint("MissingPermission")
    fun startScan() {
        try {
            val settings = ScanSettings.Builder()
                .setScanMode(ScanSettings.SCAN_MODE_LOW_LATENCY)
                .build()
            val filters = listOf<ScanFilter>()
            scanner?.startScan(filters, settings, scanCallback)
            Log.i("BleScanner", "Scan started")
        } catch (e: Exception) {
            Log.e("BleScanner", "startScan error: ${e.message}")
        }
    }

    @SuppressLint("MissingPermission")
    fun stopScan() {
        try {
            scanner?.stopScan(scanCallback)
            Log.i("BleScanner", "Scan stopped")
        } catch (e: Exception) {
            Log.e("BleScanner", "stopScan error: ${e.message}")
        }
    }
}
