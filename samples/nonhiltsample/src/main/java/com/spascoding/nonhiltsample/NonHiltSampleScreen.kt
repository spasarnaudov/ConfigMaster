package com.spascoding.nonhiltsample

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import com.spascoding.configmasterui.ConfigDialog
import com.spascoding.configmasterui.JsonViewer
import com.spascoding.configmasterui.SearchBar
import org.json.JSONObject

@Composable
fun NonHiltSampleScreen(
    modifier: Modifier = Modifier
) {
    // Manually create ViewModel
    val viewModel: NonHiltViewModel = viewModel(
        factory = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                @Suppress("UNCHECKED_CAST")
                return NonHiltViewModel() as T
            }
        }
    )

    val receivedConfig by viewModel.receivedConfig.collectAsState()
    var fetchConfig by remember { mutableStateOf("") }
    var fetchConfigParameter by remember { mutableStateOf("") }
    var showConfigDialog by remember { mutableStateOf(false) }
    var isEditMode by remember { mutableStateOf(false) }
    var editConfigName by remember { mutableStateOf("") }
    var configPairs by remember { mutableStateOf<List<Pair<String, String>>>(emptyList()) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text("Non-Hilt Sample", style = MaterialTheme.typography.titleLarge)
            Spacer(Modifier.width(24.dp))
            Button(
                onClick = {
                    isEditMode = false
                    showConfigDialog = true
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
            receivedConfig?.let { (_, jsonData) ->
                JsonViewer(
                    configName = fetchConfig,
                    jsonData = jsonData,
                    onEditConfirmed = { editedJson ->
                        val jsonObject = JSONObject(editedJson)
                        val pairs = mutableListOf<Pair<String, String>>()
                        for (key in jsonObject.keys()) {
                            pairs.add(key to jsonObject.optString(key, ""))
                        }

                        configPairs = pairs
                        editConfigName = fetchConfig
                        isEditMode = true
                        showConfigDialog = true
                    }
                )
            }
        }
    }

    // --- Add/Edit Config Dialog ---
    if (showConfigDialog) {
        ConfigDialog(
            onDismiss = {
                showConfigDialog = false
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
                showConfigDialog = false
            },
            initialConfigName = if (isEditMode) editConfigName else "",
            initialPairs = if (isEditMode) configPairs else emptyList()
        )
    }
}