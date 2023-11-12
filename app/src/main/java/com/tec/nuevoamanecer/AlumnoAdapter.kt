package com.tec.nuevoamanecer

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.TextView
import androidx.navigation.Navigation


class AlumnoAdapter(context: Context, alumnos: List<Alumno>) :
    ArrayAdapter<Alumno>(context, 0, alumnos) {

    // private lateinit var database: DatabaseReference

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view : View =
            convertView ?: LayoutInflater.from(context).inflate(R.layout.item_alumno, parent, false)

        val alumno = getItem(position)
        val txtViewNombre = view.findViewById<TextView>(R.id.txtViewNombre)
        val nombre = alumno?.nombre + " " + alumno?.apellidos
        txtViewNombre.text = nombre

        val btnEditar = view.findViewById<Button>(R.id.btnEditar)
        btnEditar.setOnClickListener {
            val bundle = Bundle()
            bundle.putString("userUID", alumno?.id)
            Navigation.findNavController(view).navigate(R.id.action_terapeutaFragment_to_editaDatosAlumnoFragment, bundle)
        }

        // database = FirebaseDatabase.getInstance().reference

        // val btnEliminar = view.findViewById<ImageButton>(R.id.btnEliminar)
        // btnEliminar.setOnClickListener {
        //     if (alumno != null) {
        //         database.child("Usuarios").child("Alumnos").child(alumno.id).removeValue()
        //     }
        // }

        return view
    }
}