package io.github.rusticflare.squarecanvas

import com.sksamuel.scrimage.ImmutableImage
import com.sksamuel.scrimage.nio.JpegWriter
import java.awt.Color
import java.io.File

private const val INSTAGRAM_IMAGE_SIZE = 1080

fun File.squareForInstagram(target: File) {
    immutableImage()
        .bound(INSTAGRAM_IMAGE_SIZE, INSTAGRAM_IMAGE_SIZE)
        .resizeTo(INSTAGRAM_IMAGE_SIZE, INSTAGRAM_IMAGE_SIZE, Color.WHITE)
        .output(JpegWriter(), target)
}

private fun File.immutableImage() = ImmutableImage.loader().fromFile(this)
