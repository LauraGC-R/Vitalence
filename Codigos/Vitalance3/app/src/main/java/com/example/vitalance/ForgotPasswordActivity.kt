package com.example.vitalence

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.widget.TextView

class ForgotPasswordActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val textView = TextView(this)
        textView.text = "Pantalla: Olvidé mi contraseña"
        setContentView(textView)
    }
}
