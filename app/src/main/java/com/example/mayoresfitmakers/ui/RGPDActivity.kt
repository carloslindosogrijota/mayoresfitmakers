package com.example.mayoresfitmakers.ui

import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.mayoresfitmakers.R
import com.google.android.material.appbar.MaterialToolbar

class RGPDActivity : AppCompatActivity() {

    private lateinit var toolbarRgpd: MaterialToolbar
    private lateinit var btnCerrarRgpd: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_rgpd)

        toolbarRgpd = findViewById(R.id.toolbarRgpd)
        btnCerrarRgpd = findViewById(R.id.btnCerrarRgpd)

        toolbarRgpd.setNavigationOnClickListener {
            finish()
        }

        btnCerrarRgpd.setOnClickListener {
            finish()
        }
    }
}
