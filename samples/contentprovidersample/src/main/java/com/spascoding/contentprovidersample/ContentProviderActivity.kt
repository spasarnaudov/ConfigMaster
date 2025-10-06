package com.spascoding.contentprovidersample

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import com.spascoding.contentprovidersample.ui.theme.ConfigMasterTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ContentProviderActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ConfigMasterTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    ContentProviderSampleScreen(
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}