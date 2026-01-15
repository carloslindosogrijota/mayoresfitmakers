package com.example.mayoresfitmakers.ui

import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.mayoresfitmakers.R
import com.google.android.material.appbar.MaterialToolbar

class AnalisisLegalActivity : AppCompatActivity() {

    private lateinit var toolbarAnalisisLegal: MaterialToolbar
    private lateinit var btnCerrarAnalisisLegal: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_analisislegal)

        toolbarAnalisisLegal = findViewById(R.id.toolbarAnalisisLegal)
        btnCerrarAnalisisLegal = findViewById(R.id.btnCerrarAnalisisLegal)

        toolbarAnalisisLegal.setNavigationOnClickListener {
            finish()
        }

        btnCerrarAnalisisLegal.setOnClickListener {
            finish()
        }
    }
}
