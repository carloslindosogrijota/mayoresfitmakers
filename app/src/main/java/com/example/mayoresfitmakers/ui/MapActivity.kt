package com.example.mayoresfitmakers.ui

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.mayoresfitmakers.R
import com.example.mayoresfitmakers.datos.repositorio.RutaRepository
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
    private lateinit var txtTiempo: TextView
    private lateinit var txtDistancia: TextView
    private lateinit var destino: GeoPoint
    private lateinit var locationOverlay: MyLocationNewOverlay

    private val rutaRepository = RutaRepository()
    private val handler = Handler(Looper.getMainLooper())
    private val LOCATION_PERMISSION = 1001

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

        cargarRuta()
    }


//  PEDIR DESTINO AL REPOSITORY

    private fun cargarRuta() {
        rutaRepository.obtenerRuta { geoPoint, nombre ->
            destino = geoPoint
            supportActionBar?.title = nombre ?: "Ruta"

            val marker = Marker(map)
            marker.position = destino
            marker.title = "Destino"
            map.overlays.add(marker)

            comprobarPermisos()
        }
    }


    // PERMISOS

    private fun comprobarPermisos() {
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
            iniciarNavegacion()
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
            iniciarNavegacion()
        }
    }


    private fun iniciarNavegacion() {
        locationOverlay = MyLocationNewOverlay(
            GpsMyLocationProvider(this),
            map
        )

        locationOverlay.enableMyLocation()
        locationOverlay.enableFollowLocation()
        map.overlays.add(locationOverlay)

        locationOverlay.runOnFirstFix {
            runOnUiThread {
                val origen = locationOverlay.myLocation ?: return@runOnUiThread
                map.controller.setCenter(origen)
                dibujarRuta(origen)
            }
        }
    }

    private fun dibujarRuta(origen: GeoPoint) {

        val url = "https://router.project-osrm.org/route/v1/foot/" +
                "${origen.longitude},${origen.latitude};" +
                "${destino.longitude},${destino.latitude}" +
                "?overview=full&geometries=geojson"

        OkHttpClient().newCall(Request.Builder().url(url).build())
            .enqueue(object : Callback {

                override fun onFailure(call: Call, e: IOException) {}

                override fun onResponse(call: Call, response: Response) {
                    val body = response.body?.string() ?: return
                    val json = JSONObject(body)
                    val route = json.getJSONArray("routes").getJSONObject(0)

                    val tiempoMin = (route.getDouble("duration") / 60).roundToInt()
                    val distancia = route.getDouble("distance").roundToInt()

                    val coords =
                        route.getJSONObject("geometry").getJSONArray("coordinates")

                    val puntos = ArrayList<GeoPoint>()
                    for (i in 0 until coords.length()) {
                        val c = coords.getJSONArray(i)
                        puntos.add(GeoPoint(c.getDouble(1), c.getDouble(0)))
                    }

                    runOnUiThread {
                        val linea = Polyline()
                        linea.setPoints(puntos)
                        map.overlays.add(linea)

                        txtTiempo.text = "Tiempo: $tiempoMin min"
                        txtDistancia.text = "Distancia: $distancia m"

                        map.invalidate()
                    }
                }
            })
    }
}
