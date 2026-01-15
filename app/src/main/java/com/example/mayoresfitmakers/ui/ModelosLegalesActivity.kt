package com.example.mayoresfitmakers.ui

import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.mayoresfitmakers.R
import com.google.android.material.appbar.MaterialToolbar

class ModelosLegalesActivity : AppCompatActivity() {

    private lateinit var toolbarModelosLegales: MaterialToolbar
    private lateinit var btnCerrarModelosLegales: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_modelos_legales)

        toolbarModelosLegales = findViewById(R.id.toolbarModelosLegales)
        btnCerrarModelosLegales = findViewById(R.id.btnCerrarModelosLegales)

        toolbarModelosLegales.setNavigationOnClickListener {
            finish()
        }

        btnCerrarModelosLegales.setOnClickListener {
            finish()
        }
    }
}
