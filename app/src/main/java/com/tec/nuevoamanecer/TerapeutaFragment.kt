package com.tec.nuevoamanecer

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import androidx.navigation.Navigation
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.tec.nuevoamanecer.databinding.FragmentTerapeutaBinding

class TerapeutaFragment : Fragment() {
    private var _binding : FragmentTerapeutaBinding? = null
    private val binding get() = _binding!!

    private lateinit var database: DatabaseReference
    private lateinit var alumnosRef : DatabaseReference
    private lateinit var alumnoAdapter: AlumnoAdapter
    private val alumnosList = mutableListOf<Alumno>()
    private lateinit var listViewAlumnos: ListView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        database = FirebaseDatabase.getInstance().reference
        alumnosRef = database.child("Usuarios").child("Alumnos")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentTerapeutaBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnRegresar.setOnClickListener{
            Navigation.findNavController(view).navigate(R.id.action_terapeutaFragment_to_mainFragment)
        }

        listViewAlumnos = view.findViewById(R.id.listViewAlumnos)
        alumnoAdapter = AlumnoAdapter(requireContext(), alumnosList)
        listViewAlumnos.adapter = alumnoAdapter

        cargarAlumnos()

        alumnoAdapter.notifyDataSetChanged()
    }

    private fun cargarAlumnos() {
        alumnosRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                alumnosList.clear()

                for (alumnoSnapshot in dataSnapshot.children) {
                    val alumno = alumnoSnapshot.getValue(Alumno::class.java)
                    alumno?.let { alumnosList.add(it) }
                }
                alumnoAdapter.notifyDataSetChanged()
            }

            override fun onCancelled(databaseError: DatabaseError) {

            }
        })
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            TerapeutaFragment().apply {
                arguments = Bundle().apply {
                }
            }
    }
}