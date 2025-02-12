package com.example.aplicacionexamen

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.ContextMenu
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity

class CarrerasActivity : AppCompatActivity() {
    private lateinit var listView: ListView
    private lateinit var adapter: ArrayAdapter<String>
    private val gestorSQL: GestorSQL = GestorSQL(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_carreras)

        listView = findViewById(R.id.CarrerasListView)
        adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, loadCarreras())
        listView.adapter = adapter
        registerForContextMenu(listView)

        val crearCarreraButton = findViewById<Button>(R.id.CrearCarreraBt)
        crearCarreraButton.setOnClickListener {
            val intent = Intent(this, CrearCarreraActivity::class.java)
            startActivityForResult(intent, 2)
        }

        val regresarButton = findViewById<Button>(R.id.RegresarBt)
        regresarButton.setOnClickListener {
            finish()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 2 && resultCode == RESULT_OK) {
            updateListView()
        }
    }

    private fun loadCarreras(): List<String> {
        return gestorSQL.getCarreras().map { it.detalle + " - " + it.ubicacion }
    }

    private fun updateListView() {
        adapter.clear()
        adapter.addAll(loadCarreras())
        adapter.notifyDataSetChanged()
    }

    override fun onCreateContextMenu(menu: ContextMenu, v: View, menuInfo: ContextMenu.ContextMenuInfo?) {
        super.onCreateContextMenu(menu, v, menuInfo)
        menuInflater.inflate(R.menu.carrera_context_menu, menu)
    }

    override fun onContextItemSelected(item: MenuItem): Boolean {
        val info = item.menuInfo as AdapterView.AdapterContextMenuInfo
        when (item.itemId) {
            R.id.edit -> editCarrera(info.position)
            R.id.delete -> {
                gestorSQL.deleteCarrera(gestorSQL.getCarreras()[info.position].id)
                updateListView()
            }
            R.id.map -> showMap(gestorSQL.getCarreras()[info.position].ubicacion)
            else -> return super.onContextItemSelected(item)
        }
        return true
    }

    private fun editCarrera(position: Int) {
        val carrera = gestorSQL.getCarreras()[position]
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Editar Carrera")

        val input = EditText(this)
        input.setText(carrera.detalle + " - " + carrera.ubicacion)
        builder.setView(input)

        builder.setPositiveButton("Guardar") { dialog, which ->
            val parts = input.text.toString().split(" - ")
            if (parts.size >= 2) {
                gestorSQL.updateCarrera(carrera.id, parts[0], parts[1])
                updateListView()
            }
        }

        builder.setNegativeButton("Cancelar") { dialog, which -> dialog.cancel() }

        builder.show()
    }

    private fun showMap(ubicacion: String) {
        val gmmIntentUri = Uri.parse("geo:0,0?q=${Uri.encode(ubicacion)}")
        val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
        mapIntent.setPackage("com.google.android.apps.maps")
        if (mapIntent.resolveActivity(packageManager) != null) {
            startActivity(mapIntent)
        } else {
            Toast.makeText(this, "Google Maps no está instalado.", Toast.LENGTH_SHORT).show()
        }
    }
}
