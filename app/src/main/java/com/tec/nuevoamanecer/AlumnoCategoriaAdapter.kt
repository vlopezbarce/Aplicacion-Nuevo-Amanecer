package com.tec.nuevoamanecer

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

class AlumnoCategoriaAdapter(private val context: Context, private val userUID: String, private val categorias: List<Categoria>, private val ttsList: MutableList<Imagen>, private val isFeminineVoice: Boolean) :
    RecyclerView.Adapter<AlumnoCategoriaAdapter.CategoriaViewHolder>() {

    private val storage: StorageReference = FirebaseStorage.getInstance().reference

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoriaViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_imagen, parent, false)
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
        private val btnCategoria: ImageView = itemView.findViewById(R.id.btnImagen)

        fun bind(categoria: Categoria) {
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
                bundle.putParcelableArrayList("ttsList", ArrayList(ttsList))
                bundle.putBoolean("isFeminineVoice", isFeminineVoice)
                Navigation.findNavController(itemView).navigate(R.id.action_alumnoCategoriasFragment_to_alumnoTableroFragment, bundle)
            }
        }
    }
}