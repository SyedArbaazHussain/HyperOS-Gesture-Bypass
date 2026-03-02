package com.arbaaz.hyperos.gesture.bypass

import android.app.Activity
import android.os.Bundle
import android.widget.TextView
import android.view.Gravity
import android.graphics.Color

class MainActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        val tv = TextView(this).apply {
            text = "HyperOS Gesture Bypass\n\nStatus: Module Active\n\nEnsure 'System Framework' is enabled in LSPosed and Reboot."
            textSize = 18f
            setTextColor(Color.BLACK)
            gravity = Gravity.CENTER
            setPadding(60, 60, 60, 60)
        }
        
        setContentView(tv)
    }
}