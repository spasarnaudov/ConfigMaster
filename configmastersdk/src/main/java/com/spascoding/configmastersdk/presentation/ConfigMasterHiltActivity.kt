package com.spascoding.configmastersdk.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import com.spascoding.configmastersdk.presentation.theme.ConfigMasterTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ConfigMasterHiltActivity : ComponentActivity() {

    companion object {
        const val EXTRA_TITLE = "extra_title"
        const val EXTRA_SELECTED_CONFIG = "extra_selected_config"
    }

    private val viewModel: ConfigViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

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
                            title = title
                        )
                    }
                }
            }
        }
    }
}