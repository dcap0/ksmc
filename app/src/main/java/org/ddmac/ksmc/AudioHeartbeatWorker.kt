package org.ddmac.ksmc

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AudioHeartbeatWorker(ctx: Context, params: WorkerParameters):  CoroutineWorker(ctx,params) {
    override suspend fun doWork(): Result {
        CoroutineScope(Dispatchers.Default).launch {
            SelectedFlow("http://192.168.1.71:5000/getSelected").selected.collect {
                1
            }
        }
        return Result.success()
    }
}