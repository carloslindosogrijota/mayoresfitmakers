package com.example.mayoresfitmakers.ui

import android.Manifest
import android.content.pm.PackageManager
import android.os.*
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.mayoresfitmakers.R
import okhttp3.*
import org.json.JSONObject
import org.osmdroid.config.Configuration
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker
import org.osmdroid.views.overlay.Polyline
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay
import java.io.IOException
import kotlin.math.roundToInt

class MapActivity : AppCompatActivity() {

    private lateinit var map: MapView
    private lateinit var locationOverlay: MyLocationNewOverlay
    private lateinit var destino: GeoPoint
    private lateinit var txtDistancia: TextView
    private lateinit var txtTiempo: TextView

    private var rutaActual: Polyline? = null
    private var yaHaLlegado = false

    private val LOCATION_PERMISSION = 1001

    // Handler para actualizar posición (OSMDroid way)
    private val handler = Handler(Looper.getMainLooper())
    private val updateInterval = 2000L // 2 segundos

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // OSMDroid config
        Configuration.getInstance().load(
            applicationContext,
            getSharedPreferences("osmdroid", MODE_PRIVATE)
        )
        Configuration.getInstance().userAgentValue = packageName

        setContentView(R.layout.activity_map)

        // Datos desde el carrusel
        val lat = intent.getDoubleExtra("lat", 0.0)
        val lng = intent.getDoubleExtra("lng", 0.0)
        val nombre = intent.getStringExtra("nombre") ?: "Destino"

        destino = GeoPoint(lat, lng)

        // Views
        map = findViewById(R.id.map)
        txtDistancia = findViewById(R.id.txtDistancia)
        txtTiempo = findViewById(R.id.txtTiempo)

        map.setMultiTouchControls(true)
        map.controller.setZoom(17.5)

        addDestinationMarker(destino, nombre)
        checkLocationPermission()
    }

    // --------------------------------------------------
    // PERMISOS
    // --------------------------------------------------
    private fun checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                LOCATION_PERMISSION
            )
        } else {
            startNavigation()
        }
    }

    @Suppress("DEPRECATION")
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == LOCATION_PERMISSION &&
            grantResults.isNotEmpty() &&
            grantResults[0] == PackageManager.PERMISSION_GRANTED
        ) {
            startNavigation()
        }
    }

    // --------------------------------------------------
    // NAVEGACIÓN
    // --------------------------------------------------
    private fun startNavigation() {

        locationOverlay = MyLocationNewOverlay(
            GpsMyLocationProvider(this),
            map
        )

        locationOverlay.enableMyLocation()
        locationOverlay.enableFollowLocation()
        map.overlays.add(locationOverlay)

        locationOverlay.runOnFirstFix {
            runOnUiThread {
                val actual = locationOverlay.myLocation ?: return@runOnUiThread
                map.controller.setCenter(actual)
                drawRoute(actual, destino)
                actualizarInfo(actual)
                startLocationUpdates()
            }
        }
    }

    // --------------------------------------------------
    // ACTUALIZACIONES CONTINUAS (FORMA CORRECTA)
    // --------------------------------------------------
    private fun startLocationUpdates() {
        handler.postDelayed(object : Runnable {
            override fun run() {
                val actual = locationOverlay.myLocation
                if (actual != null) {
                    actualizarInfo(actual)
                    comprobarDesvio(actual)
                }
                handler.postDelayed(this, updateInterval)
            }
        }, updateInterval)
    }

    // --------------------------------------------------
    // MARCADOR DESTINO
    // --------------------------------------------------
    private fun addDestinationMarker(point: GeoPoint, title: String) {
        val marker = Marker(map)
        marker.position = point
        marker.title = title
        map.overlays.add(marker)
    }

    // --------------------------------------------------
    // RUTA (API OSRM)
    // --------------------------------------------------
    private fun drawRoute(start: GeoPoint, end: GeoPoint) {

        val url = "https://router.project-osrm.org/route/v1/foot/" +
                "${start.longitude},${start.latitude};" +
                "${end.longitude},${end.latitude}" +
                "?overview=full&geometries=geojson"

        val request = Request.Builder().url(url).build()

        OkHttpClient().newCall(request).enqueue(object : Callback {

            override fun onFailure(call: Call, e: IOException) {}

            override fun onResponse(call: Call, response: Response) {
                val body = response.body?.string() ?: return
                val json = JSONObject(body)

                val route = json.getJSONArray("routes").getJSONObject(0)
                val coords = route.getJSONObject("geometry").getJSONArray("coordinates")
                val durationMin = (route.getDouble("duration") / 60).roundToInt()

                val points = ArrayList<GeoPoint>()
                for (i in 0 until coords.length()) {
                    val c = coords.getJSONArray(i)
                    points.add(GeoPoint(c.getDouble(1), c.getDouble(0)))
                }

                runOnUiThread {
                    rutaActual?.let { map.overlays.remove(it) }

                    rutaActual = Polyline().apply {
                        setPoints(points)
                    }

                    map.overlays.add(rutaActual)
                    txtTiempo.text = "Tiempo estimado: $durationMin min"
                    map.invalidate()
                }
            }
        })
    }

    // --------------------------------------------------
    // DISTANCIA + LLEGADA
    // --------------------------------------------------
    private fun actualizarInfo(actual: GeoPoint) {
        val metros = actual.distanceToAsDouble(destino).roundToInt()

        if (metros > 20) {
            txtDistancia.text = "Distancia restante: $metros m"
        } else if (!yaHaLlegado) {
            yaHaLlegado = true
            txtDistancia.text = "🏁 ¡Has llegado!"
            vibrar()
        }
    }

    private fun vibrar() {
        val vibrator = getSystemService(VIBRATOR_SERVICE) as Vibrator
        vibrator.vibrate(
            VibrationEffect.createOneShot(
                600,
                VibrationEffect.DEFAULT_AMPLITUDE
            )
        )
    }

    // --------------------------------------------------
    // RECALCULAR SI TE SALES
    // --------------------------------------------------
    private fun comprobarDesvio(actual: GeoPoint) {
        val ruta = rutaActual ?: return

        val distanciaARuta = ruta.points.minOf {
            it.distanceToAsDouble(actual)
        }

        if (distanciaARuta > 50) {
            drawRoute(actual, destino)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        handler.removeCallbacksAndMessages(null)
    }
}
