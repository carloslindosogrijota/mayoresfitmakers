package com.example.mayoresfitmakers

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.Drawable
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

    // 📍 Destino hardcodeado
    private val DESTINO = GeoPoint(40.4469, -3.9990)

    // ⏱️ FACTOR REALISTA PARA PERSONAS MAYORES
    private val FACTOR_MAYOR = 3.0

    private lateinit var map: MapView
    private lateinit var locationOverlay: MyLocationNewOverlay

    private lateinit var txtTiempo: TextView
    private lateinit var txtDistancia: TextView

    private var rutaActual: Polyline? = null
    private var yaHaLlegado = false

    private val LOCATION_PERMISSION = 1001
    private val handler = Handler(Looper.getMainLooper())
    private val updateInterval = 2000L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Configuration.getInstance().load(
            applicationContext,
            getSharedPreferences("osmdroid", MODE_PRIVATE)
        )
        Configuration.getInstance().userAgentValue = packageName

        setContentView(R.layout.activity_map)

        map = findViewById(R.id.map)
        txtTiempo = findViewById(R.id.txtTiempo)
        txtDistancia = findViewById(R.id.txtDistancia)

        map.setMultiTouchControls(true)
        map.controller.setZoom(18.0)

        addDestinationMarker(DESTINO)
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

        val arrowDrawable = ContextCompat.getDrawable(
            this,
            android.R.drawable.arrow_up_float
        )

        if (arrowDrawable != null) {
            val arrowBitmap = drawableToBitmapScaled(arrowDrawable, 4.5f)
            locationOverlay.setPersonIcon(arrowBitmap)
            locationOverlay.setDirectionIcon(arrowBitmap)
        }

        locationOverlay.isDrawAccuracyEnabled = false
        locationOverlay.enableMyLocation()
        locationOverlay.enableFollowLocation()

        map.overlays.add(locationOverlay)

        locationOverlay.runOnFirstFix {
            runOnUiThread {
                val actual = locationOverlay.myLocation ?: return@runOnUiThread
                map.controller.setCenter(actual)
                drawRoute(actual, DESTINO)
                startLocationUpdates()
            }
        }
    }

    // --------------------------------------------------
    // ACTUALIZACIONES
    // --------------------------------------------------
    private fun startLocationUpdates() {
        handler.postDelayed(object : Runnable {
            override fun run() {
                val actual = locationOverlay.myLocation
                if (actual != null) {
                    comprobarLlegada(actual)
                }
                handler.postDelayed(this, updateInterval)
            }
        }, updateInterval)
    }

    // --------------------------------------------------
    // MARCADOR
    // --------------------------------------------------
    private fun addDestinationMarker(point: GeoPoint) {
        val marker = Marker(map)
        marker.position = point
        marker.title = "Destino"
        map.overlays.add(marker)
    }

    // --------------------------------------------------
    // RUTA + TIEMPO + DISTANCIA
    // --------------------------------------------------
    private fun drawRoute(start: GeoPoint, end: GeoPoint) {

        val url = "https://router.project-osrm.org/route/v1/foot/" +
                "${start.longitude},${start.latitude};" +
                "${end.longitude},${end.latitude}" +
                "?overview=full&geometries=geojson"

        OkHttpClient().newCall(request = Request.Builder().url(url).build())
            .enqueue(object : Callback {

                override fun onFailure(call: Call, e: IOException) {}

                override fun onResponse(call: Call, response: Response) {
                    val body = response.body?.string() ?: return
                    val json = JSONObject(body)

                    val routes = json.optJSONArray("routes") ?: return
                    if (routes.length() == 0) return

                    val route = routes.getJSONObject(0)
                    val coords = route.getJSONObject("geometry").getJSONArray("coordinates")

                    val tiempoSeg = route.getDouble("duration")
                    val tiempoMin =
                        ((tiempoSeg * FACTOR_MAYOR) / 60).roundToInt()

                    val distanciaMetros =
                        route.getDouble("distance").roundToInt()

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

                        txtTiempo.text =
                            "Tiempo estimado: unos $tiempoMin minutos"

                        txtDistancia.text =
                            "Distancia restante: $distanciaMetros m"

                        map.invalidate()
                    }
                }
            })
    }

    // --------------------------------------------------
    // LLEGADA
    // --------------------------------------------------
    private fun comprobarLlegada(actual: GeoPoint) {
        if (actual.distanceToAsDouble(DESTINO) < 20 && !yaHaLlegado) {
            yaHaLlegado = true
            txtTiempo.text = "🏁 Has llegado"
            txtDistancia.text = ""
        }
    }

    // --------------------------------------------------
    // UTILIDADES
    // --------------------------------------------------
    private fun drawableToBitmapScaled(drawable: Drawable, scale: Float): Bitmap {
        val w = (drawable.intrinsicWidth * scale).toInt()
        val h = (drawable.intrinsicHeight * scale).toInt()
        val bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        drawable.setBounds(0, 0, w, h)
        drawable.draw(canvas)
        return bitmap
    }

    override fun onPause() {
        super.onPause()
        handler.removeCallbacksAndMessages(null)
    }

    override fun onDestroy() {
        super.onDestroy()
        handler.removeCallbacksAndMessages(null)
    }
}
