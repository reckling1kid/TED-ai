
package com.tedchat.ai

import android.app.Activity
import android.os.Bundle
import android.graphics.Color
import android.view.Gravity
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.TextView

class MainActivity : Activity() {

    private lateinit var messages: LinearLayout
    private lateinit var input: EditText
    private lateinit var scrollView: ScrollView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val root = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(20, 24, 20, 16)
            setBackgroundColor(Color.rgb(245, 247, 250))
        }

        val title = TextView(this).apply {
            text = "Ted Chat AI"
            textSize = 26f
            setTextColor(Color.rgb(30, 60, 120))
            gravity = Gravity.CENTER
            setPadding(0, 8, 0, 20)
        }

        root.addView(
            title,
            LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
        )

        scrollView = ScrollView(this)
        messages = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
        }
        scrollView.addView(messages)

        root.addView(
            scrollView,
            LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                0,
                1f
            )
        )

        addMessage("Ted Chat AI", "Hello! I'm Ted. How can I help you today?")

        val bottom = LinearLayout(this).apply {
            orientation = LinearLayout.HORIZONTAL
            gravity = Gravity.CENTER_VERTICAL
        }

        input = EditText(this).apply {
            hint = "Type your message..."
            textSize = 16f
            setSingleLine(true)
        }

        bottom.addView(
            input,
            LinearLayout.LayoutParams(
                0,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                1f
            )
        )

        val sendButton = Button(this).apply {
            text = "Send"
            setOnClickListener {
                val message = input.text.toString().trim()

                if (message.isNotEmpty()) {
                    addMessage("You", message)
                    input.text.clear()
                    addMessage(
                        "Ted Chat AI",
                        "Message received! The AI connection will be added next."
                    )
                }
            }
        }

        bottom.addView(
            sendButton,
            LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
        )

        root.addView(bottom)
        setContentView(root)
    }

    private fun addMessage(sender: String, message: String) {
        val textView = TextView(this).apply {
            text = "$sender:\n$message"
            textSize = 16f
            setTextColor(Color.rgb(35, 40, 50))
            setPadding(14, 12, 14, 12)
            setBackgroundColor(Color.WHITE)
        }

        val params = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        params.setMargins(0, 0, 0, 12)
        messages.addView(textView, params)

        scrollView.post {
            scrollView.fullScroll(ScrollView.FOCUS_DOWN)
        }
    }
}
