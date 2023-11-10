package com.tec.myapplication

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView

class ImagenAdapter(context: Context, imagenes: List<Imagen>) :
    ArrayAdapter<Imagen>(context, 0, imagenes) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view : View =
            convertView ?: LayoutInflater.from(context).inflate(R.layout.item_imagen, parent, false)

        val imagen = getItem(position)
        val txtViewNombre = view.findViewById<TextView>(R.id.txtViewNombre)
        txtViewNombre.text = imagen?.nombre

        return view
    }
}