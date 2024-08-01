package dev.mahdidroid.compose_lock

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import dev.mahdidroid.compose_lock.activities.BaseLockComponentActivity
import dev.mahdidroid.compose_lock.ui.theme.MyApplicationTheme
import dev.mahdidroid.compose_lock.utils.ThemeConfiguration

class MainComponentActivity : BaseLockComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                applyTheme(ThemeConfiguration.Material3Config(MaterialTheme.colorScheme))
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding)
                    ) {
                        Text(text = "Main Screen", color = MaterialTheme.colorScheme.primary)
                    }
                }
            }
        }
    }
}