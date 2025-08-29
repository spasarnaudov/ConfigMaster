package com.spascoding.configmaster.presentation.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.spascoding.configmaster.domain.models.ConfigEntity

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
        // Key instead of generic title
        title = { Text(config.parameter, style = MaterialTheme.typography.titleLarge) },
        text = {
            Column(modifier = Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(12.dp)) {

                // --- Original Value ---
                OutlinedTextField(
                    value = config.originalValue,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Original Value") },
                    modifier = Modifier.fillMaxWidth(),
                    trailingIcon = {
                        Icon(
                            imageVector = Icons.Default.Refresh,
                            contentDescription = "Copy to modified",
                            modifier = Modifier.clickable {
                                modifiedValue = config.originalValue
                            }
                        )
                    }
                )

                // --- Modified Value ---
                OutlinedTextField(
                    value = modifiedValue,
                    onValueChange = { modifiedValue = it },
                    label = { Text("Modified Value") },
                    modifier = Modifier.fillMaxWidth(),
                    trailingIcon = {
                        if (modifiedValue.isNotEmpty()) {
                            Icon(
                                imageVector = Icons.Default.Delete,
                                contentDescription = "Clear text",
                                modifier = Modifier.clickable { modifiedValue = "" }
                            )
                        }
                    }
                )
            }
        }
    )
}
