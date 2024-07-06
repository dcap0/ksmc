package org.ddmac.ksmc

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class MainViewModel: ViewModel() {

    private var _selected = MutableStateFlow(0)
    val selected: MutableStateFlow<Int> get() = _selected

    init {
        viewModelScope.launch {
            CoroutineScope(Dispatchers.Default).launch {
                SelectedFlow("http://192.168.1.71:5000/getSelected").selected.collect {
                    _selected.value = it
                    Log.d(MainViewModel::class.simpleName, "SELECTED: $it")
                }
            }
        }
    }
}