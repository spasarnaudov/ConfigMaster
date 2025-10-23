package com.spascoding.configmastersdk.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import com.spascoding.configmastersdk.ConfigMasterSdk
import com.spascoding.configmastersdk.domain.usecases.DeleteConfigUseCase
import com.spascoding.configmastersdk.domain.usecases.GetAllConfigNamesUseCase
import com.spascoding.configmastersdk.domain.usecases.GetConfigUseCase
import com.spascoding.configmastersdk.domain.usecases.InsertConfigUseCase
import com.spascoding.configmastersdk.presentation.theme.ConfigMasterTheme

class ConfigMasterActivity : ComponentActivity() {

    companion object {
        const val EXTRA_TITLE = "extra_title"
        const val EXTRA_SELECTED_CONFIG = "extra_selected_config"
    }

    private val viewModel by lazy {
        val repo = ConfigMasterSdk.provideRepository()
        val prefs = ConfigMasterSdk.providePreferences()

        val insert = InsertConfigUseCase(repo)
        val getAll = GetAllConfigNamesUseCase(repo)
        val get = GetConfigUseCase(repo)
        val delete = DeleteConfigUseCase(repo)

        ConfigViewModel(
            insertConfigUseCase = insert,
            getConfigUseCase = get,
            getAllConfigNamesUseCase = getAll,
            deleteConfigUseCase = delete,
            appPreferences = prefs
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Safety check
        require(ConfigMasterSdk.isInitialized()) {
            "ConfigMasterSdk not initialized. You must call ConfigMasterSdk.initialize(context) in your Application or before starting this Activity."
        }

        var title = ""
        if (intent.hasExtra(EXTRA_TITLE)) {
            title = intent.getStringExtra(EXTRA_TITLE) ?: ""
        }
        if (intent.hasExtra(EXTRA_SELECTED_CONFIG)) {
            val selectedConfig = intent.getStringExtra(EXTRA_SELECTED_CONFIG)
            if (selectedConfig != null) {
                viewModel.selectConfig(selectedConfig)
            }
        }

        enableEdgeToEdge()
        setContent {
            ConfigMasterTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding)
                    ) {
                        ConfigMasterScreen(
                            viewModel = viewModel,
                            title = title
                        )
                    }
                }
            }
        }
    }
}