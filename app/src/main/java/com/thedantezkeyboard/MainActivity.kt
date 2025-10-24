package com.thedantezkeyboard

import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.view.inputmethod.InputMethodManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.thedantezkeyboard.ui.theme.ThedantezkeyboardTheme

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
    var fontsize by remember { mutableStateOf(Preferences.getFontSize(context)) }
    var speeddelete by remember { mutableStateOf(Preferences.getSpeedDelete(context).toFloat()) }
    var alwaysBigSymbsEnabled by remember { mutableStateOf(Preferences.isBigSymbsEnabled(context)) }
    var gestureSensitivity by remember { mutableStateOf(Preferences.getGestureSensitivity(context).toFloat()) }
    var cursorSpeed by remember { mutableStateOf(Preferences.getCursorSpeed(context).toFloat()) }

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

        Row() {}

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(8.dp)
        ) {
            Text(
                "Switch always uppercase",
                modifier = Modifier.weight(1f)
            )
            Switch(
                checked = alwaysBigSymbsEnabled,
                {
                    alwaysBigSymbsEnabled = it
                    Preferences.setBigSymbsEnabled(context, it)
                }
            )
        }

        Row() {}

        Row( //slider for change font size
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(8.dp)
        ) {
            Text(
                "Font size: ${fontsize.toInt()} ",
                modifier = Modifier.weight(1f)
            )
            Slider(
                value = fontsize,
                onValueChange = { newSize ->
                    fontsize = newSize
                    Preferences.setFontSize(context, newSize)
                },
                valueRange = 12f..24f, // Диапазон размеров
                steps = 11, // Шаги (12, 13, 14...24)
                modifier = Modifier.weight(2f)
            )
        }
        Row() {}

        Row(    //slider for change volume delete
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(8.dp)
        ) {
            Text(
                "Speed delete ${speeddelete.toInt()} ",
                modifier = Modifier.weight(1f)
            )
            Slider(
                value = speeddelete,
                onValueChange = { newSpeed ->
                    speeddelete = newSpeed
                    Preferences.setSpeedDelete(context, newSpeed.toInt())
                },
                valueRange = 10f..500f,
                steps = 24,
                modifier = Modifier.weight(2f)
            )
        }
        Row(
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = "more speed = slower deleting",
                textAlign = TextAlign.End,
                modifier = Modifier.fillMaxWidth()
            )
        }
        Row(
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = "more speed = slower deleting",
                textAlign = TextAlign.End,
                modifier = Modifier.fillMaxWidth()
            )
        }

        Row() {}

        Row(    //slider for gesture sensitivity
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(8.dp)
        ) {
            Text(
                "Gesture sensitivity: ${gestureSensitivity.toInt()}",
                modifier = Modifier.weight(1f)
            )
            Slider(
                value = gestureSensitivity,
                onValueChange = { newSensitivity ->
                    gestureSensitivity = newSensitivity
                    Preferences.setGestureSensitivity(context, newSensitivity.toInt())
                },
                valueRange = 10f..150f, // Минимум 10, максимум 150 пикселей
                steps = 13, // 14 значений от 10 до 150
                modifier = Modifier.weight(2f)
            )
        }
        Row(
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = "Higher value = more movement needed to start gesture",
                textAlign = TextAlign.End,
                modifier = Modifier.fillMaxWidth()
            )
        }

        Row() {}

        Row(    //slider for cursor speed
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(8.dp)
        ) {
            Text(
                "Cursor speed: ${cursorSpeed.toInt()}",
                modifier = Modifier.weight(1f)
            )
            Slider(
                value = cursorSpeed,
                onValueChange = { newSpeed ->
                    cursorSpeed = newSpeed
                    Preferences.setCursorSpeed(context, newSpeed.toInt())
                },
                valueRange = 10f..100f, // Минимум 10мс, максимум 100мс задержки
                steps = 8, // 9 значений от 10 до 100
                modifier = Modifier.weight(2f)
            )
        }
        Row(
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = "Higher value = slower cursor movement",
                textAlign = TextAlign.End,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewKeyboardSetup() {
    ThedantezkeyboardTheme {
        KeyboardSetupScreen()
    }
}