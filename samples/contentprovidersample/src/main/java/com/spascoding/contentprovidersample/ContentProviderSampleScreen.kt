package com.spascoding.contentprovidersample

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.spascoding.configmasterui.SearchBar
import com.spascoding.contentprovidersample.components.JsonViewer
import com.spascoding.configmasterui.ConfigDialog
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
    var configPairs by remember { mutableStateOf<List<Pair<String, String>>>(emptyList()) }

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
                onClick = {
                    isEditMode = false
                    showAddDialog = true
                }
            ) {
                Icon(Icons.Filled.Add, contentDescription = "Add Config")
            }
        }

        Spacer(Modifier.height(8.dp))

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
                    onEditConfirmed = { editedPairs ->
                        // Convert ConfigItems from JsonViewer into key–value pairs
                        configPairs = editedPairs.map { it.name to it.jsonData }
                        editConfigName = fetchConfig
                        isEditMode = true
                        showAddDialog = true
                    }
                )
            }
        }
    }

    // --- Add/Edit Config Dialog ---
    if (showAddDialog) {
        ConfigDialog(
            onDismiss = {
                showAddDialog = false
                isEditMode = false
            },
            onAdd = { appId, keyValuePairs ->
                val jsonObject = JSONObject()
                keyValuePairs.forEach { (key, value) ->
                    if (key.isNotBlank()) {
                        jsonObject.put(key, value)
                    }
                }
                viewModel.addConfig(appId, jsonObject.toString())
                showAddDialog = false
            },
            initialConfigName = if (isEditMode) editConfigName else "",
            initialPairs = if (isEditMode) configPairs else emptyList()
        )
    }
}