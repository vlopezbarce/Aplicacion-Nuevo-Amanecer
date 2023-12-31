package com.tec.nuevoamanecer

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import androidx.navigation.Navigation
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.tec.nuevoamanecer.databinding.FragmentTerapeutaBinding

class TerapeutaFragment : Fragment() {
    private var _binding: FragmentTerapeutaBinding? = null
    private val binding get() = _binding!!

    private lateinit var database: DatabaseReference
    private lateinit var alumnosRef : DatabaseReference
    private lateinit var alumnoAdapter: AlumnoAdapter
    private val alumnosList = mutableListOf<Alumno>()
    private lateinit var listViewAlumnos: ListView

    private lateinit var userRef: DatabaseReference
    private lateinit var auth: FirebaseAuth
    private lateinit var userUID: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        database = FirebaseDatabase.getInstance().reference
        auth = FirebaseAuth.getInstance()
        userUID = auth.currentUser?.uid.orEmpty()
        alumnosRef = database.child("Usuarios").child("Alumnos")
        userRef = database.child("Usuarios").child("Terapeutas").child(userUID)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentTerapeutaBinding.inflate(inflater, container, false)

        userRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    val terapeuta = snapshot.getValue(Terapeuta::class.java)
                    if (terapeuta != null) {
                        binding.txtViewNombre.text = terapeuta.nombre
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {}
        })

        listViewAlumnos = binding.listViewAlumnos
        alumnoAdapter = AlumnoAdapter(requireContext(), alumnosList)
        listViewAlumnos.adapter = alumnoAdapter

        cargarAlumnos()

        alumnoAdapter.notifyDataSetChanged()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.editTextBuscar.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(text: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(text: CharSequence?, start: Int, before: Int, count: Int) {
                alumnoAdapter.filter.filter(text)
            }

            override fun afterTextChanged(text: Editable?) {}
        })

        binding.btnRegresar.setOnClickListener{
            FirebaseAuth.getInstance().signOut()
            Navigation.findNavController(view).navigate(R.id.action_terapeutaFragment_to_mainFragment)
        }
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

            override fun onCancelled(databaseError: DatabaseError) {}
        })
    }
}