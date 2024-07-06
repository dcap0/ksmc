package org.ddmac.ksmc

import android.media.AudioAttributes
import android.media.MediaPlayer
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.Text
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.ddmac.ksmc.ui.theme.KsmcTheme
import org.ddmac.ksmc.ui.theme.Purple40

class MainActivity : ComponentActivity() {

    private val url = "http://192.168.1.71:5000/p"
    lateinit var mediaPlayer: MediaPlayer
    val vm: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            KsmcTheme {
                val selected by vm.selected.collectAsStateWithLifecycle()
                LaunchedEffect(selected) {
                    playMedia()
                }
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



    private fun playMedia() {
        MediaPlayer().apply {
            setAudioAttributes(
                AudioAttributes.Builder()
                    .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                    .setUsage(AudioAttributes.USAGE_MEDIA)
                    .build()
            )
            setDataSource(url)
            prepare()
            start()
        }
    }
}