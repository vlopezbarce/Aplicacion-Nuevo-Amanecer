package com.tec.nuevoamanecer

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.tec.nuevoamanecer.databinding.FragmentAlumnocategoriasBinding

class AlumnoCategoriasFragment : Fragment() {
    private var _binding: FragmentAlumnocategoriasBinding? = null
    private val binding get() = _binding!!

    private lateinit var database: DatabaseReference
    private lateinit var categoriasRef : DatabaseReference
    private lateinit var userUID: String

    private lateinit var recyclerView: RecyclerView
    private val categoriasList = mutableListOf<Categoria>()
    private lateinit var categoriasAdapter: AlumnoCategoriaAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        database = FirebaseDatabase.getInstance().reference
        userUID = arguments?.getString("userUID").orEmpty()
        categoriasRef = database.child("Usuarios").child("Tablero").child(userUID)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentAlumnocategoriasBinding.inflate(inflater, container, false)

        recyclerView = binding.recyclerViewCategorias
        recyclerView.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        categoriasAdapter = AlumnoCategoriaAdapter(requireContext(), userUID, categoriasList)
        recyclerView.adapter = categoriasAdapter
        recyclerView.layoutManager = GridLayoutManager(context, 2,RecyclerView.HORIZONTAL, false)

        cargarCategorias()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnRegresar.setOnClickListener {
            val bundle = Bundle()
            bundle.putString("userUID", userUID)
            Navigation.findNavController(view).navigate(R.id.action_alumnoCategoriasFragment_to_alumnoFragment4, bundle)
        }
    }

    private fun cargarCategorias() {
        categoriasRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                categoriasList.clear()

                for (categoriaSnapshot in dataSnapshot.children) {
                    categoriaSnapshot?.let {
                        val key = it.key
                        val categoria = Categoria(key.orEmpty())
                        categoriasList.add(categoria)
                    }
                }

                categoriasAdapter.notifyDataSetChanged()
            }

            override fun onCancelled(databaseError: DatabaseError) {}
        })
    }
}