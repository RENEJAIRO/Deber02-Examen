package com.example.aplicacionexamen

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity

class CrearCarreraActivity : AppCompatActivity() {
    private lateinit var gestorSQL: GestorSQL

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_crear_carrera)

        gestorSQL = GestorSQL(this)  // Instancia de GestorSQL

        val detalleEditText = findViewById<EditText>(R.id.crearCarrera)
        val ubicacionEditText = findViewById<EditText>(R.id.UbicacionCarrera)
        val guardarCarreraButton = findViewById<Button>(R.id.GuardarCarreraBt)
        val universidadId = intent.getIntExtra("universidadId", 0)  // Asumiendo que el clienteId es pasado a esta actividad

        guardarCarreraButton.setOnClickListener {
            val detalle = detalleEditText.text.toString().trim()
            val ubicacion = ubicacionEditText.text.toString().trim()

            // Guardar directamente en la base de datos
            val id = gestorSQL.addCarrera(detalle, ubicacion, universidadId)
            if (id > 0) {
                setResult(RESULT_OK)  // Indica que la factura fue creada con éxito
            } else {
                setResult(RESULT_CANCELED)  // Indica que hubo un error
            }
            finish()
        }
    }
}
