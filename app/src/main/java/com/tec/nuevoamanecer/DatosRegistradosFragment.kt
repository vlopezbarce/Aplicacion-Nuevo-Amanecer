package com.tec.nuevoamanecer

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.tec.nuevoamanecer.databinding.FragmentDatosRegistradosBinding

class DatosRegistradosFragment : Fragment() {
    private var _binding : FragmentDatosRegistradosBinding? = null
    private val binding get() = _binding!!

    private lateinit var database: DatabaseReference
    private lateinit var userRef: DatabaseReference
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
        _binding = FragmentDatosRegistradosBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        userRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    val alumno = snapshot.getValue(Alumno::class.java)
                    if (alumno != null) {
                        binding.txtViewNombre.text = alumno.nombre
                        binding.txtViewApellidos.text = alumno.apellidos
                        binding.txtViewFecha.text = alumno.fechaNacimiento
                        binding.txtViewNivel.text = alumno.nivel
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })

        val user = FirebaseAuth.getInstance().currentUser
        if (user != null) {
            binding.txtViewCorreo.text = user.email
        }

        binding.btnRegresar.setOnClickListener{
            Navigation.findNavController(view).navigate(R.id.action_datosRegistradosFragment_to_datosDeAlumnoFragment)
        }

        binding.btnSiguiente.setOnClickListener{
            Navigation.findNavController(view).navigate(R.id.action_datosRegistradosFragment_to_alumnoFragment)
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            DatosRegistradosFragment().apply {
                arguments = Bundle().apply {
                }
            }
    }
}