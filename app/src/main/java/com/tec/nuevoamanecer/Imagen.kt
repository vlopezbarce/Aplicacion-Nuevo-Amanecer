package com.tec.nuevoamanecer

import android.os.Parcel
import android.os.Parcelable

data class Imagen(val id: String, val nombre: String, val url: String) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: ""
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeString(nombre)
        parcel.writeString(url)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Imagen> {
        override fun createFromParcel(parcel: Parcel): Imagen {
            return Imagen(parcel)
        }

        override fun newArray(size: Int): Array<Imagen?> {
            return arrayOfNulls(size)
        }
    }
}