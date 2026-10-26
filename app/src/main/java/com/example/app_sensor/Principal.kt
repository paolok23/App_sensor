package com.example.app_sensor

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.addCallback
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class Principal : AppCompatActivity() {

    private lateinit var txtusu: EditText
    private lateinit var txtcon: EditText
    private lateinit var btnini: Button
    private lateinit var btncrea: Button // 1. Declaramos el botón de crear cuenta

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()
        setContentView(R.layout.activity_principal)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(
                WindowInsetsCompat.Type.systemBars()
            )

            v.setPadding(
                systemBars.left,
                systemBars.top,
                systemBars.right,
                systemBars.bottom
            )

            insets
        }

        txtusu = findViewById(R.id.txtusu)
        txtcon = findViewById(R.id.txtcon)
        btnini = findViewById(R.id.btnini)
        btncrea = findViewById(R.id.btncrea) // 2. Enlazamos con el ID del XML

        botonLogin()
        configurarBotonCrear() // 3. Activamos la función del botón crear cuenta
        desactivarFlecha()
    }

    private fun botonLogin() {
        btnini.setOnClickListener {

            if (
                txtusu.text.toString().trim().isEmpty() ||
                txtcon.text.toString().trim().isEmpty()
            ) {
                Toast.makeText(
                    this@Principal,
                    "¡ Complete Los Campos Faltantes !",
                    Toast.LENGTH_LONG
                ).show()

            } else {
                val ingresoUsuario = txtusu.text.toString().trim()
                val ingresoPassword = txtcon.text.toString().trim()

                var estado = false

                val dbref = FirebaseDatabase
                    .getInstance()
                    .getReference("Usuario")

                dbref.addListenerForSingleValueEvent(
                    object : ValueEventListener {

                        override fun onDataChange(snapshot: DataSnapshot) {

                            for (x in snapshot.children) {

                                val nombreDb =
                                    x.child("nombre").value?.toString()
                                        ?: x.child("Nombre").value?.toString()
                                        ?: ""

                                val rutDb =
                                    x.child("rut").value?.toString()
                                        ?: x.child("Rut").value?.toString()
                                        ?: ""

                                val passDb =
                                    x.child("contraseña").value?.toString()
                                        ?: ""

                                if (
                                    (
                                            ingresoUsuario.equals(
                                                nombreDb,
                                                ignoreCase = true
                                            ) ||
                                                    ingresoUsuario.equals(
                                                        rutDb,
                                                        ignoreCase = true
                                                    )
                                            ) &&
                                    ingresoPassword == passDb
                                ) {
                                    estado = true

                                    val usu = Usuario(
                                        Nombre = nombreDb,
                                        Apellido =
                                            x.child("apellido").value?.toString()
                                                ?: x.child("Apellido").value?.toString()
                                                ?: "",
                                        Cargo =
                                            x.child("cargo").value?.toString()
                                                ?: x.child("Cargo").value?.toString()
                                                ?: "",
                                        Rut = rutDb,
                                        correo =
                                            x.child("correo").value?.toString()
                                                ?: "",
                                        contraseña = passDb
                                    )

                                    val intent = Intent(
                                        this@Principal,
                                        Menu::class.java
                                    )

                                    intent.putExtra("usu", usu)

                                    startActivity(intent)
                                    finish()

                                    break
                                }
                            }

                            if (!estado) {
                                txtusu.setText("")
                                txtcon.setText("")

                                ocultarTeclado()

                                Toast.makeText(
                                    this@Principal,
                                    "¡ Error de Usuario y/o Contraseña !",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }

                        override fun onCancelled(error: DatabaseError) {
                            Toast.makeText(
                                this@Principal,
                                "Error en la base de datos: ${error.message}",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                )
            }
        }
    }

    // 4. Función para que el botón de Crear Cuenta abra la actividad correspondiente
    private fun configurarBotonCrear() {
        btncrea.setOnClickListener {
            val intent = Intent(this, Cuenta::class.java)
            startActivity(intent)
        }
    }

    private fun desactivarFlecha() {
        onBackPressedDispatcher.addCallback(this) {
            // Bloquea el botón atrás.
        }
    }

    private fun ocultarTeclado() {
        val view = currentFocus

        if (view != null) {
            val imm = getSystemService(
                Context.INPUT_METHOD_SERVICE
            ) as InputMethodManager

            imm.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }
}