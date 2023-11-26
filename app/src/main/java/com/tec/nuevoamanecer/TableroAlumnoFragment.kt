package com.tec.nuevoamanecer

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.GridLayout
import android.widget.ImageButton
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import java.util.UUID


class TableroAlumnoFragment : Fragment() {
    private lateinit var uidAlumno: String
    private lateinit var gridLayout: GridLayout
    private lateinit var carouselRecyclerView: RecyclerView
    private val carouselAdapter = CarouselAdapter()
    private lateinit var auth: FirebaseAuth

    private val databaseRef by lazy {
        FirebaseDatabase.getInstance().getReference("Usuarios").child("Tablero").child(uidAlumno)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = FirebaseAuth.getInstance()
        uidAlumno = auth.currentUser?.uid.orEmpty() // Getting the UID of the current user
        Toast.makeText(context, uidAlumno, Toast.LENGTH_SHORT).show()
    }


    
        override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
        ): View? {
            val view = inflater.inflate(R.layout.fragment_tableroalumno, container, false)
            gridLayout = view.findViewById(R.id.button_grid)
            carouselRecyclerView = view.findViewById(R.id.carousel_recyclerview)
            setupGridLayout()
            setupCarousel()
            return view
        }

        private fun setupGridLayout() {
            databaseRef.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    dataSnapshot.children.forEachIndexed { index, snapshot ->
                        if (index < gridLayout.childCount) {
                            val childView = gridLayout.getChildAt(index) as? ImageButton
                            val imageUrl = snapshot.child("url").value as String?
                            childView?.tag = snapshot.key
                            if (imageUrl != null) {
                                if (childView != null) {
                                    Glide.with(this@TableroAlumnoFragment)
                                        .load(imageUrl)
                                        .centerCrop()
                                        .into(childView)
                                }
                                carouselAdapter.addImage(childView?.drawable)
                            }
                        }
                    }
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    // Handle error
                }
            })
        }

        private fun setupCarousel() {
            carouselRecyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            carouselRecyclerView.adapter = carouselAdapter
        }

        class CarouselAdapter : RecyclerView.Adapter<CarouselAdapter.ViewHolder>() {
            private val images: MutableList<Drawable> = mutableListOf()

            fun addImage(drawable: Drawable?) {
                drawable?.let {
                    images.add(it)
                    notifyItemInserted(images.size - 1)
                }
            }

            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
                val imageView = ImageButton(parent.context)
                imageView.layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                )
                return ViewHolder(imageView)
            }

            override fun onBindViewHolder(holder: ViewHolder, position: Int) {
                holder.imageView.setImageDrawable(images[position])
            }

            override fun getItemCount(): Int = images.size

            class ViewHolder(val imageView: ImageButton) : RecyclerView.ViewHolder(imageView)
        }
    }

