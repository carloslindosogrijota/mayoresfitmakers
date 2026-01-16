package com.example.mayoresfitmakers.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.mayoresfitmakers.R
import com.example.mayoresfitmakers.ui.descuento.DescuentoActivity
import com.google.android.material.button.MaterialButton
import com.google.android.material.card.MaterialCardView

class ActividadesActivity : AppCompatActivity() {

    private lateinit var botonVolver: ImageView
    private lateinit var textoTituloPantalla: TextView

    private lateinit var botonPerfil: LinearLayout
    private lateinit var botonMenu: LinearLayout

    private lateinit var cardCaminar: MaterialCardView
    private lateinit var cardEjerciciosSuaves: MaterialCardView
    private lateinit var cardEstiramientos: MaterialCardView

    private lateinit var botonDocumentosLegales: MaterialButton

    // LOGROS
    private lateinit var cardLogros: MaterialCardView
    private lateinit var txtSaludoLogro: TextView
    private lateinit var txtUltimoLogro: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 1) Carga del layout NUEVO
        setContentView(R.layout.activity_mis_actividades)

        // 2) Referencias UI
        enlazarVistas()

        // 3) Configuración visual mínima
        configurarHeader()

        // 4) Listeners
        configurarContenidoHardcodeado()
        configurarListeners()



    }

    private fun enlazarVistas() {
        botonVolver = findViewById(R.id.btnVolver)
        textoTituloPantalla = findViewById(R.id.txtTituloPantalla)

        botonPerfil = findViewById(R.id.btnPerfil)
        botonMenu = findViewById(R.id.btnMenu)

        cardCaminar = findViewById(R.id.cardCaminar)
        cardEjerciciosSuaves = findViewById(R.id.cardEjerciciosSuaves)
        cardEstiramientos = findViewById(R.id.cardEstiramientos)

        botonDocumentosLegales = findViewById(R.id.btnDocumentosLegales)

        // LOGROS
        cardLogros = findViewById(R.id.cardLogros)
        txtSaludoLogro = findViewById(R.id.txtSaludoLogro)
        txtUltimoLogro = findViewById(R.id.txtUltimoLogro)
    }

    private fun configurarContenidoHardcodeado() {
        val puntos = 120
        val logro = "¡Desbloqueaste un 10% de descuento!"

        txtSaludoLogro.text = "¡Felicidades!"
        txtUltimoLogro.text = "Tienes $puntos puntos. $logro"
    }

    private fun configurarHeader() {
        textoTituloPantalla.text = "Mis Actividades"
    }

    private fun configurarListeners() {
        botonVolver.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                finish()
            }
        })

        botonPerfil.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                // TODO: Cambia PerfilActivity por tu activity real
                val intent = Intent(this@ActividadesActivity, PerfilActivity::class.java)
                startActivity(intent)
            }
        })

        cardCaminar.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                // TODO: Cambia CaminarActivity por tu activity real
                val intent = Intent(this@ActividadesActivity, SenderismoActivity::class.java)
                startActivity(intent)
            }
        })

        cardEjerciciosSuaves.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                // TODO: Cambia EjerciciosActivity por tu activity real
                val intent = Intent(this@ActividadesActivity, SeleccionEjercicioActivity::class.java)
                startActivity(intent)
            }
        })

        cardEstiramientos.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                // TODO: Cambia EventosActivity por tu activity real
                val intent = Intent(this@ActividadesActivity, EventosActivity::class.java)
                startActivity(intent)
            }
        })

        cardLogros.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                val texto = txtUltimoLogro.text.toString()

                // Opcional: solo abrir si hay descuento
                if (texto.contains("10%")) {
                    val intent = Intent(this@ActividadesActivity, DescuentoActivity::class.java)
                    startActivity(intent)
                }
            }
        })



        botonDocumentosLegales.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                // TODO: Cambia DocumentosLegalesActivity por tu activity real
                val intent = Intent(this@ActividadesActivity, DocumentosLegalesActivity::class.java)
                startActivity(intent)
            }
        })

    }
}
