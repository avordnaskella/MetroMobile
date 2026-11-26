package com.example.metroosmotr.presentation.utils

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.pdf.PdfDocument
import java.io.File
import java.io.FileOutputStream

object PdfExporter {
    fun exportShiftSummary(context: Context, critical: Int, medium: Int, low: Int): File {
        val document = PdfDocument()
        val pageInfo = PdfDocument.PageInfo.Builder(595, 842, 1).create() // A4
        val page = document.startPage(pageInfo)
        val canvas: Canvas = page.canvas
        val paint = Paint()

        paint.textSize = 20f
        paint.color = Color.BLACK
        canvas.drawText("МетроОсмотр - Сводка смены", 50f, 50f, paint)

        paint.textSize = 14f
        canvas.drawText("Критических: $critical", 50f, 100f, paint)
        canvas.drawText("Средних: $medium", 50f, 130f, paint)
        canvas.drawText("Низких: $low", 50f, 160f, paint)

        document.finishPage(page)

        val file = File(context.getExternalFilesDir(null), "shift_report.pdf")
        val outputStream = FileOutputStream(file)
        document.writeTo(outputStream)
        document.close()
        outputStream.close()

        return file
    }
}