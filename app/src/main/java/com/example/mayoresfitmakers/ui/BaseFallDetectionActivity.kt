package com.example.mayoresfitmakers.ui.base

import android.content.Intent
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.mayoresfitmakers.ui.CheckCaidaActivity
import kotlin.math.sqrt

abstract class BaseFallDetectionActivity : AppCompatActivity(), SensorEventListener {

    private lateinit var sensorManager: SensorManager
    private var accelerometer: Sensor? = null

    // Ajustes MVP (puedes retocar el umbral si dispara mucho o poco)
    private val UMBRAL_CAIDA = 15f
    private val TIEMPO_ENTRE_DETECCIONES_MS = 3000L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        sensorManager = getSystemService(SENSOR_SERVICE) as SensorManager
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
    }

    override fun onResume() {
        super.onResume()
        accelerometer?.also { sensor ->
            sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_UI)
        }
    }

    override fun onPause() {
        super.onPause()
        sensorManager.unregisterListener(this)
    }

    override fun onSensorChanged(event: SensorEvent?) {
        if (event == null) return
        if (event.sensor.type != Sensor.TYPE_ACCELEROMETER) return

        val x = event.values[0]
        val y = event.values[1]
        val z = event.values[2]

        // Magnitud total (incluye gravedad). Para MVP es suficiente.
        val accel = sqrt(x * x + y * y + z * z)

        val now = System.currentTimeMillis()

        if (accel > UMBRAL_CAIDA && DetectorCaidaState.puedeDisparar(now, TIEMPO_ENTRE_DETECCIONES_MS)) {
            DetectorCaidaState.registrarDisparo(now)

            val intent = Intent(this, CheckCaidaActivity::class.java)
            // Evita que se creen muchas pantallas si hay varias lecturas seguidas
            intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
            startActivity(intent)
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
        // No necesario para MVP
    }
}

/**
 * Estado global simple para evitar “spam” de pantallas.
 */
object DetectorCaidaState {
    private var ultimaDeteccionMs: Long = 0L
    var dialogoAbierto: Boolean = false
        private set

    fun puedeDisparar(nowMs: Long, cooldownMs: Long): Boolean {
        val pasaCooldown = (nowMs - ultimaDeteccionMs) > cooldownMs
        return pasaCooldown && !dialogoAbierto
    }

    fun registrarDisparo(nowMs: Long) {
        ultimaDeteccionMs = nowMs
    }

    fun setDialogoAbierto(abierto: Boolean) {
        dialogoAbierto = abierto
    }
}
