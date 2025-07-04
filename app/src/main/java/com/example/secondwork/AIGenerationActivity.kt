package com.example.secondwork

import android.graphics.Color
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.setPadding
import androidx.core.widget.NestedScrollView
import com.google.android.material.internal.ViewUtils.hideKeyboard

class AIGenerationActivity : AppCompatActivity() {

    // Объявляем переменные для доступа из всех методов
    private lateinit var editMessage: EditText
    private lateinit var scrollView: NestedScrollView
    private lateinit var messageContainer: LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_aigeneration)

        // Инициализация UI элементов
        editMessage = findViewById(R.id.editMessage)
        scrollView = findViewById(R.id.scrollView)
        messageContainer = findViewById(R.id.messageContainer)
        val btnSend: ImageButton = findViewById(R.id.btnSend)

        // Обработчик отправки сообщения
        btnSend.setOnClickListener {
            sendMessage()
        }

        // Обработчик фокуса для автоматической прокрутки
        editMessage.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                scrollView.postDelayed({
                    scrollView.smoothScrollTo(0, scrollView.bottom)
                }, 150)
            }
        }
    }

    private fun sendMessage() {
        val message = editMessage.text.toString().trim()
        if (message.isNotEmpty()) {
            // Добавляем сообщение пользователя
            addMessageToChat(message, true)

            // Очищаем поле ввода
            editMessage.text.clear()

            // Скрываем клавиатуру
            hideKeyboard()

            // Имитируем ответ бота (можно заменить на реальный API)
            simulateBotResponse(message)
        }
    }

    private fun addMessageToChat(text: String, isUser: Boolean) {
        // Создаем текстовое представление сообщения
        val messageView = TextView(this).apply {
            this.text = text
            setTextColor(Color.WHITE) // Всегда белый текст

            // Устанавливаем правильный фон
            background = if (isUser) {
                ContextCompat.getDrawable(this@AIGenerationActivity, R.drawable.message_user_bg)
            } else {
                ContextCompat.getDrawable(this@AIGenerationActivity, R.drawable.message_bot_bg)
            }

            textSize = 16f
            setPadding(32.dpToPx(), 16.dpToPx(), 32.dpToPx(), 16.dpToPx())

            // Параметры для красивого отображения
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            ).apply {
                // Правильное выравнивание через gravity
                gravity = if (isUser) Gravity.END else Gravity.START
                setMargins(16.dpToPx(), 8.dpToPx(), 16.dpToPx(), 8.dpToPx())
            }

            // Ограничение ширины сообщения
            maxWidth = (resources.displayMetrics.widthPixels * 0.8).toInt()
        }

        // Добавляем сообщение в контейнер
        messageContainer.addView(messageView)

        // Автопрокрутка к последнему сообщению
        scrollView.post {
            scrollView.fullScroll(View.FOCUS_DOWN)
        }
    }

    private fun simulateBotResponse(userMessage: String) {
        // Имитация задержки ответа бота
        scrollView.postDelayed({
            val botResponse = when {
                userMessage.contains("привет", ignoreCase = true) -> "Привет! Чем могу помочь?"
                userMessage.contains("как дела", ignoreCase = true) -> "У меня всё отлично, я же бот!"
                else -> "Я получил ваше сообщение: \"$userMessage\""
            }
            addMessageToChat(botResponse, false)
        }, 1000)
    }

    private fun hideKeyboard() {
        val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(editMessage.windowToken, 0)
    }

    // Функция для конвертации dp в px
    private fun Int.dpToPx(): Int = (this * resources.displayMetrics.density).toInt()
}
