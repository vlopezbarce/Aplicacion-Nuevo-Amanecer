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
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.tec.nuevoamanecer.databinding.FragmentAlumnoBinding

class AlumnoFragment : Fragment() {
    private var _binding: FragmentAlumnoBinding? = null
    private val binding get() = _binding!!

    private lateinit var database: DatabaseReference
    private lateinit var userRef: DatabaseReference
    private lateinit var auth: FirebaseAuth
    private lateinit var userUID: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        database = FirebaseDatabase.getInstance().reference
        auth = FirebaseAuth.getInstance()
        userUID = auth.currentUser?.uid.orEmpty()
        userRef = database.child("Usuarios").child("Alumnos").child(userUID)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAlumnoBinding.inflate(inflater, container, false)

        userRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    val alumno = snapshot.getValue(Alumno::class.java)
                    if (alumno != null) {
                        binding.txtViewNombre.text = alumno.nombre
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {}
        })

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnRegresar.setOnClickListener{
            FirebaseAuth.getInstance().signOut()
            Navigation.findNavController(view).navigate(R.id.action_alumnoFragment_to_mainFragment)
        }

        val botones = listOf(
            binding.btnJuego1, binding.btnJuego2
        )

        botones.forEachIndexed { index, btnJuego ->
            btnJuego.setOnClickListener {
                val actionId = when (index) {
                    0 -> R.id.action_alumnoFragment_to_game1Fragment
                    1 -> R.id.action_alumnoFragment_to_game2Fragment
                    else -> R.id.action_alumnoFragment_to_mainFragment
                }
                Navigation.findNavController(view).navigate(actionId)
            }
        }
    }
}