package com.tec.nuevoamanecer

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class TTSAdapter(private val context: Context, private val tts: MutableList<Imagen>) :
    RecyclerView.Adapter<TTSAdapter.ImagenViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImagenViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_imagen, parent, false)
        return ImagenViewHolder(view)
    }

    override fun onBindViewHolder(holder: ImagenViewHolder, position: Int) {
        val imagen = tts[position]
        holder.bind(imagen)
    }

    override fun getItemCount(): Int {
        return tts.size
    }

    inner class ImagenViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val btnImagen: ImageView = itemView.findViewById(R.id.btnImagen)

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