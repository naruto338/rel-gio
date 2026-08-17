package com.example.notification

import android.app.PendingIntent
import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification
import android.util.Log
import androidx.core.app.RemoteInput

class ReLogioNotificationListener : NotificationListenerService() {
    override fun onNotificationPosted(sbn: StatusBarNotification) {
        val pkg = sbn.packageName
        val notif = sbn.notification
        val extras = notif.extras
        val title = extras.getString("android.title")
        val text = extras.getCharSequence("android.text")?.toString()
        Log.i("NotifListener", "Pkg=$pkg Title=$title Text=$text")

        val actions = notif.actions
        actions?.forEach { action ->
            val remoteInputs = action.remoteInputs
            if (!remoteInputs.isNullOrEmpty()) {
                Log.i("NotifListener", "Found reply Action for pkg=$pkg")
            }
        }
    }

    override fun onNotificationRemoved(sbn: StatusBarNotification) {
    }

    private fun trySendReply(pendingIntent: PendingIntent, inputKey: String, text: String) {
        try {
            val intent = android.content.Intent()
            val bundle = android.os.Bundle()
            bundle.putCharSequence(inputKey, text)
            RemoteInput.addResultsToIntent(
                arrayOf(RemoteInput.Builder(inputKey).build()), intent, bundle
            )
            pendingIntent.send(this, 0, intent)
        } catch (e: Exception) {
            Log.e("NotifListener", "reply failed: ${e.message}")
        }
    }
}
