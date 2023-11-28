package com.tec.nuevoamanecer

import android.content.Context
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Filter
import android.widget.Filterable
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

class AlumnoAdapter(context: Context, private val alumnos: List<Alumno>) :
    ArrayAdapter<Alumno>(context, 0, alumnos), Filterable {

    private lateinit var database: DatabaseReference
    private lateinit var storage: StorageReference
    private var busquedaAlumnos: List<Alumno> = alumnos

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

        database = FirebaseDatabase.getInstance().reference
        storage = FirebaseStorage.getInstance().reference

        val btnEliminar = view.findViewById<ImageButton>(R.id.btnEliminar)
        btnEliminar.setOnClickListener {
            if (alumno != null) {
                database.child("Usuarios").child("Alumnos").child(alumno.id).removeValue()
                database.child("Usuarios").child("Usuario").child(alumno.id).removeValue()
                database.child("Usuarios").child("Tablero").child(alumno.id).removeValue()
            }
        }

        return view
    }

    override fun getCount(): Int {
        return busquedaAlumnos.size
    }

    override fun getItem(position: Int): Alumno? {
        return busquedaAlumnos[position]
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(busqueda: CharSequence?): FilterResults {
                val resultados = FilterResults()
                val listaBusqueda = mutableListOf<Alumno>()

                if (busqueda.isNullOrBlank()) {
                    listaBusqueda.addAll(alumnos)
                } else {
                    for (alumno in alumnos) {
                        val nombre = "${alumno.nombre} ${alumno.apellidos}".lowercase()
                        if (nombre.contains(busqueda.toString().lowercase().trim())) {
                            listaBusqueda.add(alumno)
                        }
                    }
                }

                resultados.values = listaBusqueda
                resultados.count = listaBusqueda.size
                return resultados
            }

            override fun publishResults(busqueda: CharSequence?, resultados: FilterResults?) {
                busquedaAlumnos = resultados?.values as? List<Alumno> ?: emptyList()
                notifyDataSetChanged()
            }
        }
    }
}