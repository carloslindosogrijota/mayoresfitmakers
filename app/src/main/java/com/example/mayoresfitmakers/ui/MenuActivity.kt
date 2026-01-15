package com.example.mayoresfitmakers.ui

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.app.ActivityOptions
import androidx.appcompat.app.AppCompatActivity
import com.example.mayoresfitmakers.R
import com.google.android.material.card.MaterialCardView

class MenuActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu)

        val cardPerfil = findViewById<MaterialCardView>(R.id.cardPerfil)

        cardPerfil.setOnClickListener {
            val intent = Intent(this, PerfilActivity::class.java)

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                val options = ActivityOptions.makeSceneTransitionAnimation(
                    this,
                    cardPerfil,
                    "card_transition"
                )
                startActivity(intent, options.toBundle())
            } else {
                // Fallback para versiones antiguas
                startActivity(intent)
            }
        }
    }
}
