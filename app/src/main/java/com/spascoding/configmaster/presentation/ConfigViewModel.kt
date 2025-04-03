package com.spascoding.configmaster.presentation

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.spascoding.configmaster.domain.models.ConfigEntity
import com.spascoding.configmaster.domain.usecases.GetAllConfigsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ConfigViewModel @Inject constructor(
    private val getAllConfigsUseCase: GetAllConfigsUseCase
) : ViewModel() {

    private val _configs = MutableLiveData<List<ConfigEntity>>()
    val configs: LiveData<List<ConfigEntity>> = _configs

    fun fetchConfigs() {
        viewModelScope.launch {
            val configList = getAllConfigsUseCase.execute()
            _configs.postValue(configList)
        }
    }

}