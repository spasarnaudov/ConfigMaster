package com.spascoding.configmastersdk.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.spascoding.configmastersdk.presentation.components.ConfigRowBordered
import com.spascoding.configmastersdk.presentation.components.PersistentSelectedItemDropdown

@Composable
fun ConfigMasterScreen(
    viewModel: ConfigViewModel = hiltViewModel(),
    title: String = ""
) {
    val configs by viewModel.configs.observeAsState(emptyList())
    val configNames by viewModel.configNames.observeAsState(emptyList())

    LaunchedEffect(Unit) {
        viewModel.fetchData()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(Modifier.height(8.dp))
        if (title.isNotEmpty()) {
            Text(
                text = title,
                style = MaterialTheme.typography.headlineSmall
            )
            Spacer(Modifier.height(8.dp))
        }
        PersistentSelectedItemDropdown(
            items = configNames,
            selectedItem = viewModel.selectedConfigName,
            onItemSelect = { viewModel.selectConfig(it) },
            onDeleteSelected = { viewModel.deleteConfig(it) }
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
                        viewModel.updateModifiedValueAndSave(config, newValue)
                    }
                )
            }
        }
    }
}