package com.example.mayoresfitmakers.ui

import android.content.Context
import android.graphics.Paint
import android.graphics.pdf.PdfDocument
import android.os.Bundle
import android.print.PrintAttributes
import android.print.PrintManager
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.mayoresfitmakers.R
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.textfield.TextInputEditText
import java.io.File
import java.io.FileOutputStream

class CesionImagenActivity : AppCompatActivity() {

    private lateinit var toolbarCesion: MaterialToolbar
    private lateinit var btnImprimir: Button

    private lateinit var etNombre: TextInputEditText
    private lateinit var etDni: TextInputEditText
    private lateinit var etDia: TextInputEditText
    private lateinit var etMes: TextInputEditText
    private lateinit var etAnio: TextInputEditText
    private lateinit var etResponsable: TextInputEditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_cesion_imagen)

        toolbarCesion = findViewById(R.id.toolbarCesion)
        btnImprimir = findViewById(R.id.btnImprimir)

        etNombre = findViewById(R.id.etNombre)
        etDni = findViewById(R.id.etDni)
        etDia = findViewById(R.id.etDia)
        etMes = findViewById(R.id.etMes)
        etAnio = findViewById(R.id.etAnio)
        etResponsable = findViewById(R.id.etResponsable)

        toolbarCesion.setNavigationOnClickListener(object : View.OnClickListener {
            override fun onClick(view: View?) {
                finish()
            }
        })

        btnImprimir.setOnClickListener(object : View.OnClickListener {
            override fun onClick(view: View?) {
                imprimirModelo()
            }
        })
    }

    private fun imprimirModelo() {
        val nombre: String = obtenerTexto(etNombre)
        val dni: String = obtenerTexto(etDni)
        val dia: String = obtenerTexto(etDia)
        val mes: String = obtenerTexto(etMes)
        val anio: String = obtenerTexto(etAnio)
        val responsable: String = obtenerTexto(etResponsable)

        val textoFinal: String = construirTextoModelo(
            nombre,
            dni,
            dia,
            mes,
            anio,
            responsable
        )

        val archivoPdf: File = generarPdfEnCache(textoFinal)

        val printManager: PrintManager =
            getSystemService(Context.PRINT_SERVICE) as PrintManager

        val jobName: String = "Modelo_Cesion_Derechos_Imagen"

        val adapter = FilePrintDocumentAdapter(
            this,
            archivoPdf,
            jobName
        )

        printManager.print(jobName, adapter, PrintAttributes.Builder().build())
    }

    private fun obtenerTexto(editText: TextInputEditText): String {
        val texto: String
        texto = editText.text?.toString()?.trim() ?: ""
        return texto
    }

    private fun construirTextoModelo(
        nombre: String,
        dni: String,
        dia: String,
        mes: String,
        anio: String,
        responsable: String
    ): String {

        val nombreSeguro: String
        nombreSeguro = if (nombre.isNotBlank()) { nombre } else { "____________________________________________" }

        val dniSeguro: String
        dniSeguro = if (dni.isNotBlank()) { dni } else { "____________________" }

        val diaSeguro: String
        diaSeguro = if (dia.isNotBlank()) { dia } else { "_____" }

        val mesSeguro: String
        mesSeguro = if (mes.isNotBlank()) { mes } else { "___________" }

        val anioSeguro: String
        anioSeguro = if (anio.isNotBlank()) { anio } else { "______" }

        val responsableSeguro: String
        responsableSeguro = if (responsable.isNotBlank()) { responsable } else { "______________________________" }

        return (
                "MODELO DE CESIÓN DE DERECHOS DE IMAGEN\n\n" +
                        "Yo, $nombreSeguro, con DNI/NIE $dniSeguro, autorizo de forma expresa a la empresa Mi Bienestar " +
                        "a utilizar mi imagen y/o voz, captada mediante fotografías, vídeos o grabaciones audiovisuales " +
                        "realizadas en el marco de sus actividades y servicios relacionados con la promoción de la salud, " +
                        "el ejercicio físico y el bienestar de las personas mayores.\n\n" +

                        "1. Objeto de la cesión\n" +
                        "La presente cesión de derechos de imagen tiene como finalidad permitir el uso de las imágenes y/o " +
                        "grabaciones para fines informativos, educativos y promocionales de los servicios de Mi Bienestar.\n\n" +

                        "2. Ámbito de utilización\n" +
                        "Las imágenes y/o grabaciones podrán ser utilizadas en:\n" +
                        "• La aplicación y página web de Mi Bienestar.\n" +
                        "• Materiales informativos y divulgativos.\n" +
                        "• Redes sociales y otros canales digitales propios de la empresa.\n\n" +

                        "3. Carácter gratuito\n" +
                        "La cesión de derechos se realiza de manera gratuita, sin que exista contraprestación económica a favor del/la cedente.\n\n" +

                        "4. Duración\n" +
                        "La cesión de derechos se concede por tiempo indefinido, salvo revocación expresa por parte del/la interesado/a, " +
                        "que podrá ejercerse mediante solicitud escrita, sin efectos retroactivos.\n\n" +

                        "5. Protección de datos\n" +
                        "Los datos personales serán tratados conforme a la normativa vigente en materia de protección de datos " +
                        "(Reglamento General de Protección de Datos y legislación española aplicable).\n\n" +

                        "6. Aceptación\n" +
                        "Declaro haber leído y comprendido el contenido del presente documento y cedo mis derechos de imagen de forma libre, " +
                        "expresa y voluntaria, en los términos aquí establecidos.\n\n" +

                        "En Villanueva de la Cañada, Madrid a $diaSeguro de $mesSeguro de $anioSeguro.\n\n" +

                        "Firma del/la participante: ____________________________\n\n" +
                        "Nombre y firma del responsable: $responsableSeguro\n"
                )
    }

    private fun generarPdfEnCache(texto: String): File {
        val pdfDocument = PdfDocument()

        val pageInfo = PdfDocument.PageInfo.Builder(595, 842, 1).create() // A4 aprox (72dpi)
        val page = pdfDocument.startPage(pageInfo)

        val canvas = page.canvas

        val paint = Paint()
        paint.textSize = 12f

        val margenIzquierdo: Int = 40
        val margenSuperior: Int = 60
        val anchoDisponible: Int = pageInfo.pageWidth - 80

        val lineas: List<String> = partirEnLineas(texto, paint, anchoDisponible)

        var y: Int = margenSuperior
        val saltoLinea: Int = 18

        var indice: Int = 0
        while (indice < lineas.size) {
            canvas.drawText(lineas[indice], margenIzquierdo.toFloat(), y.toFloat(), paint)
            y = y + saltoLinea
            indice = indice + 1
        }

        pdfDocument.finishPage(page)

        val archivo: File = File(cacheDir, "modelo_cesion_imagen.pdf")

        val outputStream = FileOutputStream(archivo)
        pdfDocument.writeTo(outputStream)
        outputStream.flush()
        outputStream.close()

        pdfDocument.close()

        return archivo
    }

    private fun partirEnLineas(texto: String, paint: Paint, anchoDisponible: Int): List<String> {
        val resultado: MutableList<String> = mutableListOf()

        val parrafos: List<String> = texto.split("\n")
        var indiceParrafo: Int = 0

        while (indiceParrafo < parrafos.size) {
            val parrafo: String = parrafos[indiceParrafo]

            if (parrafo.isBlank()) {
                resultado.add("")
                indiceParrafo = indiceParrafo + 1
            } else {
                val palabras: List<String> = parrafo.split(" ")
                var lineaActual: String = ""
                var indicePalabra: Int = 0

                while (indicePalabra < palabras.size) {
                    val palabra: String = palabras[indicePalabra]

                    val candidata: String =
                        if (lineaActual.isBlank()) { palabra } else { lineaActual + " " + palabra }

                    val anchoCandidata: Float = paint.measureText(candidata)

                    if (anchoCandidata <= anchoDisponible.toFloat()) {
                        lineaActual = candidata
                    } else {
                        if (lineaActual.isNotBlank()) {
                            resultado.add(lineaActual)
                            lineaActual = palabra
                        } else {
                            // palabra muy larga: la metemos tal cual
                            resultado.add(palabra)
                            lineaActual = ""
                        }
                    }

                    indicePalabra = indicePalabra + 1
                }

                if (lineaActual.isNotBlank()) {
                    resultado.add(lineaActual)
                }

                indiceParrafo = indiceParrafo + 1
            }
        }

        return resultado
    }
}
