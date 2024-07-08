package org.ddmac.ksmc

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class MainViewModel: ViewModel() {

    private var _selected = MutableStateFlow(0)
    val selected: MutableStateFlow<Int> get() = _selected

    val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        Log.e(
            MainViewModel::class.simpleName,
            "Exception occurred in server communication",
            throwable
        )

    }

    init {
        viewModelScope.launch {
            CoroutineScope(Dispatchers.Default).launch(exceptionHandler) {
                SelectedFlow("http://192.168.1.71:5000/getSelected").selected.catch {
                    Log.d(SelectedFlow::class.simpleName,"Error in communication",it)
                }.collect {
                    _selected.value = it
                    Log.d(MainViewModel::class.simpleName, "SELECTED: $it")
                }
            }
        }
    }

    enum class PState{
        PAUSE,
        PLAY
    }
}