package com.tec.nuevoamanecer

import android.app.DatePickerDialog
import android.os.Bundle
import android.text.Editable
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.navigation.Navigation
import com.tec.nuevoamanecer.databinding.FragmentDatosDeAlumnoBinding
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date

class DatosDeAlumnoFragment : Fragment() {
    private var _binding : FragmentDatosDeAlumnoBinding? = null
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
        _binding = FragmentDatosDeAlumnoBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.editTextNombre.setText(nombre)
        binding.editTextApellidos.setText(apellidos)
        binding.editTextFecha.setText(fechaNacimiento)
        binding.editTextNivel.setText(nivel)

        binding.btnRegresar.setOnClickListener {
            nombre = binding.editTextNombre.text.toString()
            apellidos = binding.editTextApellidos.text.toString()
            fechaNacimiento = binding.editTextFecha.text.toString()
            nivel = binding.editTextNivel.text.toString()

            val bundle = Bundle()
            bundle.putString("email", email)
            bundle.putString("password", password)
            bundle.putString("nombre", nombre)
            bundle.putString("apellidos", apellidos)
            bundle.putString("fechaNacimiento", fechaNacimiento)
            bundle.putString("nivel", nivel)

            Navigation.findNavController(view).navigate(R.id.action_datosDeAlumnoFragment_to_registroFragment, bundle)
        }

        binding.btnSiguiente.setOnClickListener {
            nombre = binding.editTextNombre.text.toString()
            apellidos = binding.editTextApellidos.text.toString()
            fechaNacimiento = binding.editTextFecha.text.toString()
            nivel = binding.editTextNivel.text.toString()

            if (nombre.isNotEmpty() && apellidos.isNotEmpty() && fechaNacimiento.isNotEmpty() && nivel.isNotEmpty()) {
                val bundle = Bundle()
                bundle.putString("email", email)
                bundle.putString("password", password)
                bundle.putString("nombre", nombre)
                bundle.putString("apellidos", apellidos)
                bundle.putString("fechaNacimiento", fechaNacimiento)
                bundle.putString("nivel", nivel)

                Navigation.findNavController(view).navigate(R.id.action_datosDeAlumnoFragment_to_datosRegistradosFragment, bundle)
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

        binding.editTextFecha.setOnClickListener {
            showDatePickerDialog()
        }
    }

    private fun showDatePickerDialog() {
        val calendar = Calendar.getInstance()
        val currentYear = calendar.get(Calendar.YEAR)
        val currentMonth = calendar.get(Calendar.MONTH)
        val currentDay = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(
            requireContext(),
            { _, year, month, day ->
                val formattedDate = SimpleDateFormat("yyyy-MM-dd").format(Date(year - 1900, month, day))
                binding.editTextFecha.text = Editable.Factory.getInstance().newEditable(formattedDate)
            },
            currentYear,
            currentMonth,
            currentDay
        )

        datePickerDialog.datePicker.init(currentYear, currentMonth, currentDay, null)
        datePickerDialog.show()
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            DatosDeAlumnoFragment().apply {
                arguments = Bundle().apply {
                }
            }
    }
}