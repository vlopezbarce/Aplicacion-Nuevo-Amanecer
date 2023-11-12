package com.tec.nuevoamanecer

data class Imagen(val id: String = "", val nombre: String = "", val path: String = "") {
    constructor() : this("", "", "")
}