package org.ddmac.ksmc

import com.google.gson.Gson
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.ResponseBody

class SelectedFlow(url: String) {

    val selected: Flow<Int> = flow {
        while(true){
            val latestSelected = OkHttpClient()
                .newCall(
                    Request
                        .Builder()
                        .url(url)
                        .build()
                ).execute()
            if(latestSelected.body != null){
                emit(
                    Gson().
                    fromJson(
                        latestSelected.body!!.string(),
                        Selected::class.java)
                        .selected
                )
            }
            delay(5000)
        }
    }
}