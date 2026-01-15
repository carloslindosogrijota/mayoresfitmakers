package com.example.mayoresfitmakers.ui

import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.mayoresfitmakers.R
import com.google.android.material.appbar.MaterialToolbar


class CookiesActivity : AppCompatActivity() {

    private lateinit var toolbarCookies: MaterialToolbar
    private lateinit var btnCerrarCookies: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_cookies)

        toolbarCookies = findViewById(R.id.toolbarCookies)
        btnCerrarCookies = findViewById(R.id.btnCerrarCookies)

        toolbarCookies.setNavigationOnClickListener {
            finish()
        }

        btnCerrarCookies.setOnClickListener {
            finish()
        }
    }
}
