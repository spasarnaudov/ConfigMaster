package com.spascoding.configmastersdk.presentation

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.spascoding.configmastersdk.data.preferences.AppPreferences
import com.spascoding.configmastersdk.domain.models.ConfigEntity
import com.spascoding.configmastersdk.domain.usecases.DeleteConfigUseCase
import com.spascoding.configmastersdk.domain.usecases.GetAllConfigNamesUseCase
import com.spascoding.configmastersdk.domain.usecases.GetConfigUseCase
import com.spascoding.configmastersdk.domain.usecases.InsertConfigUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ConfigViewModel @Inject constructor(
    private val insertConfigUseCase: InsertConfigUseCase,
    private val getConfigUseCase: GetConfigUseCase,
    private val getAllConfigNamesUseCase: GetAllConfigNamesUseCase,
    private val deleteConfigUseCase: DeleteConfigUseCase,
    private val appPreferences: AppPreferences
) : ViewModel() {

    var selectedConfigName by mutableStateOf<String?>(null)
        private set

    private val _configs = MutableLiveData<List<ConfigEntity>>()
    val configs: LiveData<List<ConfigEntity>> = _configs

    private val _configNames = MutableLiveData<List<String>>()
    val configNames: LiveData<List<String>> = _configNames

    fun fetchData() {
        viewModelScope.launch {
            val configNames = getAllConfigNamesUseCase.execute() // returns List<String>
            _configNames.postValue(configNames)
            val savedConfigName = appPreferences.getSelectedConfig()
            if (!savedConfigName.isNullOrEmpty() && configNames.contains(savedConfigName)) {
                selectConfig(savedConfigName)
            } else if (configNames.isNotEmpty()) {
                selectConfig(configNames.first())
            }
        }
    }

    fun selectConfig(configName: String) {
        selectedConfigName = configName
        viewModelScope.launch {
            appPreferences.saveSelectedConfig(configName)
        }
        fetchConfig(configName)
    }

    private fun fetchConfig(configName: String) {
        viewModelScope.launch {
            val configList = getConfigUseCase.execute(configName)
            _configs.postValue(configList)
        }
    }

    fun updateModifiedValueAndSave(config: ConfigEntity, newValue: String) {
        val updatedConfig = config.copy(modifiedValue = newValue)
        _configs.value = _configs.value?.map {
            if (it.name == config.name && it.parameter == config.parameter) updatedConfig else it
        }
        viewModelScope.launch {
            insertConfigUseCase.execute(listOf(updatedConfig)) // save immediately
        }
    }

    fun deleteConfig(configName: String) {
        viewModelScope.launch {
            deleteConfigUseCase.execute(configName)
        }
    }

}