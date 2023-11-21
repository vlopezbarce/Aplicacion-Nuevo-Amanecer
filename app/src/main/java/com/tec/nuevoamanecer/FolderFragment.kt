package com.tec.nuevoamanecer

import android.net.Uri
import android.os.Bundle
import android.provider.OpenableColumns
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.navigation.Navigation
import com.google.firebase.Firebase
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.storage
import com.tec.nuevoamanecer.databinding.FragmentFolderBinding

class FolderFragment : Fragment() {
    private var _binding: FragmentFolderBinding? = null
    private val binding get() = _binding!!

    private lateinit var database: DatabaseReference
    private lateinit var imagenesRef : DatabaseReference
    private var storageRef = Firebase.storage
    private lateinit var userUID: String
    private lateinit var uri: Uri

    private lateinit var imagenAdapter: ImagenAdapter
    private val imagenesList = mutableListOf<Imagen>()
    private lateinit var listViewImagenes: ListView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        database = FirebaseDatabase.getInstance().reference
        storageRef = FirebaseStorage.getInstance()
        userUID = arguments?.getString("userUID").orEmpty()
        imagenesRef = database.child("Usuarios").child("Tablero").child(userUID)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFolderBinding.inflate(inflater, container, false)

        listViewImagenes = binding.listViewImagenes
        imagenAdapter = ImagenAdapter(requireContext(), imagenesList, userUID)
        listViewImagenes.adapter = imagenAdapter

        cargarImagenes()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val imagenGaleria = registerForActivityResult(
            ActivityResultContracts.GetContent()
        ) { selectedUri ->
            if (selectedUri != null) {
                // Agregar imagen con su nombre y path a Firebase Storage
                val nombre = getFileName(selectedUri)
                val path = "Tablero/$userUID/$nombre"

                storageRef.getReference(path)
                    .putFile(selectedUri)
                    .addOnSuccessListener { task ->
                        task.metadata!!.reference!!.downloadUrl
                            .addOnSuccessListener {
                                // Agregar imagen con clave única a Usuario asociado en Realtime Database
                                val imagenId = database.push().key ?: return@addOnSuccessListener
                                val imagen = nombre?.let { it -> Imagen(imagenId, it, path) }

                                imagenesRef.child(imagenId).setValue(imagen)
                                    .addOnSuccessListener {
                                        Toast.makeText(requireContext(), "Imagen subida exitosamente", Toast.LENGTH_SHORT).show()
                                    }
                                    .addOnFailureListener {
                                        Toast.makeText(requireContext(), "Error al subir imagen", Toast.LENGTH_SHORT).show()
                                    }
                            }
                    }
            } else {
                Toast.makeText(requireContext(), "Por favor selecciona una imagen.", Toast.LENGTH_SHORT).show()
            }
        }

        binding.btnRegresar.setOnClickListener{
            val bundle = Bundle()
            bundle.putString("userUID", userUID)
            Navigation.findNavController(view).navigate(R.id.action_folderFragment_to_editaDatosAlumnoFragment, bundle)
        }

        binding.btnSubirImagen.setOnClickListener {
            // Abrir galería de imágenes
            imagenGaleria.launch("image/*")
        }
    }

    private fun getFileName(uri: Uri): String? {
        val contentResolver = requireContext().contentResolver
        val cursor = contentResolver.query(uri, null, null, null, null)

        return cursor.use { cursor ->
            cursor?.use {
                if (it.moveToFirst()) {
                    val displayNameColumn = it.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                    val displayName = it.getString(displayNameColumn)
                    displayName
                } else {
                    null
                }
            }
        }
    }

    private fun cargarImagenes() {
        imagenesRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                imagenesList.clear()

                for (imagenSnapshot in dataSnapshot.children) {
                    val imagen = imagenSnapshot.getValue(Imagen::class.java)
                    imagen?.let { imagenesList.add(it) }
                }
                imagenAdapter.notifyDataSetChanged()
            }

            override fun onCancelled(databaseError: DatabaseError) {}
        })
    }
}