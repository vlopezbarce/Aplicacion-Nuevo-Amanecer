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
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

class ImagenEditarAdapter(private val context: Context, private val userUID: String, private val categoria: String, private val imagenes: List<Imagen>) :
    RecyclerView.Adapter<ImagenEditarAdapter.ImagenViewHolder>() {

    private val database: DatabaseReference = FirebaseDatabase.getInstance().reference
    private val storage: StorageReference = FirebaseStorage.getInstance().reference

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImagenViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_imagen_editar, parent, false)
        return ImagenViewHolder(view)
    }

    override fun onBindViewHolder(holder: ImagenViewHolder, position: Int) {
        val imagen = imagenes[position]
        holder.bind(imagen)
    }

    override fun getItemCount(): Int {
        return imagenes.size
    }

    inner class ImagenViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val btnImagen: ImageView = itemView.findViewById(R.id.btnImagen)
        private val txtViewImagen: TextView = itemView.findViewById(R.id.txtViewImagen)
        private val btnEliminar: ImageButton = itemView.findViewById(R.id.btnEliminar)

        fun bind(imagen: Imagen) {
            txtViewImagen.text = imagen.nombre
            val imageUrl = imagen.url

            Glide.with(context)
                .load(imageUrl)
                .placeholder(R.drawable.placeholder_image)
                .error(R.drawable.error_image)
                .into(btnImagen)

            btnEliminar.setOnClickListener{
                val storageRef = storage.child("Tablero/$userUID/$categoria/${imagen.id}")

                storageRef.delete().addOnSuccessListener {
                    val imagenesRef = database.child("Usuarios").child("Tablero").child(userUID).child(categoria)
                    imagenesRef.child(imagen.id).removeValue().addOnSuccessListener {
                        notifyDataSetChanged()
                    }.addOnFailureListener {}
                }.addOnFailureListener {}
            }
        }
    }
}