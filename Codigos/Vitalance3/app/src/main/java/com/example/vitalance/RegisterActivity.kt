package com.example.vitalence

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.widget.TextView

class RegisterActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val textView = TextView(this)
        textView.text = "Pantalla: Registro de usuario"
        setContentView(textView)
    }
}
