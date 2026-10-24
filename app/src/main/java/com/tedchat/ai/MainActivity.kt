
package com.tedchat.ai

import android.app.Activity
import android.graphics.Color
import android.graphics.Typeface
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.content.Context
import android.widget.*

class MainActivity : Activity() {

    private val bg = Color.rgb(13, 17, 30)
    private val panel = Color.rgb(24, 30, 47)
    private val accent = Color.rgb(112, 92, 255)
    private val white = Color.rgb(245, 246, 255)
    private val muted = Color.rgb(157, 166, 190)

    private lateinit var messages: LinearLayout
    private lateinit var scroll: ScrollView
    private lateinit var input: EditText
    private lateinit var sendButton: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        window.statusBarColor = bg
        window.navigationBarColor = bg
        window.decorView.systemUiVisibility = 0

        val root = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setBackgroundColor(bg)
        }

        // Modern app header
        val header = LinearLayout(this).apply {
            orientation = LinearLayout.HORIZONTAL
            gravity = Gravity.CENTER_VERTICAL
            setPadding(20, 18, 20, 18)
            background = rounded(panel, 0)
        }

        val logo = TextView(this).apply {
            text = "✦"
            textSize = 27f
            gravity = Gravity.CENTER
            setTextColor(white)
            background = rounded(accent, 16)
        }
        header.addView(logo, LinearLayout.LayoutParams(48, 48))

        val titles = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(12, 0, 0, 0)
        }

        titles.addView(TextView(this).apply {
            text = "Ted Chat AI"
            textSize = 20f
            setTextColor(white)
            typeface = Typeface.DEFAULT_BOLD
        })

        titles.addView(TextView(this).apply {
            text = "Your AI assistant"
            textSize = 13f
            setTextColor(muted)
        })

        header.addView(titles, LinearLayout.LayoutParams(
            0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f
        ))

        header.addView(TextView(this).apply {
            text = "● Ready"
            textSize = 12f
            setTextColor(Color.rgb(104, 220, 165))
        })

        root.addView(header)

        // Chat area
        scroll = ScrollView(this).apply {
            isFillViewport = true
            clipToPadding = false
            setPadding(16, 18, 16, 18)
        }

        messages = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
        }

        scroll.addView(messages)
        root.addView(scroll, LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT, 0, 1f
        ))

        addMessage(
            "Hello! 👋 I'm Ted Chat AI.\n\nAsk me anything. I'm ready to help you learn, write, plan, and solve problems.",
            false
        )

        // Bottom message composer
        val composer = LinearLayout(this).apply {
            orientation = LinearLayout.HORIZONTAL
            gravity = Gravity.CENTER_VERTICAL
            setPadding(12, 12, 12, 12)
            background = rounded(panel, 0)
        }

        input = EditText(this).apply {
            hint = "Message Ted Chat AI..."
            setHintTextColor(muted)
            setTextColor(white)
            textSize = 15f
            setPadding(14, 12, 8, 12)
            background = rounded(bg, 18)
            maxLines = 4
            minLines = 1
            imeOptions = EditorInfo.IME_ACTION_SEND
            inputType = EditorInfo.TYPE_CLASS_TEXT or
                    EditorInfo.TYPE_TEXT_FLAG_CAP_SENTENCES or
                    EditorInfo.TYPE_TEXT_FLAG_MULTI_LINE
        }

        composer.addView(input, LinearLayout.LayoutParams(
            0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f
        ))

        sendButton = TextView(this).apply {
            text = "➤"
            textSize = 23f
            gravity = Gravity.CENTER
            setTextColor(white)
            background = rounded(accent, 18)
            setPadding(17, 10, 17, 10)
            isClickable = true
            isFocusable = true
        }

        val buttonParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.WRAP_CONTENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        ).apply { leftMargin = 10 }

        composer.addView(sendButton, buttonParams)
        root.addView(composer)

        setContentView(root)

        sendButton.setOnClickListener { sendMessage() }
        input.setOnEditorActionListener { _, action, event ->
            if (action == EditorInfo.IME_ACTION_SEND ||
                (event?.keyCode == android.view.KeyEvent.KEYCODE_ENTER &&
                 event.isShiftPressed.not() &&
                 event.action == android.view.KeyEvent.ACTION_DOWN)) {
                sendMessage()
                true
            } else {
                false
            }
        }
    }

    private fun sendMessage() {
        val text = input.text.toString().trim()
        if (text.isEmpty()) return

        addMessage(text, true)
        input.text.clear()

        val manager = getSystemService(Context.INPUT_METHOD_SERVICE)
                as InputMethodManager
        manager.hideSoftInputFromWindow(input.windowToken, 0)

        // Temporary reply until a real AI service is connected.
        addMessage(
            "Your message was received. Ted Chat AI still needs an AI service connection to generate intelligent answers.",
            false
        )
    }

    private fun addMessage(text: String, isUser: Boolean) {
        val row = LinearLayout(this).apply {
            orientation = LinearLayout.HORIZONTAL
            gravity = if (isUser) Gravity.END else Gravity.START
            setPadding(0, 5, 0, 5)
        }

        val bubble = TextView(this).apply {
            this.text = text
            textSize = 15f
            setTextColor(white)
            setPadding(16, 13, 16, 13)
            background = rounded(
                if (isUser) accent else panel,
                20
            )
            setTextIsSelectable(true)
            setLineSpacing(3f, 1.05f)
        }

        val params = LinearLayout.LayoutParams(
            (resources.displayMetrics.widthPixels * 0.78f).toInt(),
            LinearLayout.LayoutParams.WRAP_CONTENT
        )

        row.addView(bubble, params)
        messages.addView(row)

        scroll.post { scroll.fullScroll(View.FOCUS_DOWN) }
    }

    private fun rounded(color: Int, radius: Int): GradientDrawable {
        return GradientDrawable().apply {
            setColor(color)
            cornerRadius = radius * resources.displayMetrics.density
        }
    }
}
