package com.spascoding.contentprovidersample

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.spascoding.configmasterui.SearchBar
import com.spascoding.contentprovidersample.components.JsonViewer
import com.spascoding.contentprovidersample.components.AddConfigDialog
import org.json.JSONObject

@Composable
fun ContentProviderSampleScreen(
    viewModel: DemoViewModel = hiltViewModel(),
    modifier: Modifier = Modifier
) {
    val receivedConfig by viewModel.receivedConfig.collectAsState()
    var fetchConfig by remember { mutableStateOf("") }
    var fetchConfigParameter by remember { mutableStateOf("") }
    var showAddDialog by remember { mutableStateOf(false) }
    var isEditMode by remember { mutableStateOf(false) }
    var editConfigName by remember { mutableStateOf("") }
    var configItems by remember { mutableStateOf<List<ConfigItem>>(emptyList()) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Content Provider Sample", style = MaterialTheme.typography.titleLarge)
            Spacer(Modifier.width(24.dp))
            Button(
                onClick = { showAddDialog = true}
            ) {
                Icon(Icons.Filled.Add, "Add Config")
            }
        }

        SearchBar(
            label = "Config name",
            value = fetchConfig,
            onValueChange = {
                fetchConfig = it
                if (fetchConfig.isNotBlank()) {
                    viewModel.fetchConfigParam(fetchConfig.trim())
                }
            },
            onClear = {
                fetchConfig = ""
                viewModel.fetchConfigParam()
            }
        )

        SearchBar(
            label = "Parameter name",
            value = fetchConfigParameter,
            onValueChange = {
                fetchConfigParameter = it
                if (fetchConfig.isNotBlank()) {
                    viewModel.fetchConfigParam(fetchConfig.trim(), fetchConfigParameter.trim())
                }
            },
            onClear = {
                fetchConfigParameter = ""
                viewModel.fetchConfigParam()
            }
        )

        Spacer(Modifier.height(16.dp))

        if (fetchConfig.isNotBlank()) {
            receivedConfig?.let { config ->
                JsonViewer(
                    config = config,
                    onEditConfirmed = {
                        configItems = it
                        isEditMode = true
                        showAddDialog = true
                    }
                )
            }
        }
    }

    if (showAddDialog) {
        AddConfigDialog(
            onDismiss = {
                showAddDialog = false
                isEditMode = false
            },
            onAdd = { appId, keyValuePairs ->
                val jsonObject = JSONObject()
                keyValuePairs.forEach { config ->
                    if (config.name.isNotBlank()) {
                        jsonObject.put(config.name, config.jsonData)
                    }
                }
                viewModel.addConfig(appId, jsonObject.toString())
                showAddDialog = false
            },
            initialConfig = if (isEditMode) editConfigName else "",
            initialConfigItems = if (isEditMode) configItems else emptyList()
        )
    }

}