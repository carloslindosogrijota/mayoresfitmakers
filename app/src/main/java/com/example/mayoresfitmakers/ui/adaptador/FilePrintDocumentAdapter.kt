package com.example.mayoresfitmakers.ui

import android.content.Context
import android.os.CancellationSignal
import android.os.ParcelFileDescriptor
import android.print.PrintDocumentAdapter
import android.print.PrintDocumentInfo
import android.print.PageRange
import android.print.PrintAttributes
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream

class FilePrintDocumentAdapter(
    private val context: Context,
    private val pdfFile: File,
    private val documentName: String
) : PrintDocumentAdapter() {

    override fun onLayout(
        oldAttributes: PrintAttributes?,
        newAttributes: PrintAttributes?,
        cancellationSignal: CancellationSignal?,
        callback: LayoutResultCallback?,
        extras: android.os.Bundle?
    ) {
        if (cancellationSignal != null && cancellationSignal.isCanceled) {
            if (callback != null) {
                callback.onLayoutCancelled()
            }
            return
        }

        val info = PrintDocumentInfo.Builder(documentName)
            .setContentType(PrintDocumentInfo.CONTENT_TYPE_DOCUMENT)
            .setPageCount(PrintDocumentInfo.PAGE_COUNT_UNKNOWN)
            .build()

        if (callback != null) {
            callback.onLayoutFinished(info, true)
        }
    }

    override fun onWrite(
        pages: Array<out PageRange>?,
        destination: ParcelFileDescriptor?,
        cancellationSignal: CancellationSignal?,
        callback: WriteResultCallback?
    ) {
        if (destination == null) {
            if (callback != null) {
                callback.onWriteFailed("Destino de impresión no disponible")
            }
            return
        }

        if (cancellationSignal != null && cancellationSignal.isCanceled) {
            if (callback != null) {
                callback.onWriteCancelled()
            }
            return
        }

        val inputStream = FileInputStream(pdfFile)
        val outputStream = FileOutputStream(destination.fileDescriptor)

        val buffer = ByteArray(1024)
        var bytesLeidos: Int

        bytesLeidos = inputStream.read(buffer)
        while (bytesLeidos > 0) {
            outputStream.write(buffer, 0, bytesLeidos)
            bytesLeidos = inputStream.read(buffer)
        }

        inputStream.close()
        outputStream.close()

        if (callback != null) {
            callback.onWriteFinished(arrayOf(PageRange.ALL_PAGES))
        }
    }
}
