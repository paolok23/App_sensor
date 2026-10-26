package com.example.app_sensor

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.addCallback
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class Menu : AppCompatActivity() {

    // Declaración de variables usando los IDs exactos de tu activity_menu.xml
    private lateinit var tvusumenu: TextView
    private lateinit var btnmanipular: Button
    private lateinit var btnacciones: Button
    private lateinit var btnestadisticas: Button
    private lateinit var btnperfil: Button
    private lateinit var btncerrarsesion: Button

    private var usu: Usuario? = null

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_menu)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Vinculación con los IDs reales que se ven en tu panel izquierdo (Component Tree)
        tvusumenu = findViewById(R.id.tvnombreusuario)
        btnmanipular = findViewById(R.id.btnmanipular)
        btnacciones = findViewById(R.id.btnacciones)
        btnestadisticas = findViewById(R.id.btnestadisticas)
        btnperfil = findViewById(R.id.btnperfil)
        btncerrarsesion = findViewById(R.id.btncerrarsesion)

        // Recibimos el objeto Usuario enviado desde el login
        usu = intent.getSerializableExtra("usu", Usuario::class.java)

        // Mostramos el nombre del usuario logueado
        tvusumenu.text = "Bienvenido(a) : " + (usu?.Nombre ?: "Usuario")

        configurarBotones()
        desactivarFlecha()
    }

    private fun configurarBotones() {
        // 1. Manipular Circuito
        btnmanipular.setOnClickListener {
            val intent = Intent(this@Menu, Manipular::class.java)
            intent.putExtra("usu", usu)
            startActivity(intent)
        }

        // 2. Ver Acciones Realizadas
        btnacciones.setOnClickListener {
            val intent = Intent(this@Menu, Acciones::class.java)
            intent.putExtra("usu", usu)
            startActivity(intent)
        }

        // 3. Ver Estadísticas
        btnestadisticas.setOnClickListener {
            val intent = Intent(this@Menu, Estadisticas::class.java)
            intent.putExtra("usu", usu)
            startActivity(intent)
        }

        // 4. Ver Perfil
        btnperfil.setOnClickListener {
            val intent = Intent(this@Menu, Perfil::class.java)
            intent.putExtra("usu", usu)
            startActivity(intent)
        }

        // 5. Cerrar Sesión con Alerta
        btncerrarsesion.setOnClickListener {
            val a = AlertDialog.Builder(this@Menu)
            a.setTitle("Pregunta")
            a.setMessage("¿Desea Cerrar La Sesión?")

            a.setNegativeButton("Cancelar") { dialog, _ ->
                dialog.dismiss()
            }

            a.setPositiveButton("Aceptar") { _, _ ->
                val intent = Intent(this@Menu, Principal::class.java)
                startActivity(intent)
                finish()
            }

            a.setCancelable(false)
            a.show()
        }
    }

    private fun desactivarFlecha() {
        onBackPressedDispatcher.addCallback(this) {
            // Bloquea el botón atrás en el menú principal para evitar retornar al login sin cerrar sesión
        }
    }
}