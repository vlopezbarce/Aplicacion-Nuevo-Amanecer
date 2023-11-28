package com.tec.nuevoamanecer

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

class CategoriaAdapter(private val context: Context, private val userUID: String, private val categorias: List<Categoria>) :
    RecyclerView.Adapter<CategoriaAdapter.CategoriaViewHolder>() {

    private val database: DatabaseReference = FirebaseDatabase.getInstance().reference
    private val storage: StorageReference = FirebaseStorage.getInstance().reference

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
        private val btnCategoria: ImageView = itemView.findViewById(R.id.btnCategoria)
        private val txtViewCategoria: TextView = itemView.findViewById(R.id.txtViewCategoria)
        private val btnEliminar: ImageButton = itemView.findViewById(R.id.btnEliminar)

        fun bind(categoria: Categoria) {
            txtViewCategoria.text = categoria.nombre

            val portadaRef = storage.child("Tablero/$userUID/${categoria.nombre}/portada")

            portadaRef.downloadUrl.addOnSuccessListener { uri ->
                val imageUrl = uri.toString()

                Glide.with(context)
                    .load(imageUrl)
                    .placeholder(R.drawable.placeholder_image)
                    .error(R.drawable.error_image)
                    .into(btnCategoria)
            }.addOnFailureListener {
                Glide.with(context)
                    .load(R.drawable.error_image)
                    .into(btnCategoria)
            }

            btnCategoria.setOnClickListener {
                val bundle = Bundle()
                bundle.putString("userUID", userUID)
                bundle.putString("categoria", categoria.nombre)
                Navigation.findNavController(itemView).navigate(R.id.action_categoriasFragment_to_tableroFragment, bundle)
            }

            btnEliminar.setOnClickListener{
                val storageRef = storage.child("Tablero/$userUID/${categoria.nombre}")

                storageRef.listAll().addOnSuccessListener { result ->
                    if (result.items.isNotEmpty()) {
                        for (item in result.items) {
                            item.delete().addOnSuccessListener {
                                val categoriaRef = database.child("Usuarios").child("Tablero").child(userUID)
                                categoriaRef.child(categoria.nombre).removeValue().addOnSuccessListener {
                                    notifyDataSetChanged()
                                }.addOnFailureListener {}
                            }.addOnFailureListener {}
                        }
                    }
                }.addOnFailureListener {}
            }
        }
    }
}