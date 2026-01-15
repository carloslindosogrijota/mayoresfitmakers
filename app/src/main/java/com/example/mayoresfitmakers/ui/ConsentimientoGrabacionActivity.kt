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

class ConsentimientoGrabacionActivity : AppCompatActivity() {

    private lateinit var toolbar: MaterialToolbar
    private lateinit var btnImprimir: Button

    private lateinit var etNombre: TextInputEditText
    private lateinit var etDni: TextInputEditText
    private lateinit var etDia: TextInputEditText
    private lateinit var etMes: TextInputEditText
    private lateinit var etAnio: TextInputEditText
    private lateinit var etResponsable: TextInputEditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_consentimiento_grabacion)

        toolbar = findViewById(R.id.toolbarConsentimiento)
        btnImprimir = findViewById(R.id.btnImprimir)

        etNombre = findViewById(R.id.etNombre)
        etDni = findViewById(R.id.etDni)
        etDia = findViewById(R.id.etDia)
        etMes = findViewById(R.id.etMes)
        etAnio = findViewById(R.id.etAnio)
        etResponsable = findViewById(R.id.etResponsable)

        toolbar.setNavigationOnClickListener {
            finish()
        }

        btnImprimir.setOnClickListener {
            imprimirConsentimiento()
        }
    }

    private fun imprimirConsentimiento() {
        val textoFinal = construirTexto(
            obtener(etNombre),
            obtener(etDni),
            obtener(etDia),
            obtener(etMes),
            obtener(etAnio),
            obtener(etResponsable)
        )

        val pdfFile = generarPdf(textoFinal)

        val printManager = getSystemService(Context.PRINT_SERVICE) as PrintManager
        val jobName = "Consentimiento_Grabacion_Imagen"

        val adapter = FilePrintDocumentAdapter(this, pdfFile, jobName)
        printManager.print(jobName, adapter, PrintAttributes.Builder().build())
    }

    private fun obtener(editText: TextInputEditText): String {
        return editText.text?.toString()?.trim() ?: ""
    }

    private fun construirTexto(
        nombre: String,
        dni: String,
        dia: String,
        mes: String,
        anio: String,
        responsable: String
    ): String {

        val n = if (nombre.isNotBlank()) nombre else "____________________________________________"
        val d = if (dni.isNotBlank()) dni else "____________________"
        val di = if (dia.isNotBlank()) dia else "_____"
        val m = if (mes.isNotBlank()) mes else "___________"
        val a = if (anio.isNotBlank()) anio else "______"
        val r = if (responsable.isNotBlank()) responsable else "______________________________"

        return (
                "CONSENTIMIENTO INFORMADO PARA GRABACIÓN Y DIFUSIÓN DE IMÁGENES\n\n" +
                        "Yo, $n, con DNI/NIE $d, declaro que he sido informado/a de forma clara y comprensible " +
                        "sobre la grabación de mi imagen y/o voz en el marco de las actividades y servicios ofrecidos " +
                        "por la empresa Mi Bienestar.\n\n" +

                        "1. Finalidad de la grabación\n" +
                        "Las grabaciones podrán utilizarse con fines educativos, informativos y de mejora del servicio, " +
                        "incluyendo su difusión en la aplicación, página web y materiales informativos relacionados con " +
                        "la actividad física, la salud y el bienestar de las personas mayores.\n\n" +

                        "2. Uso de las imágenes y grabaciones\n" +
                        "Las imágenes y/o grabaciones podrán ser reproducidas, comunicadas y difundidas exclusivamente " +
                        "en los canales propios de Mi Bienestar, sin fines comerciales distintos a los descritos en el " +
                        "presente documento.\n\n" +

                        "3. Protección de datos\n" +
                        "Los datos personales serán tratados conforme a la normativa vigente en materia de protección de datos " +
                        "(Reglamento General de Protección de Datos y normativa española aplicable) y no serán cedidos a terceros " +
                        "sin consentimiento expreso, salvo obligación legal.\n\n" +

                        "4. Carácter voluntario\n" +
                        "La participación es totalmente voluntaria. El consentimiento podrá ser retirado en cualquier momento " +
                        "mediante comunicación escrita, sin que ello suponga perjuicio alguno para el/la participante.\n\n" +

                        "5. Aceptación\n" +
                        "Declaro haber leído y comprendido la información anterior y otorgo mi consentimiento libre, expreso y " +
                        "voluntario para la grabación y difusión de mi imagen y/o voz en los términos indicados.\n\n" +

                        "En Villanueva de la Cañada, Madrid a $di de $m de $a.\n\n" +
                        "Firma del/la participante: ____________________________\n\n" +
                        "Nombre y firma del responsable: $r\n"
                )
    }

    private fun generarPdf(texto: String): File {
        val pdf = PdfDocument()
        val pageInfo = PdfDocument.PageInfo.Builder(595, 842, 1).create()
        val page = pdf.startPage(pageInfo)

        val paint = Paint()
        paint.textSize = 12f

        val margenX = 40
        var y = 60
        val ancho = pageInfo.pageWidth - 80
        val salto = 18

        val lineas = partirLineas(texto, paint, ancho)

        var i = 0
        while (i < lineas.size) {
            page.canvas.drawText(lineas[i], margenX.toFloat(), y.toFloat(), paint)
            y += salto
            i++
        }

        pdf.finishPage(page)

        val file = File(cacheDir, "consentimiento_grabacion.pdf")
        val fos = FileOutputStream(file)
        pdf.writeTo(fos)
        fos.close()
        pdf.close()

        return file
    }

    private fun partirLineas(texto: String, paint: Paint, ancho: Int): List<String> {
        val resultado = mutableListOf<String>()
        val parrafos = texto.split("\n")

        var p = 0
        while (p < parrafos.size) {
            val palabras = parrafos[p].split(" ")
            var linea = ""
            var i = 0

            while (i < palabras.size) {
                val candidata =
                    if (linea.isEmpty()) palabras[i] else "$linea ${palabras[i]}"

                if (paint.measureText(candidata) <= ancho) {
                    linea = candidata
                } else {
                    resultado.add(linea)
                    linea = palabras[i]
                }
                i++
            }

            if (linea.isNotBlank()) {
                resultado.add(linea)
            }
            resultado.add("")
            p++
        }

        return resultado
    }
}
