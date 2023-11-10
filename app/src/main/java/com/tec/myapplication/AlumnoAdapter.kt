package com.tec.myapplication

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.TextView
import androidx.navigation.Navigation

class AlumnoAdapter(context: Context, alumnos: List<Alumno>) :
    ArrayAdapter<Alumno>(context, 0, alumnos) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view : View =
            convertView ?: LayoutInflater.from(context).inflate(R.layout.item_alumno, parent, false)

        val alumno = getItem(position)
        val txtViewNombre = view.findViewById<TextView>(R.id.txtViewNombre)
        txtViewNombre.text = alumno?.nombre

        val btnEditar = view.findViewById<Button>(R.id.btnEditar)
        btnEditar.setOnClickListener {
            Navigation.findNavController(view).navigate(R.id.action_terapeutaFragment_to_editaDatosAlumnoFragment)
        }

        return view
    }
}