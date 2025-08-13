package com.spascoding.configmaster.presentation

import android.content.Context
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
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.spascoding.configmaster.domain.models.ConfigEntity
import com.spascoding.configmaster.presentation.components.PersistentSelectedItemDropdown

fun getAppVersionName(context: Context): String {
    val packageInfo = context.packageManager.getPackageInfo(context.packageName, 0)
    return packageInfo.versionName.toString()
}

@Composable
fun ConfigScreen(viewModel: ConfigViewModel = hiltViewModel()) {
    val configs by viewModel.config.observeAsState(emptyList())
    val appIds by viewModel.appIds.observeAsState(emptyList())

    LaunchedEffect(Unit) {
        viewModel.fetchAppIds()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.size(16.dp))

        Row (
            verticalAlignment = Alignment.CenterVertically
        ){
            val context = LocalContext.current
            Text("Config Master - v " + getAppVersionName(context), style = MaterialTheme.typography.headlineSmall)
            Spacer(Modifier.width(24.dp))
            Button(onClick = { viewModel.saveConfig() }) {
                Icon(Icons.Filled.Done, "Save")
            }
        }

        Spacer(Modifier.height(8.dp))

        PersistentSelectedItemDropdown(
            items = appIds,
            selectedItem = viewModel.selectedAppId,
            onItemSelect = { viewModel.selectAppId(it) },
        )

        Spacer(Modifier.height(8.dp))

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
            items(configs) { config ->
                ConfigRowBordered (
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
fun ConfigRowBordered(
    config: ConfigEntity,
    onModifiedValueChanged: (String) -> Unit
) {
    var showDialog by remember { mutableStateOf(false) }

    Box(modifier = Modifier
        .fillMaxWidth()
        .border(1.dp, Color.Gray, RoundedCornerShape(8.dp))
        .clickable { showDialog = true }
        .padding(8.dp)
    ) {
        Column {
            Text(config.key, style = MaterialTheme.typography.bodyLarge)
            Text("Original: ${config.originalValue}", style = MaterialTheme.typography.bodyMedium)
            Text("Modified: ${config.modifiedValue}", style = MaterialTheme.typography.bodyMedium)
        }

        if (showDialog) {
            ConfigDialog(
                config = config,
                onDismiss = { showDialog = false },
                onModifiedValueChange = { newValue ->
                    onModifiedValueChanged(newValue)
                }
            )
        }
    }
}

@Composable
fun ConfigDialog(
    config: ConfigEntity,
    onDismiss: () -> Unit,
    onModifiedValueChange: (String) -> Unit
) {
    var modifiedValue by remember { mutableStateOf(config.modifiedValue) }

    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            Button(onClick = {
                onModifiedValueChange(modifiedValue)
                onDismiss()
            }) {
                Text("Save")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        },
        title = { Text("Config Details") },
        text = {
            Column(modifier = Modifier.fillMaxWidth()) {
                Text("Key: ${config.key}", style = MaterialTheme.typography.bodyLarge)
                Spacer(modifier = Modifier.height(8.dp))
                Text("Original Value:", style = MaterialTheme.typography.bodyMedium)
                OutlinedTextField(
                    value = config.originalValue,
                    onValueChange = {},
                    readOnly = true,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text("Modified Value:", style = MaterialTheme.typography.bodyMedium)
                OutlinedTextField(
                    value = modifiedValue,
                    onValueChange = { modifiedValue = it },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    )
}