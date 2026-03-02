package com.arbaaz.hyperos.gesture.bypass

import android.app.Activity
import android.os.Bundle
import android.widget.LinearLayout
import android.widget.TextView
import android.graphics.Color
import android.view.Gravity

class MainActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        val layout = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            gravity = Gravity.CENTER
            setBackgroundColor(Color.WHITE)
        }

        val textView = TextView(this).apply {
            text = "HyperOS Gesture Bypass\n\nStatus: Build Successful\n\nEnable in LSPosed and Reboot."
            textSize = 20f
            setTextColor(Color.BLACK)
            gravity = Gravity.CENTER
        }
        
        layout.addView(textView)
        setContentView(layout)
    }
}