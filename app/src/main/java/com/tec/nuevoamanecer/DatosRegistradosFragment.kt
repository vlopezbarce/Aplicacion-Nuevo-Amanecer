package com.tec.nuevoamanecer

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.tec.nuevoamanecer.databinding.FragmentDatosRegistradosBinding

class DatosRegistradosFragment : Fragment() {
    private var _binding : FragmentDatosRegistradosBinding? = null
    private val binding get() = _binding!!

    private lateinit var email: String
    private lateinit var password: String
    private lateinit var nombre: String
    private lateinit var apellidos: String
    private lateinit var fechaNacimiento: String
    private lateinit var nivel: String

    private lateinit var auth: FirebaseAuth
    private lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        email = arguments?.getString("email").orEmpty()
        password = arguments?.getString("password").orEmpty()
        nombre = arguments?.getString("nombre").orEmpty()
        apellidos = arguments?.getString("apellidos").orEmpty()
        fechaNacimiento = arguments?.getString("fechaNacimiento").orEmpty()
        nivel = arguments?.getString("nivel").orEmpty()

        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance().reference
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

        binding.txtViewNombre.text = nombre
        binding.txtViewApellidos.text = apellidos
        binding.txtViewFecha.text = fechaNacimiento
        binding.txtViewNivel.text = nivel
        binding.txtViewCorreo.text = email

        binding.btnRegresar.setOnClickListener{
            val bundle = Bundle()
            bundle.putString("email", email)
            bundle.putString("password", password)
            bundle.putString("nombre", nombre)
            bundle.putString("apellidos", apellidos)
            bundle.putString("fechaNacimiento", fechaNacimiento)
            bundle.putString("nivel", nivel)

            Navigation.findNavController(view).navigate(R.id.action_datosRegistradosFragment_to_datosDeAlumnoFragment, bundle)
        }

        binding.btnSiguiente.setOnClickListener{
            registerUser()
        }
    }

    private fun registerUser() {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val userUID = auth.currentUser?.uid
                    if (userUID != null) {
                        val alumno = Alumno(userUID, nombre, apellidos, fechaNacimiento, nivel)

                        database.child("Usuarios").child("Usuario").child(userUID).setValue("Alumno")
                        database.child("Usuarios").child("Alumnos").child(userUID).setValue(alumno)
                            .addOnSuccessListener {
                                when (nivel.toInt()) {
                                    1 -> Navigation.findNavController(binding.root)
                                        .navigate(R.id.action_datosRegistradosFragment_to_alumnoFragment)
                                    2 -> Navigation.findNavController(binding.root)
                                        .navigate(R.id.action_datosRegistradosFragment_to_alumnoFragment2)
                                    3 -> Navigation.findNavController(binding.root)
                                        .navigate(R.id.action_datosRegistradosFragment_to_alumnoFragment3)
                                    4 -> Navigation.findNavController(binding.root)
                                        .navigate(R.id.action_datosRegistradosFragment_to_alumnoFragment4)
                                }
                            }
                            .addOnFailureListener {
                            }
                    }
                }
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