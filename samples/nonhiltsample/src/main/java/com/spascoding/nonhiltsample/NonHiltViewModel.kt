package com.spascoding.nonhiltsample

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.spascoding.configmastersdk.ConfigMasterSdk
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class NonHiltViewModel : ViewModel() {

    private val _receivedConfig = MutableStateFlow<Pair<String, String>?>(null)
    val receivedConfig: StateFlow<Pair<String, String>?> = _receivedConfig

    fun addConfig(configName: String, jsonData: String) {
        ConfigMasterSdk.insertJsonAsync(
            configName,
            jsonData
        )
    }

    fun fetchConfigParam(
        configName: String = "",
        parameter: String = ""
    ) {
        viewModelScope.launch {
            val json = ConfigMasterSdk.getModifiedJson(configName)
            _receivedConfig.value = configName to json
        }
    }
}