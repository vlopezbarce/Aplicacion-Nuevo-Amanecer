package com.tec.nuevoamanecer

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.tec.nuevoamanecer.databinding.FragmentAlumnotableroBinding

class AlumnoTableroFragment : Fragment() {
    private var _binding: FragmentAlumnotableroBinding? = null

    private val binding get() = _binding!!

    private lateinit var database: DatabaseReference
    private lateinit var storageRef : StorageReference
    private lateinit var imagenesRef : DatabaseReference
    private lateinit var userUID: String

    private lateinit var categoria: String
    private lateinit var imagenAdapter: ImagenAdapter
    private val imagenesList = mutableListOf<Imagen>()
    private lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        database = FirebaseDatabase.getInstance().reference
        storageRef = FirebaseStorage.getInstance().reference
        userUID = arguments?.getString("userUID").orEmpty()
        categoria = arguments?.getString("categoria").orEmpty()
        imagenesRef = database.child("Usuarios").child("Tablero").child(userUID).child(categoria)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAlumnotableroBinding.inflate(inflater, container, false)

        recyclerView = binding.recyclerViewImagenes
        recyclerView.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        imagenAdapter = ImagenAdapter(requireContext(), imagenesList)
        recyclerView.adapter = imagenAdapter

        cargarImagenes()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnRegresar.setOnClickListener {
            val bundle = Bundle()
            bundle.putString("userUID", userUID)
            Navigation.findNavController(view).navigate(R.id.action_alumnoTableroFragment_to_alumnoCategoriasFragment, bundle)
        }
    }

    private fun cargarImagenes() {
        val imagenesRef = database.child("Usuarios").child("Tablero").child(userUID).child(categoria)

        imagenesRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                imagenesList.clear()

                for (imagenSnapshot in dataSnapshot.children) {
                    imagenSnapshot?.let {
                        val id = it.child("id").getValue(String::class.java) ?: ""
                        val nombre = it.child("nombre").getValue(String::class.java) ?: ""
                        val path = it.child("url").getValue(String::class.java) ?: ""

                        if (it.key != "categoria") {
                            val imagen = Imagen(id, nombre, path)
                            imagenesList.add(imagen)
                        }
                    }
                }

                imagenAdapter.notifyDataSetChanged()
            }

            override fun onCancelled(databaseError: DatabaseError) {}
        })
    }
}