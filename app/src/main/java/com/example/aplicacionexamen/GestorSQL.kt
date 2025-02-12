package com.example.aplicacionexamen
import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.aplicacionexamen.Universidad
import com.example.aplicacionexamen.Carrera

class GestorSQL(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    companion object {
        private const val DATABASE_NAME = "AppDatabase1.db"
        private const val DATABASE_VERSION = 1

        private const val SQL_CREATE_TABLE_UNIVERSIDAD = """
            CREATE TABLE Universidad (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                nombre TEXT,
                acronimo TEXT
            )
        """

        private const val SQL_CREATE_TABLE_CARRERA = """
            CREATE TABLE Carrera (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                detalle TEXT,
                ubicacion TEXT,
                universidadId INTEGER,
                FOREIGN KEY(universidadId) REFERENCES universidad(id)
            )
        """
    }

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(SQL_CREATE_TABLE_UNIVERSIDAD)
        db.execSQL(SQL_CREATE_TABLE_CARRERA)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        // Aquí puedes manejar las actualizaciones de la base de datos
    }

    // CRUD Operaciones para Cliente
    fun addUniversidad(nombre: String, acronimo: String): Long {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put("nombre", nombre)
            put("acronimo", acronimo)
        }
        //return db.insert("Universidad", null, values)
        val result = db.insert("Universidad", null, values)
        db.close()  // Cierra la conexión
        return result
    }

    fun getUniversidades(): MutableList<Universidad> {
        val db = this.readableDatabase
        val projection = arrayOf("id", "nombre", "acronimo")
        val cursor = db.query("Universidad", projection, null, null, null, null, null)
        val universidades = mutableListOf<Universidad>()
        with(cursor) {
            while (moveToNext()) {
                val id = getInt(getColumnIndexOrThrow("id"))
                val nombre = getString(getColumnIndexOrThrow("nombre"))
                val acronimo = getString(getColumnIndexOrThrow("acronimo"))
                universidades.add(Universidad(id, nombre, acronimo))
            }
        }
        cursor.close()
        return universidades
    }

    fun updateUniversidad(id: Int, nombre: String, acronimo: String): Int {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put("nombre", nombre)
            put("acronimo", acronimo)
        }
        return db.update("Universidad", values, "id=?", arrayOf(id.toString()))
    }

    fun deleteUniversidad(id: Int): Int {
        val db = this.writableDatabase
        return db.delete("Universidad", "id=?", arrayOf(id.toString()))
    }

    // CRUD Operaciones para Factura
    fun addCarrera(detalle: String, ubicacion: String, universidadId: Int): Long {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put("detalle", detalle)
            put("ubicacion", ubicacion)
            put("universidadId", universidadId)
        }
        return db.insert("Carrera", null, values)
    }

    fun getCarreras(): MutableList<Carrera> {
        val db = this.readableDatabase
        val projection = arrayOf("id", "detalle", "ubicacion", "universidadId")
        val cursor = db.query("Carrera", projection, null, null, null, null, null)
        val carreras = mutableListOf<Carrera>()
        with(cursor) {
            while (moveToNext()) {
                val id = getInt(getColumnIndexOrThrow("id"))
                val detalle = getString(getColumnIndexOrThrow("detalle"))
                val ubicacion = getString(getColumnIndexOrThrow("ubicacion"))
                val universidadId = getInt(getColumnIndexOrThrow("universidadId"))
                carreras.add(Carrera(id, detalle, ubicacion, universidadId))
            }
        }
        cursor.close()
        return carreras
    }

    fun updateCarrera(id: Int, detalle: String, ubicacion: String): Int {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put("detalle", detalle)
            put("ubicacion", ubicacion)

        }
        return db.update("Carrera", values, "id=?", arrayOf(id.toString()))
    }

    fun deleteCarrera(id: Int): Int {
        val db = this.writableDatabase
        return db.delete("Carrera", "id=?", arrayOf(id.toString()))
    }
}
