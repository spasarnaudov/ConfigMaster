package com.spascoding.configmaster.presentation

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import com.spascoding.configmastersdk.presentation.ConfigMasterActivity
import com.spascoding.configmastersdk.presentation.theme.ConfigMasterTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ConfigMasterTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding)
                    ) {
                        ConfigMasterLabel()
                    }
                }
            }
        }

    }

}

@Composable
fun ConfigMasterLabel() {
    val context = LocalContext.current
    var tapCount by remember { mutableStateOf(0) }

    Text(
        text = "ConfigMaster",
        modifier = Modifier.pointerInput(Unit) {
            awaitPointerEventScope {
                while (true) {
                    val event = awaitPointerEvent()
                    if (event.changes.isNotEmpty() && event.changes[0].pressed) {
                        tapCount++

//                        if (BuildConfig.DEBUG) {
//                            // Debug build → open immediately
//                            context.startActivity(Intent(context, ConfigMasterActivity::class.java))
//                            tapCount = 0
//                        } else {
                            // Release build → need 10 taps
//                            if (tapCount >= 10) {
                                context.startActivity(Intent(context, ConfigMasterActivity::class.java))
//                                tapCount = 0
//                            }
//                        }
                    }
                }
            }
        }
    )
}