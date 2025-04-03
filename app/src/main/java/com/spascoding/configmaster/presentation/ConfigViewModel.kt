package com.spascoding.configmaster.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.spascoding.configmaster.domain.models.ConfigEntity
import com.spascoding.configmaster.domain.usecases.GetConfigUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ConfigViewModel @Inject constructor(
    private val getConfigUseCase: GetConfigUseCase
) : ViewModel() {

    private val _configs = MutableLiveData<List<ConfigEntity>>()
    val configs: LiveData<List<ConfigEntity>> = _configs

    fun fetchConfigs() {
        viewModelScope.launch {
            val configList = getConfigUseCase.execute("DemoApp1")
            _configs.postValue(configList)
        }
    }

}