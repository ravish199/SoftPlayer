package com.ravish.softplayer.ui.notification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.util.Log
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.ui.PlayerNotificationManager
import com.ravish.softplayer.R
import com.ravish.softplayer.ui.MainActivity

private const val NOTIFICATION_ID = 2001
private const val NOTIFICATION_CHANNEL_ID = "soft_player_music_channel"
private const val NOTIFICATION_CHANNEL_NAME = "Soft Player Music"

@UnstableApi // Media3 UI components are often marked as UnstableApi
class MediaNotificationManager(
    private val context: Context,
    private val player: Player,
    notificationListener: PlayerNotificationManager.NotificationListener
) {

    private val notificationManager: NotificationManager =
        context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    private var playerNotificationManager: PlayerNotificationManager


    init {
        // Create the notification channel
        createNotificationChannel()

        // Build the PlayerNotificationManager
        playerNotificationManager = PlayerNotificationManager.Builder(
            context,
            NOTIFICATION_ID,
            NOTIFICATION_CHANNEL_ID
        )
            .setChannelNameResourceId(R.string.notification_channel_name)
            .setChannelDescriptionResourceId(R.string.notification_channel_desc)
            .setNotificationListener(notificationListener)
            .setMediaDescriptionAdapter(DescriptionAdapter())
            .build().apply {
                Log.d("MediaNotificationManager:", "build")
                setPlayer(player)
                setUsePlayPauseActions(true)
                setUseNextActionInCompactView(true)
                setUsePreviousActionInCompactView(true)
            }
    }

    private inner class DescriptionAdapter : PlayerNotificationManager.MediaDescriptionAdapter {
        override fun getCurrentContentTitle(player: Player): CharSequence {
            return player.mediaMetadata.title ?: "Unknown"
        }

        override fun createCurrentContentIntent(player: Player): PendingIntent? {
            val intent = Intent(context, MainActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_CLEAR_TOP
            }
            return PendingIntent.getActivity(
                context,
                0,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )
        }

        override fun getCurrentContentText(player: Player): CharSequence? {
            return player.mediaMetadata.artist
        }

        override fun getCurrentLargeIcon(
            player: Player,
            callback: PlayerNotificationManager.BitmapCallback
        ): Bitmap? {
            return null
        }
    }

    fun showNotification() {
        Log.d("MediaNotificationManager:", "showNotification")
        playerNotificationManager.invalidate()
    }

    fun hideNotification() {
        Log.d("MediaNotificationManager:", "hideNotification")
        playerNotificationManager.setPlayer(null)
    }

    private fun createNotificationChannel() {
        val channel = NotificationChannel(
            NOTIFICATION_CHANNEL_ID,
            NOTIFICATION_CHANNEL_NAME,
            NotificationManager.IMPORTANCE_LOW
        ).apply {
            description = "Notification for music playback"
            setSound(null, null)
        }
        Log.d("MediaNotificationManager:", "createNotificationChannel")
        notificationManager.createNotificationChannel(channel)
    }
}
