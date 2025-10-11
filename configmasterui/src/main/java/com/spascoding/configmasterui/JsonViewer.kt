package com.spascoding.configmasterui

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.json.JSONObject

@Composable
fun JsonViewer(
    configName: String,
    jsonData: String,
    onEditConfirmed: (editedJson: String) -> Unit,
    modifier: Modifier = Modifier
) {
    var showConfirmDialog by remember { mutableStateOf(false) }

    val formattedJson = remember(jsonData) {
        try {
            JSONObject(jsonData).toString(4)
        } catch (e: Exception) {
            jsonData
        }
    }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .clickable { showConfirmDialog = true }
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .border(1.dp, MaterialTheme.colorScheme.outline, MaterialTheme.shapes.medium)
                .padding(8.dp)
        ) {
            Text(
                text = formattedJson,
                modifier = Modifier
                    .verticalScroll(rememberScrollState())
                    .fillMaxWidth(),
                style = LocalTextStyle.current.copy(lineHeight = 20.sp)
            )
        }
    }

    if (showConfirmDialog) {
        AlertDialog(
            onDismissRequest = { showConfirmDialog = false },
            title = { Text("Edit Configuration?") },
            text = { Text("Do you want to edit the configuration for App ID: $configName?") },
            confirmButton = {
                TextButton(onClick = {
                    showConfirmDialog = false
                    onEditConfirmed(jsonData)
                }) {
                    Text("Yes")
                }
            },
            dismissButton = {
                TextButton(onClick = { showConfirmDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }
}