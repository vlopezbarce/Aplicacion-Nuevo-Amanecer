package com.tec.nuevoamanecer

import android.app.DatePickerDialog
import android.os.Bundle
import android.text.Editable
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import com.google.firebase.database.DatabaseReference
import com.tec.nuevoamanecer.databinding.FragmentDatosDeAlumnoBinding
import com.google.firebase.database.FirebaseDatabase
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date

class DatosDeAlumnoFragment : Fragment() {
    private var _binding : FragmentDatosDeAlumnoBinding? = null
    private val binding get() = _binding!!

    private lateinit var database: DatabaseReference
    private lateinit var userUID: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        database = FirebaseDatabase.getInstance().reference
        userUID = arguments?.getString("userUID").orEmpty()
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

        binding.btnSiguiente.setOnClickListener {
            saveStudentData()
        }

        binding.editTextFecha.setOnClickListener {
            showDatePickerDialog()
        }
    }

    private fun saveStudentData() {
        val nombre = binding.editTextNombre.text.toString()
        val apellidos = binding.editTextApellidos.text.toString()
        val fecha_nacimiento = binding.editTextFecha.text.toString()
        val nivel = binding.editTextNivel.text.toString()

        val alumno = Alumno(userUID, nombre, apellidos, fecha_nacimiento, nivel)

        // Store data under 'Usuario > UID > Alumno'
        database.child("Usuarios").child("Usuario").child(userUID).setValue("Alumno")
        database.child("Usuarios").child("Alumnos").child(userUID).setValue(alumno)
            .addOnSuccessListener {
                // Handle success, e.g., navigate to next fragment
                val bundle = Bundle()
                bundle.putString("userUID", userUID)
                Navigation.findNavController(binding.root).navigate(R.id.action_datosDeAlumnoFragment_to_datosRegistradosFragment, bundle)
            }
            .addOnFailureListener {
                // Handle failure, e.g., show error message
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