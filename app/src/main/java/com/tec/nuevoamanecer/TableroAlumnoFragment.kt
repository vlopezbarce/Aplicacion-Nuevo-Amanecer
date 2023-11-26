package com.tec.nuevoamanecer

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class TableroAlumnoFragment : Fragment() {
    private lateinit var database: DatabaseReference
    private lateinit var userRef: DatabaseReference
    private lateinit var auth: FirebaseAuth
    private lateinit var uidAlumno: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        database = FirebaseDatabase.getInstance().reference
        auth = FirebaseAuth.getInstance()
        uidAlumno = auth.currentUser?.uid.orEmpty()
        Toast.makeText(context, uidAlumno, Toast.LENGTH_SHORT).show()
    }

    private lateinit var recyclerView: RecyclerView
    private lateinit var imageAdapter: ImageAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView = view.findViewById(R.id.carousel_recyclerview)
        imageAdapter = ImageAdapter()
        recyclerView.adapter = imageAdapter
        recyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)

        fetchImages()
    }

    private fun fetchImages() {
        // Replace "your_image_data_path" with the actual path where the image URLs are stored
        val imagesRef = FirebaseDatabase.getInstance().getReference("Usuarios").child("Tablero").child(uidAlumno)


        imagesRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val urls = mutableListOf<String>()
                dataSnapshot.children.forEach { snapshot ->
                    val url = snapshot.getValue(String::class.java)
                    url?.let { urls.add(it) }
                }
                imageAdapter.setImages(urls)
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Handle errors
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
            val imageView = ImageView(parent.context).apply {
                layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
                )
                scaleType = ImageView.ScaleType.CENTER_CROP
            }
            return ImageViewHolder(imageView)
        }

        override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
            Glide.with(holder.itemView.context)
                .load(imageUrls[position])
                .into(holder.imageView)
        }

        override fun getItemCount(): Int = imageUrls.size

        class ImageViewHolder(val imageView: ImageView) : RecyclerView.ViewHolder(imageView)
    }

}