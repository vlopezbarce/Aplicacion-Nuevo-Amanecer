package com.tec.nuevoamanecer

data class Alumno(val id: String = "",
                  val nombre: String = "",
                  val apellidos: String = "",
                  val fechaNacimiento: String = "",
                  val nivel: String = "",
    ) {
    constructor() : this("", "", "", "", "")
}