package com.tec.nuevoamanecer

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.Firebase
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.tec.nuevoamanecer.databinding.FragmentCategoriasBinding

class CategoriasFragment : Fragment() {
    private var _binding: FragmentCategoriasBinding? = null
    private val binding get() = _binding!!

    private lateinit var database: DatabaseReference
    private lateinit var categoriasRef : DatabaseReference
    private lateinit var storageRef : StorageReference
    private lateinit var userUID: String
    private lateinit var uri: Uri

    private lateinit var categoriaAdapter: CategoriaAdapter
    private val categoriasList = mutableListOf<Categoria>()
    private lateinit var recyclerViewCategorias: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        database = FirebaseDatabase.getInstance().reference
        storageRef = FirebaseStorage.getInstance().reference
        userUID = arguments?.getString("userUID").orEmpty()
        categoriasRef = database.child("Usuarios").child("Tablero").child(userUID)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCategoriasBinding.inflate(inflater, container, false)

        recyclerViewCategorias = binding.scrollView.findViewById(R.id.listaCategorias)
        categoriaAdapter = CategoriaAdapter(requireContext(), userUID, categoriasList)
        recyclerViewCategorias.adapter = categoriaAdapter

        cargarCategorias()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val imagenGaleria = registerForActivityResult(
            ActivityResultContracts.GetContent()
        ) { selectedUri ->
            if (selectedUri != null) {
                Glide.with(requireContext())
                    .load(selectedUri)
                    .into(binding.btnCategoria)

                uri = selectedUri
            } else {
                Toast.makeText(requireContext(), "Por favor selecciona una imagen.", Toast.LENGTH_SHORT).show()
            }
        }

        binding.btnRegresar.setOnClickListener{
            val bundle = Bundle()
            bundle.putString("userUID", userUID)
            Navigation.findNavController(view).navigate(R.id.action_categoriasFragment_to_editaDatosAlumnoFragment, bundle)
        }

        binding.btnCategoria.setOnClickListener {
            imagenGaleria.launch("image/*")
        }

        binding.btnSubir.setOnClickListener {
            val categoriaNombre = binding.editTextCategoria.text.toString().trim()

            if (categoriaNombre.isNotEmpty() && uri != null) {
                val path = "Tablero/$userUID/$categoriaNombre/portada"

                storageRef.child(path)
                    .putFile(uri!!)
                    .addOnSuccessListener { task ->
                        task.metadata!!.reference!!.downloadUrl
                            .addOnSuccessListener { imageUri ->
                                categoriasRef.setValue(categoriaNombre)
                                    .addOnSuccessListener {
                                        Toast.makeText(
                                            requireContext(),
                                            "Imagen y categoría subidas exitosamente",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                    .addOnFailureListener {
                                        Toast.makeText(
                                            requireContext(),
                                            "Error al subir imagen y categoría",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                            }
                    }
            } else {
                Toast.makeText(requireContext(), "Por favor selecciona una imagen y proporciona un nombre de categoría.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun cargarCategorias() {
        categoriasRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                categoriasList.clear()

                for (categoriaSnapshot in dataSnapshot.children) {
                    val categoria = Categoria(categoriaSnapshot.key.orEmpty())
                    categoriasList.add(categoria)
                }
                categoriaAdapter.notifyDataSetChanged()
            }

            override fun onCancelled(databaseError: DatabaseError) {}
        })
    }
}