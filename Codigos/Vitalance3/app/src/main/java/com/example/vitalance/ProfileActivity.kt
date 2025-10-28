package com.example.vitalence

import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.MaterialAutoCompleteTextView
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.google.android.material.button.MaterialButton
import java.text.SimpleDateFormat
import java.util.*

class ProfileActivity : AppCompatActivity() {

    private lateinit var tilFecha: TextInputLayout
    private lateinit var etFecha: TextInputEditText
    private lateinit var actSexo: MaterialAutoCompleteTextView
    private lateinit var tilAltura: TextInputLayout
    private lateinit var tilPeso: TextInputLayout
    private lateinit var actEnfermedad: MaterialAutoCompleteTextView
    private lateinit var tilOtros: TextInputLayout
    private lateinit var btnGuardar: MaterialButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        tilFecha = findViewById(R.id.tilFecha)
        etFecha = findViewById(R.id.etFecha)
        actSexo = findViewById(R.id.actSexo)
        tilAltura = findViewById(R.id.tilAltura)
        tilPeso = findViewById(R.id.tilPeso)
        actEnfermedad = findViewById(R.id.actEnfermedad)
        tilOtros = findViewById(R.id.tilOtros)
        btnGuardar = findViewById(R.id.btnGuardar)

        // Dropdowns
        actSexo.setAdapter(ArrayAdapter(
            this,
            android.R.layout.simple_list_item_1,
            resources.getStringArray(R.array.sexo_options)
        ))
        val enfAdapter = ArrayAdapter(
            this,
            android.R.layout.simple_list_item_1,
            resources.getStringArray(R.array.enfermedades_options)
        )
        actEnfermedad.setAdapter(enfAdapter)
        actEnfermedad.setOnItemClickListener { _, _, _, _ ->
            val sel = actEnfermedad.text?.toString()?.trim().orEmpty()
            tilOtros.visibility = if (sel.equals("Otros", true)) View.VISIBLE else View.GONE
        }

        // Fecha con MaterialDatePicker (calendario grande con selección de año/mes)
        etFecha.setOnClickListener { showDatePicker() }
        tilFecha.setStartIconOnClickListener { showDatePicker() }

        btnGuardar.setOnClickListener { guardar() }
    }

    private fun showDatePicker() {
        val picker = MaterialDatePicker.Builder.datePicker()
            .setTitleText("Selecciona tu fecha de nacimiento")
            .setSelection(MaterialDatePicker.thisMonthInUtcMilliseconds())
            .build()

        picker.addOnPositiveButtonClickListener { utcMillis ->
            val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
            etFecha.setText(sdf.format(Date(utcMillis)))
        }
        picker.show(supportFragmentManager, "fecha_nac")
    }

    private fun guardar() {
        val fechaOk = !etFecha.text.isNullOrBlank()
        val sexoOk = !actSexo.text.isNullOrBlank()

        val altStr = tilAltura.editText?.text?.toString()?.trim().orEmpty()
        val pesoStr = tilPeso.editText?.text?.toString()?.trim().orEmpty()
        val alturaOk = altStr.toIntOrNull()?.let { it in 80..250 } == true
        val pesoOk = pesoStr.toFloatOrNull()?.let { it in 20f..500f } == true

        val enfSel = actEnfermedad.text?.toString()?.trim().orEmpty()
        val enfOk = enfSel.isNotEmpty()
        val otrosOk = if (enfSel.equals("Otros", true))
            !tilOtros.editText?.text.isNullOrBlank() else true

        findViewById<TextInputLayout>(R.id.tilFecha).error = if (!fechaOk) "Elige una fecha" else null
        findViewById<TextInputLayout>(R.id.tilSexo).error = if (!sexoOk) "Selecciona el sexo" else null
        tilAltura.error = if (!alturaOk) "Altura inválida (80–250 cm)" else null
        tilPeso.error   = if (!pesoOk) "Peso inválido (20–500 kg)" else null
        findViewById<TextInputLayout>(R.id.tilEnfermedad).error = if (!enfOk) "Selecciona una enfermedad" else null
        tilOtros.error  = if (!otrosOk) "Especifica la enfermedad" else null

        if (fechaOk && sexoOk && alturaOk && pesoOk && enfOk && otrosOk) {
            val snack = Snackbar.make(findViewById(android.R.id.content),
                "Perfil guardado ✅", Snackbar.LENGTH_LONG)
            snack.view.findViewById<android.widget.TextView>(
                com.google.android.material.R.id.snackbar_text
            )?.textSize = 18f
            snack.show()
        }
    }
}
