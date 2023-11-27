package com.tec.nuevoamanecer

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.ktx.storage
import com.google.firebase.ktx.Firebase

class CategoriaAdapter(private val context: Context, private val userUID: String, private val categorias: List<Categoria>) :
    RecyclerView.Adapter<CategoriaAdapter.CategoriaViewHolder>() {

    private val storage: FirebaseStorage = Firebase.storage

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoriaViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_categoria, parent, false)
        return CategoriaViewHolder(view)
    }

    override fun onBindViewHolder(holder: CategoriaViewHolder, position: Int) {
        val categoria = categorias[position]
        holder.bind(categoria)
    }

    override fun getItemCount(): Int {
        return categorias.size
    }

    inner class CategoriaViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val imgCategoria: ImageView = itemView.findViewById(R.id.imgCategoria)
        private val txtViewCategoria: TextView = itemView.findViewById(R.id.txtViewCategoria)
        private val btnEliminar: ImageButton = itemView.findViewById(R.id.btnEliminar)

        fun bind(categoria: Categoria) {
            txtViewCategoria.text = categoria.nombre

            val portadaRef = storage.reference.child("Tablero/$userUID/${categoria.nombre}/portada")

            portadaRef.listAll()
                .addOnSuccessListener { items ->
                    if (items.items.isNotEmpty()) {
                        val firstItem = items.items[0]

                        Glide.with(context)
                            .load(firstItem)
                            .placeholder(R.drawable.placeholder_image)
                            .error(R.drawable.error_image)
                            .into(imgCategoria)
                    }
                }
                .addOnFailureListener {}

            btnEliminar.setOnClickListener {
                notifyDataSetChanged()
            }
        }
    }
}