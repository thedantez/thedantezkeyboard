package com.thedantezkeyboard

import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.view.inputmethod.InputMethodManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.thedantezkeyboard.ui.theme.ThedantezkeyboardTheme
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.foundation.layout.Row
import androidx.compose.ui.graphics.Color

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ThedantezkeyboardTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    KeyboardSetupScreen()
                }
            }
        }
    }
}

@Composable
fun KeyboardSetupScreen() {
    val context = LocalContext.current
    var emptyRowEnabled by remember { mutableStateOf(Preferences.isEmptyRowEnabled(context)) }

    Column(modifier = Modifier.padding(16.dp)) {
        Button(
            onClick = {
                // Открываем настройки клавиатуры
                val intent = Intent(Settings.ACTION_INPUT_METHOD_SETTINGS)
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                context.startActivity(intent)
            },
            modifier = Modifier.padding(8.dp)
        ) {
            Text("Открыть настройки клавиатуры")
        }

        Button(
            onClick = {
                // Выбор клавиатуры
                val imeManager = context.getSystemService(InputMethodManager::class.java)
                imeManager?.showInputMethodPicker()
            },
            modifier = Modifier.padding(8.dp)
        ) {
            Text("Выбрать клавиатуру")
        }


        Text(
            text = "Инструкция:\n" +
                    "1. Нажмите 'Открыть настройки клавиатуры'\n" +
                    "2. Включите 'Thedantez Keyboard'\n" +
                    "3. Нажмите 'Выбрать клавиатуру'\n" +
                    "4. Выберите 'Thedantez Keyboard'\n" +
                    "5. При появлении системного предупреждения нажмите ОК",
            modifier = Modifier.padding(16.dp)
        )

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(8.dp)
        ) {
            Text(
                "bottom empty row",
                modifier = Modifier.weight(1f)
            )
            Switch(
                checked = emptyRowEnabled,
                onCheckedChange = {
                    emptyRowEnabled = it
                    Preferences.setEmptyRowEnabled(context, it)
                }
            )
        }

        Text(
            text = "После переключения понадобится заново выбрать клавиатуру",
            modifier = Modifier.padding(6.dp),
            color = Color(150, 150, 150)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewKeyboardSetup() {
    ThedantezkeyboardTheme {
        KeyboardSetupScreen()
    }
}