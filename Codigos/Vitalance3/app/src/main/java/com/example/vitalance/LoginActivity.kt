package com.example.vitalence

import android.content.Intent
import android.content.res.ColorStateList
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Patterns
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.google.android.material.button.MaterialButton
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputLayout

class LoginActivity : AppCompatActivity() {

    private lateinit var tilEmail: TextInputLayout
    private lateinit var tilPass: TextInputLayout
    private lateinit var btnLogin: MaterialButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        tilEmail = findViewById(R.id.tilEmail)
        tilPass  = findViewById(R.id.tilPass)
        btnLogin = findViewById(R.id.btnLogin)

        findViewById<MaterialButton>(R.id.btnForgot).setOnClickListener {
            startActivity(Intent(this, ForgotPasswordActivity::class.java))
        }
        findViewById<MaterialButton>(R.id.btnRegister).setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }
        findViewById<android.widget.TextView>(R.id.tvNoAccount).setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }
        btnLogin.setOnClickListener { doLogin() }

        val watcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun afterTextChanged(s: Editable?) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                validate()
            }
        }
        tilEmail.editText?.addTextChangedListener(watcher)
        tilPass.editText?.addTextChangedListener(watcher)

        validate()
    }

    private fun validate() {
        val email = tilEmail.editText?.text?.toString()?.trim().orEmpty()
        val pass  = tilPass.editText?.text?.toString().orEmpty()

        val okEmail = Patterns.EMAIL_ADDRESS.matcher(email).matches()
        tilEmail.error = if (email.isEmpty() || okEmail) null else "Correo inválido"
        tilEmail.helperText = when {
            email.isEmpty()      -> null
            okEmail              -> "Correo válido ✅"
            else                 -> "Ejemplo: usuario@dominio.com"
        }
        val helperColor = ContextCompat.getColor(this,
            if (okEmail) R.color.vitalence_red else R.color.text_secondary)
        tilEmail.setHelperTextColor(ColorStateList.valueOf(helperColor))

        val hasLen  = pass.length >= 8
        val hasUp   = pass.any { it.isUpperCase() }
        val hasLow  = pass.any { it.isLowerCase() }
        val hasNum  = pass.any { it.isDigit() }
        val hasSym  = pass.any { "!@#\$%^&*()-_=+[]{};:'\",.<>?/\\|`~".contains(it) }
        val okPass  = hasLen && hasUp && hasLow && hasNum && hasSym

        tilPass.error = null
        tilPass.helperText = when {
            pass.isEmpty() -> "Usa 8+ caracteres, mayúscula, número y símbolo"
            !hasLen -> "Añade más caracteres (mín. 8)"
            !hasUp  -> "Agrega una MAYÚSCULA"
            !hasLow -> "Agrega una minúscula"
            !hasNum -> "Agrega un número"
            !hasSym -> "Agrega un símbolo (ej. @ # ! ?)"
            else    -> "Contraseña segura ✅"
        }
        val passHelperColor = ContextCompat.getColor(this,
            if (okPass) R.color.vitalence_red else R.color.text_secondary)
        tilPass.setHelperTextColor(ColorStateList.valueOf(passHelperColor))

        btnLogin.isEnabled = okEmail && okPass
        btnLogin.alpha = if (btnLogin.isEnabled) 1f else 0.5f
    }

    private fun doLogin() {
        val snack = Snackbar.make(findViewById(android.R.id.content),
            "Inicio exitoso 🎉", Snackbar.LENGTH_LONG)
        snack.view.findViewById<android.widget.TextView>(
            com.google.android.material.R.id.snackbar_text
        )?.textSize = 18f
        snack.show()

        // Ir al Perfil (si así lo deseas)
        startActivity(Intent(this, ProfileActivity::class.java))
    }
}
