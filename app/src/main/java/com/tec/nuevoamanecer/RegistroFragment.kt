package com.tec.nuevoamanecer

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import com.google.firebase.auth.FirebaseAuth
import com.tec.nuevoamanecer.databinding.FragmentRegistroBinding

class RegistroFragment : Fragment() {
    private var _binding : FragmentRegistroBinding? = null
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
        _binding = FragmentRegistroBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnRegresar.setOnClickListener{
            Navigation.findNavController(view).navigate(R.id.action_registroFragment_to_mainFragment)
        }

        binding.btnSiguiente.setOnClickListener{
            registerUser()
        }
    }

    private fun registerUser() {
        val email = binding.editTextCorreo.text.toString()
        val password = binding.editTextContrasena.text.toString()

        // Validate email and password (add your validation logic)
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val user = auth.currentUser
                    val bundle = Bundle()
                    bundle.putString("userUID", user?.uid) // Add UID to bundle

                    // Navigate to DatosDeAlumnoFragment with UID
                    Navigation.findNavController(binding.root).navigate(R.id.action_registroFragment_to_datosDeAlumnoFragment, bundle)
                } else {
                    // Handle errors
                }
            }
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