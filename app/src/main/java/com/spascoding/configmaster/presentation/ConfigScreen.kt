package com.spascoding.configmaster.presentation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.spascoding.configmaster.domain.models.ConfigEntity

@Composable
fun ConfigScreen(viewModel: ConfigViewModel = hiltViewModel()) {
    val configs by viewModel.config.observeAsState(emptyList())
    LaunchedEffect(Unit) {
        viewModel.fetchConfigs()
    }

    Column {
        Spacer(modifier = Modifier.size(32.dp))
        Button(
            onClick = {
                viewModel.saveConfig()
            }
        ) {
            Text("Save")
        }
        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(8.dp)
        ) {
            items(configs) { config ->
                ConfigRow(
                    config = config,
                    onModifiedValueChanged = { newValue ->
                        viewModel.updateModifiedValue(config, newValue)
                    }
                )
            }
        }
    }
}

@Composable
fun ConfigRow(
    config: ConfigEntity,
    onModifiedValueChanged: (String) -> Unit
) {
    Text("Key: ${config.key}", style = MaterialTheme.typography.bodyLarge)
    Text("Original: ${config.originalValue}", style = MaterialTheme.typography.bodyMedium)
    var modifiedValue by remember { mutableStateOf(config.modifiedValue) }
    TextField(
        value = modifiedValue,
        onValueChange = { newValue ->
            modifiedValue = newValue
            onModifiedValueChanged(newValue) // Trigger save to database
        },
        label = { Text("Modified Value") },
        modifier = Modifier.width(200.dp)
    )
    Divider()
}