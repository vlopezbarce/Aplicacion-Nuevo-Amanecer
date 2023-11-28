package com.tec.nuevoamanecer

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.activity.result.contract.ActivityResultContracts
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.tec.nuevoamanecer.databinding.FragmentImagenesBinding

class ImagenesFragment : Fragment() {
    private var _binding: FragmentImagenesBinding? = null
    private val binding get() = _binding!!

    private lateinit var database: DatabaseReference
    private lateinit var storageRef : StorageReference
    private lateinit var imagenesRef : DatabaseReference
    private lateinit var userUID: String
    private lateinit var uri: Uri

    private lateinit var categoria: String
    private lateinit var imagenAdapter: ImagenAdapter
    private val imagenesList = mutableListOf<Imagen>()
    private lateinit var recyclerViewImagenes: RecyclerView

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
        _binding = FragmentImagenesBinding.inflate(inflater, container, false)

        recyclerViewImagenes = binding.recyclerImagenes
        recyclerViewImagenes.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        imagenAdapter = ImagenAdapter(requireContext(), userUID, categoria, imagenesList)
        recyclerViewImagenes.adapter = imagenAdapter

        cargarImagenes()

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
                    .into(binding.btnImagen)

                uri = selectedUri
            } else {
                Toast.makeText(
                    requireContext(),
                    "Por favor selecciona una imagen.",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        binding.btnRegresar.setOnClickListener {
            val bundle = Bundle()
            bundle.putString("userUID", userUID)
            Navigation.findNavController(view).navigate(R.id.action_imagenesFragment_to_categoriasFragment, bundle)
        }

        binding.btnImagen.setOnClickListener {
            imagenGaleria.launch("image/*")
        }

        binding.btnSubir.setOnClickListener {
            val imagenNombre = binding.editTextImagen.text.toString().trim()

            if (imagenNombre.isNotEmpty() && uri != null) {
                val id = database.push().key ?: return@setOnClickListener
                val path = "Tablero/$userUID/$categoria/$id"

                storageRef.child(path)
                    .putFile(uri!!)
                    .addOnSuccessListener { task ->
                        task.metadata!!.reference!!.downloadUrl
                            .addOnSuccessListener { downloadUri ->
                                val imagen = Imagen(id, imagenNombre, downloadUri.toString())

                                if (id != null) {
                                    imagenesRef.child(id).setValue(imagen)
                                        .addOnSuccessListener {
                                            Toast.makeText(
                                                requireContext(),
                                                "Imagen subida exitosamente",
                                                Toast.LENGTH_SHORT
                                            ).show()

                                            cargarImagenes()
                                        }
                                        .addOnFailureListener {
                                            Toast.makeText(
                                                requireContext(),
                                                "Error al subir imagen",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                        }
                                }
                            }
                    }
            } else {
                Toast.makeText(
                    requireContext(),
                    "Por favor selecciona una imagen y proporciona un nombre.",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun cargarImagenes() {
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