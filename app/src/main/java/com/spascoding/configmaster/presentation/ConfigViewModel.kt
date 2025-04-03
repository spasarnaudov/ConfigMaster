package com.spascoding.configmaster.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.spascoding.configmaster.domain.models.ConfigEntity
import com.spascoding.configmaster.domain.usecases.GetConfigUseCase
import com.spascoding.configmaster.domain.usecases.InsertConfigUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ConfigViewModel @Inject constructor(
    private val insertConfigUseCase: InsertConfigUseCase,
    private val getConfigUseCase: GetConfigUseCase,
) : ViewModel() {

    private val _config = MutableLiveData<List<ConfigEntity>>()
    val config: LiveData<List<ConfigEntity>> = _config

    fun fetchConfigs() {
        viewModelScope.launch {
            val configList = getConfigUseCase.execute("DemoApp1")
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