package com.tec.nuevoamanecer

import android.os.Bundle
import android.text.Editable
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.navigation.Navigation
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.tec.nuevoamanecer.databinding.FragmentEditaDatosAlumnoBinding

class EditaDatosAlumnoFragment : Fragment() {
    private var _binding : FragmentEditaDatosAlumnoBinding? = null
    private val binding get() = _binding!!

    private lateinit var database: DatabaseReference
    private lateinit var userRef : DatabaseReference
    private lateinit var userUID: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        database = FirebaseDatabase.getInstance().reference
        userUID = arguments?.getString("userUID").orEmpty()
        userRef = database.child("Usuarios").child("Alumnos").child(userUID)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentEditaDatosAlumnoBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        userRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    val alumno = snapshot.getValue(Alumno::class.java)
                    if (alumno != null) {
                        binding.editTextNombre.text = Editable.Factory.getInstance().newEditable(alumno.nombre)
                        binding.editTextApellidos.text = Editable.Factory.getInstance().newEditable(alumno.apellidos)
                        binding.editTextFecha.text = Editable.Factory.getInstance().newEditable(alumno.fechaNacimiento)
                        binding.editTextNivel.text = Editable.Factory.getInstance().newEditable(alumno.nivel)

                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })

        binding.btnRegresar.setOnClickListener{
            Navigation.findNavController(view).navigate(R.id.action_editaDatosAlumnoFragment_to_terapeutaFragment)
        }

        binding.btnSiguiente.setOnClickListener{
            val nombre = binding.editTextNombre.text.toString()
            val apellidos = binding.editTextApellidos.text.toString()
            val fechaNacimiento = binding.editTextFecha.text.toString()
            val nivel = binding.editTextNivel.text.toString()

            userRef.child("nombre").setValue(nombre)
            userRef.child("apellidos").setValue(apellidos)
            userRef.child("fechaNacimiento").setValue(fechaNacimiento)
            userRef.child("nivel").setValue(nivel)

            Navigation.findNavController(view).navigate(R.id.action_editaDatosAlumnoFragment_to_terapeutaFragment)
        }

        binding.btnFolder.setOnClickListener{
            val bundle = Bundle()
            bundle.putString("userUID", userUID)
            Navigation.findNavController(view).navigate(R.id.action_editaDatosAlumnoFragment_to_folderFragment, bundle)
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            EditaDatosAlumnoFragment().apply {
                arguments = Bundle().apply {
                }
            }
    }
}