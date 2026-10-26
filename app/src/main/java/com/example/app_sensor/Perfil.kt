package com.example.app_sensor

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.addCallback
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class Perfil : AppCompatActivity() {

    private lateinit var tvusuperfil: TextView
    private lateinit var tvrutper: TextView
    private lateinit var tvnomper: TextView
    private lateinit var tvapeper: TextView
    private lateinit var tvcorper: TextView
    private lateinit var tvcarper: TextView
    private lateinit var tvconper: TextView
    private lateinit var btnvolvman: Button

    private var usu: Usuario? = null

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_perfil)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Vinculación usando exactamente los IDs de tu Component Tree
        tvusuperfil = findViewById(R.id.tvusuperfil)
        tvrutper = findViewById(R.id.tvrutper)
        tvnomper = findViewById(R.id.tvnomper)
        tvapeper = findViewById(R.id.tvapeper)
        tvcorper = findViewById(R.id.tvcorper)
        tvcarper = findViewById(R.id.tvcarper)
        tvconper = findViewById(R.id.tvconper)
        btnvolvman = findViewById(R.id.btnvolvman)

        // Se recibe el objeto enviado desde el Menú
        usu = intent.getSerializableExtra("usu", Usuario::class.java)

        // Se rellenan los campos con los datos del objeto Usuario
        if (usu != null) {
            tvusuperfil.text = "${usu?.Nombre} ${usu?.Apellido}"
            tvrutper.text = usu?.Rut
            tvnomper.text = usu?.Nombre
            tvapeper.text = usu?.Apellido
            tvcorper.text = usu?.correo
            tvcarper.text = usu?.Cargo
            tvconper.text = usu?.contraseña
        }

        volver()
        desactivarFlecha()
    }

    fun volver() {
        btnvolvman.setOnClickListener {
            val intent = Intent(this@Perfil, Menu::class.java)
            intent.putExtra("usu", usu)
            startActivity(intent)
            finish()
        }
    }

    private fun desactivarFlecha() {
        onBackPressedDispatcher.addCallback(this) {
            // Bloquea el botón atrás para asegurar que usen el botón Volver
        }
    }
}