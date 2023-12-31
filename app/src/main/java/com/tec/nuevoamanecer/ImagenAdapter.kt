package com.tec.nuevoamanecer

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class ImagenAdapter(private val context: Context, private val imagenes: List<Imagen>, private val tts: MutableList<Imagen>, private val ttsAdapter: TTSAdapter) :
    RecyclerView.Adapter<ImagenAdapter.ImagenViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImagenViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_imagen, parent, false)
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

        init {
            btnImagen.setOnClickListener {
                tts.add(imagenes[absoluteAdapterPosition])
                ttsAdapter.notifyDataSetChanged()
            }
        }

        fun bind(imagen: Imagen) {
            val imageUrl = imagen.url

            Glide.with(context)
                .load(imageUrl)
                .placeholder(R.drawable.placeholder_image)
                .error(R.drawable.error_image)
                .into(btnImagen)
        }
    }
}