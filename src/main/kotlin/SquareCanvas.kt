package io.github.rusticflare.squarecanvas

import com.sksamuel.scrimage.ImmutableImage
import com.sksamuel.scrimage.nio.JpegWriter
import java.awt.Color
import java.io.File

fun squareCanvas(source: File, target: File) {
    with(ImmutableImage.loader().fromFile(source)) {
        resizeTo(newCanvasSize, newCanvasSize, Color.WHITE).output(JpegWriter(), target)
    }
}

private val ImmutableImage.newCanvasSize: Int
    get() = kotlin.math.max(width, height)