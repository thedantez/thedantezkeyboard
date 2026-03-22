package com.thedantezkeyboard

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.view.inputmethod.InputMethodManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.rememberScrollState
import com.thedantezkeyboard.ui.theme.ThedantezkeyboardTheme
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.runtime.rememberCoroutineScope
import kotlinx.coroutines.launch
import kotlin.text.toInt
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.rememberNestedScrollInteropConnection

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
    val pagerState = rememberPagerState(pageCount = { 3 })
    val coroutineScope = rememberCoroutineScope()
    val tabs = listOf("sesnsitivity", "buttons", "about")

    Column(modifier = Modifier
        .fillMaxSize()
        .statusBarsPadding()
        .nestedScroll(rememberNestedScrollInteropConnection())
    ) {
        TabRow(selectedTabIndex = pagerState.currentPage) {
            tabs.forEachIndexed { index, title ->
                Tab(
                    selected = pagerState.currentPage == index,
                    onClick = {
                        coroutineScope.launch {
                            pagerState.animateScrollToPage(index)
                        }
                    },
                            text = { Text(title) }
                )
            }
        }
        HorizontalPager(
            state = pagerState,
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
        ) { page ->
            when (page) {
                0 -> SensitivityTab()
                1 -> ButtonsTab()
                2 -> AboutTab()
            }
        }
    }
}
@Composable
fun SensitivityTab() {
    val context = LocalContext.current
    var speeddelete by remember { mutableStateOf(Preferences.getSpeedDelete(context).toFloat()) }
    var gestureSensitivity by remember { mutableStateOf(Preferences.getGestureSensitivity(context).toFloat()) }
    var cursorSpeed by remember { mutableStateOf(Preferences.getCursorSpeed(context).toFloat()) }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
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

@Composable
fun ButtonsTab() {
    val context = LocalContext.current
    var emptyRowEnabled by remember { mutableStateOf(Preferences.isEmptyRowEnabled(context)) }
    var fontsize by remember { mutableStateOf(Preferences.getFontSize(context)) }
    var alwaysBigSymbsEnabled by remember { mutableStateOf(Preferences.isBigSymbsEnabled(context)) }
    var showCtrl by remember { mutableStateOf(Preferences.isShowCtrl(context)) }
    var showAlt by remember { mutableStateOf(Preferences.isShowAlt(context)) }
    var showDEL by remember { mutableStateOf(Preferences.isShowDEL(context)) }
    var showBS by remember { mutableStateOf(Preferences.isShowBS(context)) }
    var showENRU by remember { mutableStateOf(Preferences.isShowENRU(context)) }
    var buttonHeight by remember { mutableStateOf(Preferences.getButtonHeight(context).toFloat()) }

    Column(
        modifier = Modifier
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
            .fillMaxSize()
    ) {
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
        Row {
            Text("Show Ctrl", modifier=Modifier.weight(1f))
            Switch(checked = showCtrl, onCheckedChange = {
                showCtrl = it
                Preferences.setShowCtrl(context, it)
            })
        }
        Row {
            Text("Show Alt", modifier=Modifier.weight(1f))
            Switch(checked = showAlt, onCheckedChange = {
                showAlt = it
                Preferences.setShowAlt(context, it)
            })
        }
        Row {
            Text("Show EN/RU", modifier=Modifier.weight(1f))
            Switch(checked = showENRU, onCheckedChange = {
                showENRU = it
                Preferences.setShowENRU(context, it)
            })
        }
        Row {
            Text("Show DEL", modifier=Modifier.weight(1f))
            Switch(checked = showDEL, onCheckedChange = {
                showDEL = it
                Preferences.setShowDEL(context, it)
            })
        }
        Row {
            Text("Show BS", modifier=Modifier.weight(1f))
            Switch(checked = showBS, onCheckedChange = {
                showBS = it
                Preferences.setShowBS(context, it)
            })
        }
        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(8.dp)) {
            Text("button height: ${buttonHeight.toInt()}", modifier = Modifier.weight(1f))
            Slider(
                value = buttonHeight,
                onValueChange = { newHeight ->
                    buttonHeight = newHeight
                    Preferences.setButtonHeight(context, newHeight.toInt())
                },
                valueRange = 80f..200f,
                steps = 10,
                modifier = Modifier.weight(2f)
            )
        }
    }
}

@Composable
fun AboutTab() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        val context = LocalContext.current
        Text("Thedantez Keyboard v2.5", style = MaterialTheme.typography.titleLarge)
        Spacer(modifier = Modifier.height(16.dp))
        Text("A fully customizable keyboard with gestures and shortcuts")
        Spacer(modifier = Modifier.height(8.dp))
        Text("How to use:\n")
        Button(
            onClick = {
                // Открываем настройки клавиатуры
                val intent = Intent(Settings.ACTION_INPUT_METHOD_SETTINGS)
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                context.startActivity(intent)
            },
            modifier = Modifier.padding(8.dp)
        ) {
            Text("выбор клавиатур")
        }
        Button(
            onClick = {
                // Выбор клавиатуры
                val imeManager = context.getSystemService(InputMethodManager::class.java)
                imeManager?.showInputMethodPicker()
            },
            modifier = Modifier.padding(8.dp)
        ) {
            Text("выбрать клавиатуру")
        }

        Text("1. Нажмите 'Открыть выбор клавиатур'\n2. Включите 'Thedantez Keyboard'\n3. Нажмите 'Выбрать клавиатуру'\n4. Выберите 'Thedantez Keyboard'\n")
        Row{Text(
            text = "После изменений настроек понадобится заново запустить/выбрать клавиатуру",
            modifier = Modifier.padding(6.dp),
            color = Color(150, 150, 150)
        )}
        Text("Gestures on SPACE:")
        Text("- swipe up/down/left/right: move cursor (like DPAD)")
        Text("- alt + swipe left/right: Home/End")
        Text("- ctrl + swipe left/right: jump by words")
        Spacer(modifier = Modifier.height(8.dp))
        Text("Shortcuts:")
        Text("- ctrl + -/=: move cursor left/right by 1 symbol")
        Text("- alt + space: toggle language")
        Text("- alt + delete: esc")
        Text("- alt + t: enter ё")
        Text("- alt + -: enter ~")
        Text("- alt + =: toggle NumPad")
        Text("- ctrl/alt + anykey: emulating combination ctrl/alt with anykey")
        Text("- ctrl+alt + =: toggle advanced numpad (coming soon)")
        Spacer(modifier = Modifier.height(8.dp))
        Text("Cusomization: adjust button visibility, size, and sensitivity in the tabs above")
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewKeyboardSetup() {
    ThedantezkeyboardTheme {
        KeyboardSetupScreen()
    }
}