package com.tec.nuevoamanecer

data class Imagen(val id: String = "", val nombre: String = "", val url: String = "") {
    constructor() : this("", "", "")
}