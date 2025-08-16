package com.spascoding.configmaster.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.spascoding.configmaster.domain.models.ConfigEntity

@Composable
fun ConfigRowBordered(
    config: ConfigEntity,
    onModifiedValueChanged: (String) -> Unit
) {
    var showDialog by remember { mutableStateOf(false) }

    val backgroundColor = if (config.originalValue == config.modifiedValue) {
        Color.Green.copy(alpha = 0.5f) // light green
    } else {
        Color.Red.copy(alpha = 0.5f) // light red
    }

    Box(modifier = Modifier
        .fillMaxWidth()
        .border(1.dp, Color.Gray, RoundedCornerShape(8.dp))
        .clickable { showDialog = true }
        .background(backgroundColor, RoundedCornerShape(8.dp))
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