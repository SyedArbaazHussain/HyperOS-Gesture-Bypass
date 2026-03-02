package com.arbaaz.hyperos.gesture.bypass

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // Create a simple text view instead of loading a complex XML layout
        val textView = TextView(this)
        textView.text = "HyperOS Gesture Bypass is Active\nEnable in LSPosed and Reboot."
        textView.textSize = 20f
        textView.setPadding(50, 50, 50, 50)
        
        setContentView(textView)
    }
}