package com.example.aplicacionexamen

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.example.aplicacionexamen.GestorSQL

class CrearUniversidadActivity : AppCompatActivity() {
    private lateinit var gestorSQL: GestorSQL

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_crear_universidad)

        gestorSQL = GestorSQL(this)  // Instancia de GestorSQL

        val nombreEditText = findViewById<EditText>(R.id.NombreUniversidad)
        val acronimoEditText = findViewById<EditText>(R.id.AcronimoUniversidad)
        val guardarButton = findViewById<Button>(R.id.guardarUniversidad)

        guardarButton.setOnClickListener {
            val nombre = nombreEditText.text.toString().trim()
            val acronimo = acronimoEditText.text.toString().trim()

            // Guardar directamente en la base de datos
            val id = gestorSQL.addUniversidad(nombre, acronimo)
            if (id > 0) {
                setResult(RESULT_OK)  // Indica que el cliente fue creado con éxito
            } else {
                setResult(RESULT_CANCELED)  // Indica que hubo un error
            }
            finish()
        }
    }
}
