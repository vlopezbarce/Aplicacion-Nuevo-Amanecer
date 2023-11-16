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
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.tec.nuevoamanecer.databinding.FragmentLoginBinding

class LoginFragment : Fragment() {
    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    // Firebase Authentication
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Initialize Firebase Auth
        auth = FirebaseAuth.getInstance()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnRegresar.setOnClickListener {
            Navigation.findNavController(view).navigate(R.id.action_loginFragment_to_mainFragment)
        }

        binding.btnSiguiente.setOnClickListener {
            loginUser()
        }

    }

    private fun loginUser() {
        val email = binding.editTextCorreo.text.toString()
        val password = binding.editTextContrasena.text.toString()

        // Validate email and password

        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val userUID = auth.currentUser?.uid.orEmpty()
                    checkUserTypeAndNavigate(userUID)
                } else {
                    // Handle errors, e.g., incorrect credentials
                }
            }
    }

    private fun checkUserTypeAndNavigate(userUID: String) {
        val database = FirebaseDatabase.getInstance().reference
        val typeRef = database.child("Usuarios").child("Usuario").child(userUID)

        typeRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {
                    val userType = dataSnapshot.getValue(String::class.java)

                    when (userType) {
                        "Alumno" -> {
                            val nivelRef = database.child("Usuarios").child("Alumnos").child(userUID).child("nivel")

                            nivelRef.addListenerForSingleValueEvent(object : ValueEventListener {
                                override fun onDataChange(snapshot: DataSnapshot) {
                                    val nivel = snapshot.getValue(String::class.java)?.toInt()

                                    when (nivel) {
                                        1 -> Navigation.findNavController(binding.root)
                                            .navigate(R.id.action_loginFragment_to_alumnoFragment)
                                        2 -> Navigation.findNavController(binding.root)
                                            .navigate(R.id.action_loginFragment_to_alumnoFragment2)
                                        3 -> Navigation.findNavController(binding.root)
                                            .navigate(R.id.action_loginFragment_to_alumnoFragment3)
                                        4 -> Navigation.findNavController(binding.root)
                                            .navigate(R.id.action_loginFragment_to_alumnoFragment4)
                                    }
                                }

                                override fun onCancelled(error: DatabaseError) {

                                }
                            })
                        }
                        "Terapeuta" -> Navigation.findNavController(binding.root)
                            .navigate(R.id.action_loginFragment_to_terapeutaFragment)
                        else -> {
                            throw Exception("Unexpected user type")
                        }
                    }
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {

            }
        })
    }


    companion object {
        @JvmStatic
        fun newInstance() =
            LoginFragment().apply {
                arguments = Bundle().apply {
                }
            }
    }
}