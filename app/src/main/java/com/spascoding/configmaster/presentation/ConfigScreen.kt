package com.spascoding.configmaster.presentation

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.spascoding.configmaster.domain.models.ConfigEntity

@Composable
fun ConfigScreen(viewModel: ConfigViewModel = hiltViewModel()) {
    val configs by viewModel.config.observeAsState(emptyList())
    val appIds by viewModel.appIds.observeAsState(emptyList())

    LaunchedEffect(Unit) {
        viewModel.fetchAppIds()
    }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Spacer(modifier = Modifier.size(16.dp))

        Text("Select App ID:")
        AppIdDropdown(
            appIds = appIds,
            selectedAppId = viewModel.selectedAppId,
            onAppIdSelected = { viewModel.selectAppId(it) }
        )


        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = { viewModel.saveConfig() }) {
            Text("Save")
        }

        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn(modifier = Modifier.fillMaxSize().padding(top = 8.dp)) {
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

@Composable
fun AppIdDropdown(
    appIds: List<String>,
    selectedAppId: String?,
    onAppIdSelected: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { expanded = true }
            .padding(8.dp)
            .border(1.dp, Color.Gray, RoundedCornerShape(4.dp))
            .padding(12.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = selectedAppId ?: "Select App ID",
            )
            Icon(Icons.Default.ArrowDropDown, contentDescription = null)
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier
                .fillMaxWidth()
        ) {
            appIds.forEach { appId ->
                DropdownMenuItem(
                    text = { Text(appId) },
                    onClick = {
                        onAppIdSelected(appId)
                        expanded = false
                    }
                )
            }
        }
    }
}