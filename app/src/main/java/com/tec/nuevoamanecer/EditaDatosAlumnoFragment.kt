package com.tec.nuevoamanecer

import android.app.DatePickerDialog
import android.os.Bundle
import android.text.Editable
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.tec.nuevoamanecer.databinding.FragmentEditaDatosAlumnoBinding
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date

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
                        binding.editTextNombre.setText(alumno.nombre)
                        binding.editTextApellidos.setText(alumno.apellidos)
                        binding.editTextFecha.setText(alumno.fechaNacimiento)
                        binding.editTextNivel.setText(alumno.nivel)

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
            EditaDatosAlumnoFragment().apply {
                arguments = Bundle().apply {
                }
            }
    }
}