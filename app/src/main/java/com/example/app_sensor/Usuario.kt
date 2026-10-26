package com.example.app_sensor

import java.io.Serializable

data class Usuario(
    var Nombre: String = "",
    var Apellido: String = "",
    var Cargo: String = "",
    var Rut: String = "",
    var correo: String = "",
    var contraseña: String = ""
) : Serializable