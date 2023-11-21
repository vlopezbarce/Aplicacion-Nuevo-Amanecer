package com.tec.nuevoamanecer

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
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

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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

        if (email.isNotEmpty() && password.isNotEmpty()){
            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val userUID = auth.currentUser?.uid.orEmpty()
                        checkUserTypeAndNavigate(userUID)
                    } else {
                        val alertDialogBuilder = AlertDialog.Builder(requireContext())
                        val errorMessage = task.exception?.message.orEmpty()
                        when {
                            errorMessage.contains("blocked", ignoreCase = true) -> {
                                alertDialogBuilder.setTitle("Demasiados Intentos")
                                alertDialogBuilder.setMessage("Demasiados intentos fallidos. Por favor, intenta más tarde.")
                            }
                            else -> {
                                alertDialogBuilder.setTitle("Datos Incorrectos")
                                alertDialogBuilder.setMessage("Correo o contraseña no válido.")
                            }
                        }
                        alertDialogBuilder.setPositiveButton("OK") { dialog, _ ->
                            dialog.dismiss()
                        }
                        alertDialogBuilder.create().show()
                    }
                }
        } else {
            val alertDialogBuilder = AlertDialog.Builder(requireContext())
            alertDialogBuilder.setTitle("Datos Insuficientes")
            alertDialogBuilder.setMessage("Por favor, ingresa todos los campos requeridos.")
            alertDialogBuilder.setPositiveButton("OK") { dialog, _ ->
                dialog.dismiss()
            }
            alertDialogBuilder.create().show()
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

                                override fun onCancelled(error: DatabaseError) {}
                            })
                        }
                        "Terapeuta" -> Navigation.findNavController(binding.root).navigate(R.id.action_loginFragment_to_terapeutaFragment)
                    }
                } else {
                    val alertDialogBuilder = AlertDialog.Builder(requireContext())
                    alertDialogBuilder.setTitle("Error")
                    alertDialogBuilder.setMessage("Usuario no encontrado.")
                    alertDialogBuilder.setPositiveButton("OK") { dialog, _ ->
                        dialog.dismiss()
                    }
                    alertDialogBuilder.create().show()
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {}
        })
    }
}