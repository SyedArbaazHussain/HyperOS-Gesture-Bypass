package com.arbaaz.hyperos.gesture.bypass

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.topjohnwu.privileged.Shell

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val btnApply = findViewById<Button>(R.id.btn_apply)
        val btnRevert = findViewById<Button>(R.id.btn_revert)
        val statusText = findViewById<TextView>(R.id.status_text)

        btnApply.setOnClickListener {
            executeRootCommands(listOf(
                "settings put global force_fsg_nav_bar 1",
                "cmd overlay enable com.android.internal.systemui.navbar.gestural"
            ), "Gestures Applied!")
        }

        btnRevert.setOnClickListener {
            executeRootCommands(listOf(
                "settings put global force_fsg_nav_bar 0",
                "cmd overlay disable com.android.internal.systemui.navbar.gestural"
            ), "Reverted to Buttons!")
        }
    }

    private fun executeRootCommands(commands: List<String>, successMsg: String) {
        val result = Shell.cmd(*commands.toTypedArray()).exec()
        if (result.isSuccess) {
            Toast.makeText(this, successMsg, Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, "Root Access Denied or Failed", Toast.LENGTH_LONG).show()
        }
    }
}