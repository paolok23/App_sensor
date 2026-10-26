package com.example.app_sensor

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.database.FirebaseDatabase

class Cuenta : AppCompatActivity() {

    private lateinit var txtrut: EditText
    private lateinit var txtnom: EditText
    private lateinit var txtape: EditText
    private lateinit var txtemail: EditText
    private lateinit var spcargo: Spinner
    private lateinit var txtpass: EditText
    private lateinit var txtpass2: EditText
    private lateinit var btnRegistrar: Button
    private lateinit var btnVolverLogin: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_cuenta)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Vinculación exacta con los IDs de tu diseño XML
        txtrut = findViewById(R.id.txtrut)
        txtnom = findViewById(R.id.txtnom)
        txtape = findViewById(R.id.txtape)
        txtemail = findViewById(R.id.txtemail)
        spcargo = findViewById(R.id.spcargo)
        txtpass = findViewById(R.id.txtpass)
        txtpass2 = findViewById(R.id.txtpass2)
        btnRegistrar = findViewById(R.id.btnvolver) // ID del botón Registrar en tu XML
        btnVolverLogin = findViewById(R.id.button2) // ID del botón Volver en tu XML

        // Acción del botón Registrar
        btnRegistrar.setOnClickListener {
            registrarUsuario()
        }

        // Acción del botón Volver al Login
        btnVolverLogin.setOnClickListener {
            val intent = Intent(this, Principal::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun registrarUsuario() {
        val rut = txtrut.text.toString().trim()
        val nombre = txtnom.text.toString().trim()
        val apellido = txtape.text.toString().trim()
        val correo = txtemail.text.toString().trim()
        val cargo = spcargo.selectedItem.toString().trim()
        val contraseña = txtpass.text.toString().trim()
        val repetirContrasena = txtpass2.text.toString().trim()

        // Validaciones de campos vacíos
        if (rut.isEmpty() || nombre.isEmpty() || apellido.isEmpty() || correo.isEmpty() || contraseña.isEmpty() || repetirContrasena.isEmpty()) {
            Toast.makeText(this, "Por favor complete todos los campos", Toast.LENGTH_SHORT).show()
            return
        }

        // Validación de que ambas contraseñas coincidan
        if (contraseña != repetirContrasena) {
            Toast.makeText(this, "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show()
            return
        }

        // Referencia a Firebase Realtime Database en la rama "Usuario"
        val databaseReference = FirebaseDatabase.getInstance().getReference("Usuario")
        val userId = databaseReference.push().key ?: rut

        // Creación del objeto con los atributos exactos
        val nuevoUsuario = Usuario(
            Nombre = nombre,
            Apellido = apellido,
            Cargo = cargo,
            Rut = rut,
            correo = correo,
            contraseña = contraseña
        )

        // Guardar en la base de datos
        databaseReference.child(userId).setValue(nuevoUsuario)
            .addOnSuccessListener {
                Toast.makeText(this, "¡Cuenta creada con éxito!", Toast.LENGTH_SHORT).show()
                val intent = Intent(this, Principal::class.java)
                startActivity(intent)
                finish()
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Error al registrar: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }
}