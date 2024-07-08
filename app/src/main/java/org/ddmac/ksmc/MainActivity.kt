package org.ddmac.ksmc

//import android.media.MediaPlayer
import android.app.ActivityManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.OptIn
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.media3.common.util.UnstableApi
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.ddmac.ksmc.ui.theme.KsmcTheme
import org.ddmac.ksmc.ui.theme.Purple40

class MainActivity : ComponentActivity() {

//    val channelId = "ksmc channel id"
//    val channelName = "ksmc channel"
//    private val url = "http://192.168.1.71:5000/p"
////    lateinit var mediaPlayer: MediaPlayer
//    val vm: MainViewModel by viewModels()
//    val mediaItem = MediaItem.Builder()
//        .setMediaId("ksmc")
//        .setUri(url)
//        .setMediaMetadata(
//            MediaMetadata.Builder()
//                .setTitle("KSMC").build()
//        )
//        .build()


//    var mediaController: MediaController? = null

    @OptIn(UnstableApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if(!isPlaybackServiceRunning()){
            Log.d(MainActivity::class.simpleName,"Launching service.")

            CoroutineScope(Dispatchers.Default).launch {
                this@MainActivity.startForegroundService(
                    Intent(
                        this@MainActivity,
                        PlaybackService::class.java
                    )
                )
            }
        }

        enableEdgeToEdge()
        setContent {
            KsmcTheme {
//                val selected by vm.selected.collectAsStateWithLifecycle()
//                LaunchedEffect(key1 = selected, key2 = mediaController) {
//                    if (mediaController != null) {
//                        when (selected) {
//                            1 -> {
//                                Log.d("MediaController", "play")
//                                mediaController!!.setMediaItem(mediaItem)
//                                mediaController!!.prepare()
//                                mediaController!!.repeatMode = MediaController.REPEAT_MODE_ONE
//                                mediaController!!.play()
//                            }
//
//                            else -> {
//                                mediaController!!.pause()
//                            }
//                        }
//                    }
//                }
                Card(
                    modifier = Modifier.fillMaxSize(),
                    colors = CardColors(Purple40, Color.White, Color.Gray, Color.DarkGray)
                ) {
                    Column(
                        Modifier
                            .fillMaxWidth()
                            .wrapContentSize()
                    ) {
                        Text(
                            modifier = Modifier.padding(24.dp),
                            text = "KSMC"
                        )
                    }
                }
            }
        }
    }

    @OptIn(UnstableApi::class)
    override fun onStart() {
        super.onStart()
//        val sessionToken = SessionToken(
//                this,
//                ComponentName(this@MainActivity,PlaybackService::class.java)
//            )
//
//        MediaController.Builder(this,sessionToken).buildAsync().apply {
//            addListener(
//                {
//                    mediaController = this.get()
//                },
//                ContextCompat.getMainExecutor(this@MainActivity)
//            )
//        }
    }

    override fun onStop() {
        super.onStop()
//        mediaController!!.release()
    }

    @OptIn(UnstableApi::class)
    @Suppress("deprecation")
    private fun isPlaybackServiceRunning(): Boolean{
        val manager = getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        manager.getRunningServices(Integer.MAX_VALUE).forEach { runningServiceInfo ->
            if (runningServiceInfo.service::class == PlaybackService::class){
                return true
            }
        }
        return false
    }





//    private fun playMedia() {
//        MediaPlayer().apply {
//            setAudioAttributes(
//                AudioAttributes.Builder()
//                    .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
//                    .setUsage(AudioAttributes.USAGE_MEDIA)
//                    .build()
//            )
//            setDataSource(url)
//            prepare()
//            start()
//        }
//    }
}