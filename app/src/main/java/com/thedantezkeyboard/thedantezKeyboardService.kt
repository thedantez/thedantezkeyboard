package com.thedantezkeyboard

import android.annotation.SuppressLint
import android.graphics.Color
import android.inputmethodservice.InputMethodService
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputConnection
import android.widget.Button
import android.widget.LinearLayout
import android.widget.LinearLayout.LayoutParams
import android.view.MotionEvent
import android.os.Handler
import android.os.Looper
import androidx.core.content.ContextCompat
import kotlin.toString

class PCKeyboardService : InputMethodService() {
    private val handler = Handler(Looper.getMainLooper())

    private var isBackspaceLongPress = false
    private var backspaceLongPressWithCtrl = false
    private val backspaceRunnable = object : Runnable {
        override fun run() {
            if (isBackspaceLongPress) {
                if (backspaceLongPressWithCtrl) {
                    deleteWordBeforeCursor()
                } else {
                    handleBackspace()
                }
                handler.postDelayed(this, 30)
            }
        }
    }

    private var isDelLongPress = false
    private var delLongPressWithCtrl = false
    private val delRunnable = object : Runnable {
        override fun run() {
            if (isDelLongPress) {
                if (delLongPressWithCtrl) {
                    deleteWordAfterCursor()
                } else {
                    handleDelete()
                }
                handler.postDelayed(this, 30)
            }
        }
    }

    private val TAG = "KeyboardService"

    private var keyboardView: View? = null

    override fun onDestroy() {
        keyboardView = null
        handler.removeCallbacksAndMessages(null)
        super.onDestroy()
    }

    override fun onWindowHidden() {
        super.onWindowHidden()
        isBackspaceLongPress = false
        isDelLongPress = false
        handler.removeCallbacks(backspaceRunnable)
        handler.removeCallbacks(delRunnable)
    }

    // Состояния модификаторов
    private var isCtrlPressed = false
    private var isAltPressed = false
    private var isShiftPressed = false
    private var isCapsLock = false
    private var isEnglishLayout = true

    // Раскладки
    private val enLayout = mapOf(
        "1" to "1", "2" to "2", "3" to "3", "4" to "4", "5" to "5",
        "6" to "6", "7" to "7", "8" to "8", "9" to "9", "0" to "0",
        "-" to "-", "=" to "=", "~" to "~", "q" to "q", "w" to "w",
        "e" to "e", "r" to "r", "t" to "t", "y" to "y", "u" to "u",
        "i" to "i", "o" to "o", "p" to "p", "[" to "[", "]" to "]",
        "\\" to "\\", "a" to "a", "s" to "s", "d" to "d", "f" to "f",
        "g" to "g", "h" to "h", "j" to "j", "k" to "k", "l" to "l",
        ";" to ";", "'" to "'", "z" to "z", "x" to "x", "c" to "c",
        "v" to "v", "b" to "b", "n" to "n", "m" to "m", "," to ",",
        "." to ".", "/" to "/"
    )

    private val enShiftLayout = mapOf(
        "1" to "!", "2" to "@", "3" to "#", "4" to "$", "5" to "%",
        "6" to "^", "7" to "&", "8" to "*", "9" to "(", "0" to ")",
        "-" to "_", "=" to "+", "~" to "`", "q" to "Q", "w" to "W",
        "e" to "E", "r" to "R", "t" to "T", "y" to "Y", "u" to "U",
        "i" to "I", "o" to "O", "p" to "P", "[" to "{", "]" to "}",
        "\\" to "|", "a" to "A", "s" to "S", "d" to "D", "f" to "F",
        "g" to "G", "h" to "H", "j" to "J", "k" to "K", "l" to "L",
        ";" to ":", "'" to "\"", "z" to "Z", "x" to "X", "c" to "C",
        "v" to "V", "b" to "B", "n" to "N", "m" to "M", "," to "<",
        "." to ">", "/" to "?"
    )

    private val ruLayout = mapOf(
        "1" to "1", "2" to "2", "3" to "3", "4" to "4", "5" to "5",
        "6" to "6", "7" to "7", "8" to "8", "9" to "9", "0" to "0",
        "-" to "-", "=" to "=", "~" to "ё", "q" to "й", "w" to "ц",
        "e" to "у", "r" to "к", "t" to "е", "y" to "н", "u" to "г",
        "i" to "ш", "o" to "щ", "p" to "з", "[" to "х", "]" to "ъ",
        "\\" to "\\", "a" to "ф", "s" to "ы", "d" to "в", "f" to "а",
        "g" to "п", "h" to "р", "j" to "о", "k" to "л", "l" to "д",
        ";" to "ж", "'" to "э", "z" to "я", "x" to "ч", "c" to "с",
        "v" to "м", "b" to "и", "n" to "т", "m" to "ь", "," to "б",
        "." to "ю", "/" to "."
    )

    private val ruShiftLayout = mapOf(
        "1" to "!", "2" to "\"", "3" to "№", "4" to ";", "5" to "%",
        "6" to ":", "7" to "?", "8" to "*", "9" to "(", "0" to ")",
        "-" to "_", "=" to "+", "~" to "Ё", "q" to "Й", "w" to "Ц",
        "e" to "У", "r" to "К", "t" to "Е", "y" to "Н", "u" to "Г",
        "i" to "Ш", "o" to "Щ", "p" to "З", "[" to "Х", "]" to "Ъ",
        "\\" to "/", "a" to "Ф", "s" to "Ы", "d" to "В", "f" to "А",
        "g" to "П", "h" to "Р", "j" to "О", "k" to "Л", "l" to "Д",
        ";" to "Ж", "'" to "Э", "z" to "Я", "x" to "Ч", "c" to "С",
        "v" to "М", "b" to "И", "n" to "Т", "m" to "Ь", "," to "Б",
        "." to "Ю", "/" to ","
    )

    private fun getCurrentLayout(): Map<String, String> {
        return when {
            isEnglishLayout && (isShiftPressed || isCapsLock) -> enShiftLayout
            isEnglishLayout -> enLayout
            isShiftPressed || isCapsLock -> ruShiftLayout
            else -> ruLayout
        }
    }

    private fun getKeyDisplayText(key: String): String {
        val layout = when {
            isEnglishLayout && (isShiftPressed || isCapsLock) -> enShiftLayout
            isEnglishLayout -> enLayout
            isShiftPressed || isCapsLock -> ruShiftLayout
            else -> ruLayout
        }
        return layout[key] ?: key
    }

    private fun resetModifiers() {
        if (isCtrlPressed || isAltPressed || isShiftPressed || isCapsLock) {
            isCtrlPressed = false
            isAltPressed = false
            isCapsLock = false
            isShiftPressed = false
            updateKeyboardState()
        }
    }

    override fun onCreateInputView(): View {
        return createKeyboardView().also {
            keyboardView = it
        }
    }

    override fun onFinishInputView(finishingInput: Boolean) {
        handler.removeCallbacksAndMessages(null)
        resetModifiers()
        super.onFinishInputView(finishingInput)
    }

    private fun createKeyboardView(): View {
        val keyboardLayout = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)
            setBackgroundColor(Color.BLACK)

            //new:
            overScrollMode = View.OVER_SCROLL_NEVER
            isVerticalScrollBarEnabled = false
        }

        // Ряд 1: 1 2 3 4 5 6 7 8 9 0 - = Backspace
        val row1 = LinearLayout(this).apply {
            orientation = LinearLayout.HORIZONTAL
            layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)
        }
        addKeyToRow(row1, "DEL", 1.5f, ::handleDelete, true)
        listOf("1", "2", "3", "4", "5", "6", "7", "8", "9", "0", "-", "=").forEach { key ->
            addKeyToRow(row1, key, 1f)
        }

        // Ряд 2: q w e r t y u i o p [ ] \
        val row2 = LinearLayout(this).apply {
            orientation = LinearLayout.HORIZONTAL
            layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)
        }
        listOf("q", "w", "e", "r", "t", "y", "u", "i", "o", "p", "[", "]", "\\").forEach { key ->
            addKeyToRow(row2, key, 1f)
        }

        // Ряд 3: a s d f g h j k l ; '
        val row3 = LinearLayout(this).apply {
            orientation = LinearLayout.HORIZONTAL
            layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)
        }
        listOf("a", "s", "d", "f", "g", "h", "j", "k", "l", ";", "'").forEach { key ->
            addKeyToRow(row3, key, 1f)
        }
        addKeyToRow(row3, "BS", 1.5f, ::handleBackspace, true)

        // Ряд 4: Shift z x c v b n m , . / En/Ru
        val row4 = LinearLayout(this).apply {
            orientation = LinearLayout.HORIZONTAL
            layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)
        }
        addModifierButton(row4, "SH", 1.5f, ::handleShift, isShiftPressed || isCapsLock)
        listOf("z", "x", "c", "v", "b", "n", "m", ",", ".", "/").forEach { key ->
            addKeyToRow(row4, key, 1f)
        }
        addKeyToRow(row4, "EN", 1.5f, ::toggleLanguage)

        // Ряд 5: Ctrl Space Alt Enter
        val row5 = LinearLayout(this).apply {
            orientation = LinearLayout.HORIZONTAL
            layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)
        }
        addModifierButton(row5, "CTRL", 1.25f, { toggleModifier("CTRL") }, isCtrlPressed)
        addKeyToRow(row5, "Space", 4.5f, { sendText(" ") })
        addModifierButton(row5, "ALT", 1.25f, { toggleModifier("ALT") }, isAltPressed)
        addKeyToRow(row5, "ENTR", 1.5f, ::handleEnter, true)

        // Добавляем все ряды в основной layout
        listOf(row1, row2, row3, row4, row5).forEach { keyboardLayout.addView(it) }

        return keyboardLayout
    }

    // Вспомогательная функция для добавления обычной кнопки
    @SuppressLint("ClickableViewAccessibility")
    private fun addKeyToRow(
        row: LinearLayout,
        key: String,
        weight: Float,
        onClick: (() -> Unit)? = null,
        nonSymbolButton: Boolean = false
    ) {
        val button = Button(this).apply {
            tag = key
            text = getKeyDisplayText(key)
            layoutParams = LayoutParams(0, 150, weight).apply {
                setMargins(2, 2, 2, 2)
            }
            background = when {
                nonSymbolButton -> ContextCompat.getDrawable(
                    context,
                    R.drawable.rounded_button_dkgray
                )

                else -> ContextCompat.getDrawable(context, R.drawable.rounded_button_black)
            }
            textSize = 17f
            setTextColor(Color.WHITE)


            // Обработчик обычного клика
            setOnClickListener {
                if (onClick != null) {
                    onClick()
                } else {
                    handleKeyPress(key)
                }
            }

            // Обработчик долгого нажатия
            setOnLongClickListener { v ->
                when {
                    onClick == ::handleBackspace -> {
                        isBackspaceLongPress = true
                        backspaceLongPressWithCtrl = isCtrlPressed
                        handler.post(backspaceRunnable)
                        true
                    }

                    onClick == ::handleDelete -> {
                        isDelLongPress = true
                        delLongPressWithCtrl = isCtrlPressed
                        handler.post(delRunnable)
                        true
                    }

                    else -> false
                }
            }

            // Остановка долгого нажатия при отпускании кнопки
            setOnTouchListener { v, event ->
                when (event.action) {
                    MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                        isBackspaceLongPress = false
                        isDelLongPress = false
                        handler.removeCallbacks(backspaceRunnable)
                        handler.removeCallbacks(delRunnable)
                    }
                }
                false
            }
        }
        row.addView(button)
    }

    // Вспомогательная функция для кнопок-модификаторов
    private fun addModifierButton(              //Sht, Ctrl, Alt
        row: LinearLayout,
        text: String,
        weight: Float,
        onClick: () -> Unit,
        isActive: Boolean
    ) {
        val button = Button(this).apply {
            this.text = text
            tag = text
            layoutParams = LayoutParams(0, 150, weight).apply {
                setMargins(2, 2, 2, 2)
            }
            background = ContextCompat.getDrawable(
                context,
                if (isActive) R.drawable.rounded_button_gray
                else R.drawable.rounded_button_black
            )
            textSize = 16f
            setTextColor(Color.WHITE)
            setOnClickListener { onClick() }
        }
        row.addView(button)
    }

    private fun toggleModifier(modifier: String) {
        when (modifier) {
            "CTRL" -> isCtrlPressed = !isCtrlPressed
            "ALT" -> isAltPressed = !isAltPressed
        }
        updateKeyboardState()
    }

    private fun toggleLanguage() {
        isEnglishLayout = !isEnglishLayout
        updateKeyboardState()
    }

    private fun updateKeyboardState() {
        handler.post {
            keyboardView?.let { root ->
                val keysToUpdate = enLayout.keys + ruLayout.keys + setOf("EN")
                keysToUpdate.forEach { key ->
                    // Явное приведение типа к Button
                    val button = root.findViewWithTag(key) as? Button
                    button?.text = when (key) {
                        "EN" -> if (isEnglishLayout) "EN" else "RU"
                        else -> getKeyDisplayText(key)
                    }
                }

                updateModifierButton(root, "SH", isShiftPressed || isCapsLock)
                updateModifierButton(root, "CTRL", isCtrlPressed)
                updateModifierButton(root, "ALT", isAltPressed)
            }
        }
    }

    private fun updateModifierButton(root: View, text: String, isActive: Boolean) {
        val button = root.findViewWithTag(text) as? Button
        button?.apply {
            background = ContextCompat.getDrawable(
                context,
                if (isActive) R.drawable.rounded_button_gray
                else R.drawable.rounded_button_black
            )
        }
    }

    private fun handleShift() {
        when {
            isCapsLock && isShiftPressed -> {
                isCapsLock = false
                isShiftPressed = false
            }

            isShiftPressed -> {
                isCapsLock = true
            }
            else -> isShiftPressed = true
        }
        updateKeyboardState()
    }

    private fun handleKeyPress(key: String) {
        var movingCursor = false
        when {
            isCtrlPressed && key == "c" -> copyText()
            isCtrlPressed && key == "v" -> pasteText()
            isCtrlPressed && key == "x" -> cutText()
            //собственные комбинации | myself combinations
            isCtrlPressed && key == "a" -> selectAllText()
            isAltPressed && key == "t" -> rulangSendTextYo()
            isCtrlPressed && key == "t" -> sendText("\t")
            isCtrlPressed && key == "-" -> {
                moveCursorText(false)
                movingCursor = true
            }
            isCtrlPressed && key == "=" -> {
                moveCursorText(true)
                movingCursor = true
            }
            else -> {
                val charToSend = getKeyDisplayText(key)
                sendText(charToSend)
            }
        }

        //сброс ctrl и alt после комбинации
        if ((isCtrlPressed || isAltPressed) && !movingCursor) {
            resetModifiers()
            movingCursor = false
        }

        // Снимаем Shift после одного символа, если не включен CapsLock
        if (isShiftPressed && !isCapsLock) {
            isShiftPressed = false
            updateKeyboardState()
        }
    }

    private fun moveCursorText(moveForward: Boolean) {
        safeInputConnection { ic ->
            val textBefore = ic.getTextBeforeCursor(10000, 0)?.toString() ?: ""
            val currentPosition = textBefore.length
            val newPosition: Int

            if (moveForward) {
                val textAfter = ic.getTextAfterCursor(10000, 0)?.toString() ?: ""
                if (textAfter.isEmpty()) return@safeInputConnection
                newPosition = maxOf(0, currentPosition + 1)
            } else {
                if (currentPosition == 0) return@safeInputConnection
                newPosition = maxOf(0, currentPosition - 1)
            }

            ic.setSelection(newPosition, newPosition)
        }
    }

    private fun safeInputConnection(action: (InputConnection) -> Unit) {
        currentInputConnection?.let(action) ?: run {
            Log.w(TAG, "InputConnection in null")
        }
    }

    private fun copyText() {
        safeInputConnection { ic ->
            currentInputConnection?.performContextMenuAction(android.R.id.copy)
        }
    }

    private fun rulangSendTextYo() {
        if (isShiftPressed) sendText("Ё") else sendText("ё")
    }

    private fun pasteText() {
        safeInputConnection { ic ->
            ic.performContextMenuAction(android.R.id.paste)
        }
    }

    private fun cutText() {
        safeInputConnection { ic ->
            ic.performContextMenuAction(android.R.id.cut)
        }
    }

    private fun selectAllText() {
        safeInputConnection { ic ->
            ic.performContextMenuAction(android.R.id.selectAll)
        }
    }

    private fun handleBackspace() {
        val ic = currentInputConnection ?: return

        if (hasSelectedText()) {
            ic.commitText("", 1)
        } else {
            if (isCtrlPressed) {
                deleteWordBeforeCursor()
                if (!isBackspaceLongPress) {
                    resetModifiers()
                }
            } else {
                ic.deleteSurroundingText(1, 0)
            }
        }
    }

    private fun handleEnter() {
        safeInputConnection { ic ->
            ic.sendKeyEvent(KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_ENTER))
            ic.sendKeyEvent(KeyEvent(KeyEvent.ACTION_UP, KeyEvent.KEYCODE_ENTER))
        }
    }

    private fun sendText(text: String) {
        try {
            currentInputConnection?.commitText(text, 1)
        } catch (e: Exception) {
            Log.e(TAG, "Error sending text", e)
        }
    }

    override fun onStartInput(info: EditorInfo?, restarting: Boolean) {
        super.onStartInput(info, restarting)
        Log.d(TAG, "onStartInput called")
    }

    override fun onStartInputView(info: EditorInfo?, restarting: Boolean) {
        super.onStartInputView(info, restarting)
        Log.d(TAG, "onStartInputView called")
    }

    override fun onEvaluateInputViewShown(): Boolean {
        super.onEvaluateInputViewShown()
        Log.d(TAG, "onEvaluateInputViewShown called")
        return true
    }

    override fun onShowInputRequested(flags: Int, configChange: Boolean): Boolean {
        Log.d(TAG, "onShowInputRequested called")
        return true
    }

    private fun handleDelete() {
        val ic = currentInputConnection ?: return

        if (hasSelectedText()) {
            ic.commitText("", 1)
        } else {
            if (isCtrlPressed) {
                deleteWordAfterCursor()
                if (!isDelLongPress) {
                    resetModifiers()
                }
            } else {
                ic.deleteSurroundingText(0, 1)
            }
        }
    }

    private fun hasSelectedText(): Boolean {
        val ic = currentInputConnection ?: return false
        val selectedText = ic.getSelectedText(0)
        return selectedText != null && selectedText.isNotEmpty()
    }

    // Удаление слова перед курсором (Ctrl+Backspace)
    private fun deleteWordBeforeCursor() {
        val ic = currentInputConnection ?: return
        val textBeforeCursor = ic.getTextBeforeCursor(10000, 0)?.toString() ?: return
        val punctuation = "!\"#$%&'()*+,-./:;<=>?@[\\]^_`{|}~"

        if (textBeforeCursor.isEmpty()) return

        //find position for start deleting
        var startIndex = textBeforeCursor.length - 1
        var foundNonSpace = false

        //go at cursor to back
        while (startIndex >= 0) {
            if (!textBeforeCursor[startIndex].isWhitespace() && textBeforeCursor[startIndex] !in punctuation) {
                foundNonSpace = true
            } else if (foundNonSpace) {
                startIndex++
                break
            }
            startIndex--
        }

        //if go to start string
        if (startIndex < 0) startIndex = 0

        val charsToDelete = textBeforeCursor.length - startIndex
        if (charsToDelete > 0) {
            ic.deleteSurroundingText(charsToDelete, 0)
        }
    }

    // Удаление слова после курсора (Ctrl+Delete)
    private fun deleteWordAfterCursor() {
        val ic = currentInputConnection ?: return
        val textAfterCursor = ic.getTextAfterCursor(10000, 0)?.toString() ?: return
        val punctuation = "!\"#$%&'()*+,-./:;<=>?@[\\]^_`{|}~"

        if (textAfterCursor.isEmpty()) return

        //finding position to end deleting
        var endIndex = 0
        var foundNonSpace = false

        //go at cursor to forward
        while (endIndex < textAfterCursor.length) {
            if (!textAfterCursor[endIndex].isWhitespace() && textAfterCursor[endIndex] !in punctuation) {
                foundNonSpace = true
            } else if (foundNonSpace) {
                break
            }
            endIndex++
        }

        if (endIndex > 0) {
            ic.deleteSurroundingText(0, endIndex)
        }
    }
}
