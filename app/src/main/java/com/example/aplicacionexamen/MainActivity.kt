package com.example.aplicacionexamen


import android.content.Intent
import android.os.Bundle
import android.text.InputType
import android.view.ContextMenu
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ListView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat



class MainActivity : AppCompatActivity() {
    private lateinit var listView: ListView
    private lateinit var adapter: ArrayAdapter<String>
    private val gestorSQL: GestorSQL = GestorSQL(this) // Instancia de la clase GestorSQL

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        listView = findViewById(R.id.listViewUniversidades)
        adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, loadUniversidades())
        listView.adapter = adapter

        val crearUniversidadButton = findViewById<Button>(R.id.crearUniversidad)
        crearUniversidadButton.setOnClickListener {
            val intent = Intent(this, CrearUniversidadActivity::class.java)
            startActivityForResult(intent, 1)  // Use request code to identify the result
        }

        registerForContextMenu(listView)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1 && resultCode == RESULT_OK) {
            adapter.clear()
            adapter.addAll(loadUniversidades())
            adapter.notifyDataSetChanged()
        }
    }

    private fun loadUniversidades(): MutableList<String> {
        return gestorSQL.getUniversidades().map { it.nombre + " " + it.acronimo }.toMutableList()
    }

    override fun onCreateContextMenu(menu: ContextMenu, v: View, menuInfo: ContextMenu.ContextMenuInfo?) {
        super.onCreateContextMenu(menu, v, menuInfo)
        menuInflater.inflate(R.menu.context_menu, menu)
    }

    override fun onContextItemSelected(item: MenuItem): Boolean {
        val info = item.menuInfo as AdapterView.AdapterContextMenuInfo
        when (item.itemId) {
            R.id.edit -> editUniversidad(info.position)
            R.id.delete -> {
                gestorSQL.deleteUniversidad(gestorSQL.getUniversidades()[info.position].id)
                updateListView()
            }
            R.id.view_carreras -> viewCarreras(info.position)
            else -> return super.onContextItemSelected(item)
        }
        return true
    }

    private fun editUniversidad(position: Int) {
        val client = gestorSQL.getUniversidades()[position]
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Editar Universidad")
        val input = EditText(this)
        input.inputType = InputType.TYPE_CLASS_TEXT
        input.setText(client.nombre + " " + client.acronimo)
        builder.setView(input)

        builder.setPositiveButton("Guardar") { dialog, which ->
            val parts = input.text.toString().split(" ")
            gestorSQL.updateUniversidad(client.id, parts[0], parts.getOrElse(1) { "" })
            updateListView()
        }
        builder.setNegativeButton("Cancelar") { dialog, which -> dialog.cancel() }

        builder.show()
    }

    private fun updateListView() {
        adapter.clear()
        adapter.addAll(loadUniversidades())
        adapter.notifyDataSetChanged()
    }

    private fun viewCarreras(position: Int) {
        val intent = Intent(this, CarrerasActivity::class.java)
        intent.putExtra("universidadId", gestorSQL.getUniversidades()[position].id)
        startActivity(intent)
    }
}
