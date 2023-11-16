package com.tec.nuevoamanecer

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.navigation.Navigation
import com.tec.nuevoamanecer.databinding.FragmentRegistroBinding

class RegistroFragment : Fragment() {
    private var _binding : FragmentRegistroBinding? = null
    private val binding get() = _binding!!

    private lateinit var email: String
    private lateinit var password: String
    private lateinit var nombre: String
    private lateinit var apellidos: String
    private lateinit var fechaNacimiento: String
    private lateinit var nivel: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        email = arguments?.getString("email").orEmpty()
        password = arguments?.getString("password").orEmpty()
        nombre = arguments?.getString("nombre").orEmpty()
        apellidos = arguments?.getString("apellidos").orEmpty()
        fechaNacimiento = arguments?.getString("fechaNacimiento").orEmpty()
        nivel = arguments?.getString("nivel").orEmpty()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentRegistroBinding.inflate(inflater,container,false)

        binding.editTextCorreo.setText(email)
        binding.editTextContrasena.setText(password)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnRegresar.setOnClickListener{
            Navigation.findNavController(view).navigate(R.id.action_registroFragment_to_mainFragment)
        }

        binding.btnSiguiente.setOnClickListener{
            email = binding.editTextCorreo.text.toString()
            password = binding.editTextContrasena.text.toString()

            if (email.isNotEmpty() && password.isNotEmpty()) {
                val bundle = Bundle()
                bundle.putString("email", email)
                bundle.putString("password", password)
                bundle.putString("nombre", nombre)
                bundle.putString("apellidos", apellidos)
                bundle.putString("fechaNacimiento", fechaNacimiento)
                bundle.putString("nivel", nivel)

                Navigation.findNavController(binding.root).navigate(R.id.action_registroFragment_to_datosDeAlumnoFragment, bundle)
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