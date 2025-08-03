package com.spascoding.configmaster.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.spascoding.configmaster.domain.models.ConfigEntity
import com.spascoding.configmaster.domain.usecases.GetAllAppIdsUseCase
import com.spascoding.configmaster.domain.usecases.GetConfigUseCase
import com.spascoding.configmaster.domain.usecases.InsertConfigUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ConfigViewModel @Inject constructor(
    private val insertConfigUseCase: InsertConfigUseCase,
    private val getConfigUseCase: GetConfigUseCase,
    private val getAllAppIdsUseCase: GetAllAppIdsUseCase
) : ViewModel() {

    private val _config = MutableLiveData<List<ConfigEntity>>()
    val config: LiveData<List<ConfigEntity>> = _config

    private val _appIds = MutableLiveData<List<String>>()
    val appIds: LiveData<List<String>> = _appIds

    var selectedAppId: String? = null
        private set

    fun fetchAppIds() {
        viewModelScope.launch {
            val ids = getAllAppIdsUseCase.execute() // returns List<String>
            _appIds.postValue(ids)
            if (selectedAppId == null && ids.isNotEmpty()) {
                selectAppId(ids.first())
            }
        }
    }

    fun selectAppId(appId: String) {
        selectedAppId = appId
        fetchConfigs(appId)
    }

    private fun fetchConfigs(appId: String) {
        viewModelScope.launch {
            val configList = getConfigUseCase.execute(appId)
            _config.postValue(configList)
        }
    }

    fun updateModifiedValue(config: ConfigEntity, newValue: String) {
        val updatedConfig = config.copy(modifiedValue = newValue)
        _config.value = _config.value?.map {
            if (it.appId == config.appId && it.key == config.key) updatedConfig else it
        }
    }

    fun saveConfig() {
        viewModelScope.launch {
            _config.value?.let { insertConfigUseCase.execute(it) }
        }
    }

}