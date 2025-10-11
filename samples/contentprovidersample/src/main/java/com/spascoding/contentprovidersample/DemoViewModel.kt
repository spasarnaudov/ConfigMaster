package com.spascoding.contentprovidersample

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DemoViewModel @Inject constructor(
    private val application: Application
) : ViewModel() {

    private val _receivedConfig = MutableStateFlow<Pair<String, String>?>(null)
    val receivedConfig: StateFlow<Pair<String, String>?> = _receivedConfig

    fun addConfig(configName: String, jsonData: String) {
        com.spascoding.configmasterhelper.ConfigMasterHelper.insertConfig(application, configName, jsonData)
        // com.spascoding.configmasterhelper.javahelper.ConfigMasterHelper.insertConfig(application, configName, jsonData)
    }

    fun fetchConfigParam(
        configName: String = "",
        parameter: String = ""
    ) {
        viewModelScope.launch {
            val json = if (parameter.isNotEmpty()) {
                com.spascoding.configmasterhelper.ConfigMasterHelper.fetchConfigParam(
                    application,
                    configName,
                    parameter
                )
                // com.spascoding.configmasterhelper.javahelper.ConfigMasterHelper.fetchConfigParam(...)
            } else {
                com.spascoding.configmasterhelper.ConfigMasterHelper.fetchConfig(
                    application,
                    configName
                )
                // com.spascoding.configmasterhelper.javahelper.ConfigMasterHelper.fetchConfig(...)
            }
            _receivedConfig.value = configName to (json ?: "")
        }
    }
}