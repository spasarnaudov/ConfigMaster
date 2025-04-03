package com.spascoding.configmaster.presentation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.spascoding.configmaster.domain.models.ConfigEntity

@Composable
fun ConfigScreen(viewModel: ConfigViewModel = hiltViewModel()) {
    // Observe the configs LiveData
    val configs by viewModel.configs.observeAsState(emptyList())

    // Fetch configs when the screen is first composed
    LaunchedEffect(Unit) {
        viewModel.fetchConfigs()
    }

    // Show data in LazyColumn (RecyclerView equivalent in Compose)
    LazyColumn(
        modifier = Modifier.fillMaxSize()
    ) {
        items(configs) { config ->
            ConfigRow(config)
        }
    }
}

@Composable
fun ConfigRow(config: ConfigEntity) {
    Column(modifier = Modifier.fillMaxWidth().padding(8.dp)) {
        Text("Key: ${config.key}", style = MaterialTheme.typography.bodyLarge)
        Text("Original: ${config.originalValue}", style = MaterialTheme.typography.bodyMedium)
        Text("Modified: ${config.modifiedValue}", style = MaterialTheme.typography.bodyMedium)
        Divider()
    }
}