package com.tec.nuevoamanecer

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
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

        binding.editTextCorreo.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(charSequence: CharSequence?, start: Int, before: Int, count: Int) {
                val email = charSequence?.toString() ?: ""
                if (isValidEmail(email)) {
                    binding.editTextCorreo.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.icono_check, 0)
                } else {
                    binding.editTextCorreo.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.icono_x, 0)
                }
            }

            override fun afterTextChanged(editable: Editable?) {
            }
        })

        binding.editTextContrasena.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(charSequence: CharSequence?, start: Int, before: Int, count: Int) {
                val password = charSequence?.toString() ?: ""
                if (isValidPassword(password)) {
                    binding.editTextContrasena.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.icono_check, 0)
                } else {
                    binding.editTextContrasena.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.icono_x, 0)
                }
            }

            override fun afterTextChanged(editable: Editable?) {
            }
        })

        binding.btnRegresar.setOnClickListener{
            Navigation.findNavController(view).navigate(R.id.action_registroFragment_to_mainFragment)
        }

        binding.btnSiguiente.setOnClickListener{
            email = binding.editTextCorreo.text.toString()
            password = binding.editTextContrasena.text.toString()

            if (isValidEmail(email) && isValidPassword(password)) {
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
                alertDialogBuilder.setTitle("Correo o Contraseña Inválidos")
                alertDialogBuilder.setMessage(
                    "La contraseña debe contener:\n" +
                            "- Al menos un dígito (0-9)\n" +
                            "- Al menos un carácter minúscula (a-z)\n" +
                            "- Al menos un carácter mayúscula (A-Z)\n" +
                            "- Al menos un carácter especial (@#$%@&+=!)\n" +
                            "- Mínimo 8 caracteres"
                )
                alertDialogBuilder.setPositiveButton("OK") { dialog, _ ->
                    dialog.dismiss()
                }
                alertDialogBuilder.create().show()
            }
        }
    }

    private fun isValidEmail(email: String): Boolean {
        val emailCheck = "[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}".toRegex()
        return emailCheck.matches(email)
    }

    private fun isValidPassword(password: String): Boolean {
        val passwordCheck = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=!])(?=\\S+$).{8,}$".toRegex()
        return passwordCheck.matches(password)
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