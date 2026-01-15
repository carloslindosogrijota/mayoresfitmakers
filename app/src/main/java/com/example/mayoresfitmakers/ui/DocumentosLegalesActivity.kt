package com.example.mayoresfitmakers.ui

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.mayoresfitmakers.R
import com.google.android.material.appbar.MaterialToolbar

class DocumentosLegalesActivity : AppCompatActivity() {

    private lateinit var toolbar: MaterialToolbar

    private lateinit var btnRgpd: Button
    private lateinit var btnCookies: Button
    private lateinit var btnModelosLegales: Button
    private lateinit var btnAnalisisLegal: Button
    private lateinit var btnCesionImagen: Button
    private lateinit var btnConsentimientoGrabacion: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_documentos_legales)

        toolbar = findViewById(R.id.toolbarDocumentosLegales)

        btnRgpd = findViewById(R.id.btnRgpd)
        btnCookies = findViewById(R.id.btnCookies)
        btnModelosLegales = findViewById(R.id.btnModelosLegales)
        btnAnalisisLegal = findViewById(R.id.btnAnalisisLegal)
        btnCesionImagen = findViewById(R.id.btnCesionImagen)
        btnConsentimientoGrabacion = findViewById(R.id.btnConsentimientoGrabacion)

        toolbar.setNavigationOnClickListener {
            finish()
        }

        btnRgpd.setOnClickListener {
            startActivity(Intent(this, RGPDActivity::class.java))
        }

        btnCookies.setOnClickListener {
            startActivity(Intent(this, CookiesActivity::class.java))
        }

        btnModelosLegales.setOnClickListener {
            startActivity(Intent(this, ModelosLegalesActivity::class.java))
        }

        btnAnalisisLegal.setOnClickListener {
            startActivity(Intent(this, AnalisisLegalActivity::class.java))
        }

        btnCesionImagen.setOnClickListener {
            startActivity(Intent(this, CesionImagenActivity::class.java))
        }

        btnConsentimientoGrabacion.setOnClickListener {
            startActivity(Intent(this, ConsentimientoGrabacionActivity::class.java))
        }
    }
}
