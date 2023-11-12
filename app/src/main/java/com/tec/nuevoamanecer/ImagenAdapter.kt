package com.tec.nuevoamanecer

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageButton
import android.widget.TextView
import com.google.firebase.Firebase
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.storage

class ImagenAdapter(context: Context, imagenes: List<Imagen>, private val userUID: String) :
    ArrayAdapter<Imagen>(context, 0, imagenes) {

    private lateinit var database: DatabaseReference
    private var storageRef = Firebase.storage

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view : View =
            convertView ?: LayoutInflater.from(context).inflate(R.layout.item_imagen, parent, false)

        val imagen = getItem(position)
        val txtViewNombre = view.findViewById<TextView>(R.id.txtViewNombre)
        txtViewNombre.text = imagen?.nombre

        database = FirebaseDatabase.getInstance().reference
        storageRef = FirebaseStorage.getInstance()

        val btnEliminar = view.findViewById<ImageButton>(R.id.btnEliminar)
        btnEliminar.setOnClickListener {
            if (imagen != null) {
                database.child("Usuarios").child("Tablero").child(userUID).child(imagen.id).removeValue()
                storageRef.getReference("Tablero/$userUID/${imagen.nombre}").delete()
            }
        }

        return view
    }
}