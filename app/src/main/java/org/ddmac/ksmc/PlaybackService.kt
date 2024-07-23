package org.ddmac.ksmc

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.session.CommandButton
import androidx.media3.session.MediaController
import androidx.media3.session.MediaNotification
import androidx.media3.session.MediaSession
import androidx.media3.session.MediaSessionService
import androidx.media3.session.MediaStyleNotificationHelper
import com.google.common.collect.ImmutableList
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@UnstableApi
class PlaybackService : MediaSessionService() {

    lateinit var mediaSession: MediaSession
    lateinit var player: Player


    private val channelId = "ksmc channel id"
    private val channelName = "ksmc channel"

    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()
        Log.d(PlaybackService::class.simpleName, "HELLO, SERVICE!")
        player = ExoPlayer.Builder(this).build()
        mediaSession = MediaSession.Builder(this, player).build()
        this.setMediaNotificationProvider(provider)
        startForeground(8378, createNotification(mediaSession))

        CoroutineScope(Dispatchers.Default).launch {
            var lastSelected = 0
            SelectedFlow("http://192.168.0.223:5000/getSelected").selected.catch {
                Log.d(SelectedFlow::class.simpleName, "Error in communication", it)
            }.collect {
                Log.d(PlaybackService::class.simpleName, "SELECTED: $it")
                if (it != lastSelected) {
                    lastSelected = it
                    withContext(Dispatchers.Main) {
                        mediaSession.player.apply {
                            stop()
                            setMediaItem(getMediaItem(it.toString()))
                            repeatMode = MediaController.REPEAT_MODE_ONE
                            prepare()
                            play()
                        }
                    }
                }
            }
        }
    }


    private fun getMediaItem(mediaId: String) =
        MediaItem.Builder()
            .setUri("http://192.168.0.223:5000/p")
            .setMediaMetadata(
                MediaMetadata.Builder()
                    .setTitle("KSMC").build()
            )
            .setMediaId(mediaId)
            .build()


    override fun onDestroy() {
        super.onDestroy()
        mediaSession.run {
            player.release()
            release()
        }
        super.onDestroy()
    }

    override fun onGetSession(controllerInfo: MediaSession.ControllerInfo): MediaSession =
        mediaSession.also { Log.d(PlaybackService::class.simpleName, "Getting Session") }

    val provider = object : MediaNotification.Provider {
        @SuppressLint("NotificationPermission")
        override fun createNotification(
            mediaSession: MediaSession,
            customLayout: ImmutableList<CommandButton>,
            actionFactory: MediaNotification.ActionFactory,
            onNotificationChangedCallback: MediaNotification.Provider.Callback
        ): MediaNotification {
            return MediaNotification(8378, createNotification(mediaSession))
        }

        override fun handleCustomCommand(
            session: MediaSession,
            action: String,
            extras: Bundle
        ): Boolean {
            return true
        }

    }

    private fun createNotificationChannel() {
        val serviceChannel = NotificationChannel(
            channelId,
            channelName,
            NotificationManager.IMPORTANCE_LOW
        )
        val manager = getSystemService(NotificationManager::class.java)
        manager?.createNotificationChannel(serviceChannel)
    }

    fun createNotification(mediaSession: MediaSession) =
        NotificationCompat.Builder(this@PlaybackService, channelId)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle("")
            .setContentText("")
            .setStyle(MediaStyleNotificationHelper.MediaStyle(mediaSession))
            .build()

}

