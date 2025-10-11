package com.spascoding.configmasterui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun ConfigDialog(
    onDismiss: () -> Unit,
    onAdd: (String, List<Pair<String, String>>) -> Unit,
    initialConfigName: String = "",
    initialPairs: List<Pair<String, String>> = emptyList()
) {
    var configName by remember { mutableStateOf(initialConfigName) }
    val keyValuePairs = remember {
        mutableStateListOf<Pair<String, String>>().apply {
            addAll(initialPairs.ifEmpty { listOf("" to "") })
        }
    }

    val listState = rememberLazyListState()
    var shouldScroll by remember { mutableStateOf(false) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(if (initialConfigName.isBlank()) "New Configuration" else "Edit Configuration")
        },
        text = {
            Column {
                // Configuration name input
                OutlinedTextField(
                    value = configName,
                    onValueChange = { configName = it },
                    label = { Text("Configuration Name") },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = initialConfigName.isBlank() // Prevent renaming in edit mode
                )

                Spacer(Modifier.height(8.dp))
                Text("Parameters", fontWeight = FontWeight.Bold)

                // Key–Value list
                LazyColumn(
                    state = listState,
                    modifier = Modifier
                        .heightIn(max = 300.dp)
                        .fillMaxWidth()
                ) {
                    items(keyValuePairs.size) { index ->
                        val (key, value) = keyValuePairs[index]

                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(vertical = 4.dp)
                        ) {
                            OutlinedTextField(
                                value = key,
                                onValueChange = {
                                    keyValuePairs[index] = it to value
                                },
                                label = { Text("Key") },
                                modifier = Modifier
                                    .weight(1f)
                                    .padding(end = 4.dp)
                            )
                            OutlinedTextField(
                                value = value,
                                onValueChange = {
                                    keyValuePairs[index] = key to it
                                },
                                label = { Text("Value") },
                                modifier = Modifier
                                    .weight(1f)
                                    .padding(start = 4.dp)
                            )
                            IconButton(
                                onClick = {
                                    if (keyValuePairs.size > 1) keyValuePairs.removeAt(index)
                                }
                            ) {
                                Icon(Icons.Default.Delete, contentDescription = "Remove pair")
                            }
                        }
                    }
                }

                Spacer(Modifier.height(8.dp))

                Button(onClick = {
                    keyValuePairs.add("" to "")
                    shouldScroll = true
                }) {
                    Text("Add Key–Value Pair")
                }

                // Auto-scroll to the new item
                LaunchedEffect(shouldScroll, keyValuePairs.size) {
                    if (shouldScroll) {
                        listState.animateScrollToItem(keyValuePairs.lastIndex)
                        shouldScroll = false
                    }
                }
            }
        },
        confirmButton = {
            Button(onClick = {
                if (configName.isNotBlank()) {
                    onAdd(configName.trim(), keyValuePairs.toList())
                }
            }) {
                Text(if (initialConfigName.isBlank()) "Add" else "Update")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}