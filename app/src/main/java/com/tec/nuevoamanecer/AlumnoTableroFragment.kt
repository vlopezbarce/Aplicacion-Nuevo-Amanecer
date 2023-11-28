package com.tec.nuevoamanecer

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.Spinner
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.tec.nuevoamanecer.databinding.FragmentAlumno4Binding

class AlumnoTableroFragment : Fragment() {
    private lateinit var database: DatabaseReference
    private lateinit var userRef: DatabaseReference
    private lateinit var auth: FirebaseAuth
    private lateinit var uidAlumno: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        database = FirebaseDatabase.getInstance().reference
        auth = FirebaseAuth.getInstance()
        uidAlumno = auth.currentUser?.uid.orEmpty()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_tableroalumno, container, false)
    }

    private lateinit var recyclerView: RecyclerView
    private lateinit var imageAdapter: ImageAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView = view.findViewById(R.id.carousel_recyclerview)
        imageAdapter = ImageAdapter()
        recyclerView.adapter = imageAdapter
        recyclerView.layoutManager = GridLayoutManager(context, 3,RecyclerView.VERTICAL, false)

        val spinner = view.findViewById<Spinner>(R.id.description_spinner)
        val categories = arrayOf("Familia", "Horario", "Escuela", "Dias de La semana", "Colores", "Clima", "Animales")
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, categories)
        spinner.adapter = adapter

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                val selectedCategory = parent.getItemAtPosition(position).toString()
                fetchImages(selectedCategory)
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }
    }

    private fun fetchImages(category: String) {
        val imagesRef = FirebaseDatabase.getInstance().getReference("Usuarios/Tablero").child(uidAlumno)

        imagesRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val urls = mutableListOf<String>()
                dataSnapshot.children.forEach { snapshot ->
                    val imageCategory = snapshot.child("Description").getValue(String::class.java)
                    if (imageCategory == category) {
                        val url = snapshot.child("url").getValue(String::class.java)
                        if (url != null && url.isNotBlank()) {
                            Log.d("AlumnoTableroFragment", "Adding URL: $url")
                            urls.add(url)
                        }
                    }
                }
                if (urls.isEmpty()) {
                    Log.d("AlumnoTableroFragment", "No URLs found for category: $category")
                }
                imageAdapter.setImages(urls)
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.e("AlumnoTableroFragment", "Database error: ${databaseError.message}")
            }
        })
    }


    class ImageAdapter : RecyclerView.Adapter<ImageAdapter.ImageViewHolder>() {
        var imageUrls = mutableListOf<String>()

        fun setImages(urls: List<String>) {
            imageUrls = urls.toMutableList()
            notifyDataSetChanged()
        }


        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
            val itemView = LayoutInflater.from(parent.context).inflate(R.layout.image_item, parent, false)
            return ImageViewHolder(itemView)
        }

        override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
            Glide.with(holder.itemView.context)
                .load(imageUrls[position])
                .into(holder.imageView)
        }


        override fun getItemCount(): Int = imageUrls.size


        class ImageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            val imageView: ImageView = itemView.findViewById(R.id.imageView)
        }

    }
}