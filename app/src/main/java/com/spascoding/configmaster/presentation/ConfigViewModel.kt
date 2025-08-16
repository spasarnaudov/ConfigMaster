package com.spascoding.configmaster.presentation

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.spascoding.configmaster.data.preferences.AppPreferences
import com.spascoding.configmaster.domain.models.ConfigEntity
import com.spascoding.configmaster.domain.usecases.DeleteConfigurationUseCase
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
    private val getAllAppIdsUseCase: GetAllAppIdsUseCase,
    private val deleteConfigurationUseCase: DeleteConfigurationUseCase,
    private val appPreferences: AppPreferences
) : ViewModel() {

    var selectedAppId by mutableStateOf<String?>(null)
        private set

    private val _config = MutableLiveData<List<ConfigEntity>>()
    val config: LiveData<List<ConfigEntity>> = _config

    private val _appIds = MutableLiveData<List<String>>()
    val appIds: LiveData<List<String>> = _appIds

    fun fetchAppIds() {
        viewModelScope.launch {
            val ids = getAllAppIdsUseCase.execute() // returns List<String>
            _appIds.postValue(ids)
            val savedAppId = appPreferences.getSelectedAppId()
            if (!savedAppId.isNullOrEmpty() && ids.contains(savedAppId)) {
                selectAppId(savedAppId)
            } else if (ids.isNotEmpty()) {
                selectAppId(ids.first())
            }
        }
    }

    fun selectAppId(appId: String) {
        selectedAppId = appId
        viewModelScope.launch {
            appPreferences.saveSelectedAppId(appId)
        }
        fetchConfigs(appId)
    }

    private fun fetchConfigs(appId: String) {
        viewModelScope.launch {
            val configList = getConfigUseCase.execute(appId)
            _config.postValue(configList)
        }
    }

    fun updateModifiedValueAndSave(config: ConfigEntity, newValue: String) {
        val updatedConfig = config.copy(modifiedValue = newValue)
        _config.value = _config.value?.map {
            if (it.appId == config.appId && it.key == config.key) updatedConfig else it
        }
        viewModelScope.launch {
            insertConfigUseCase.execute(listOf(updatedConfig)) // save immediately
        }
    }

    fun deleteConfig(appId: String) {
        viewModelScope.launch {
            deleteConfigurationUseCase.execute(appId)
        }
    }

}