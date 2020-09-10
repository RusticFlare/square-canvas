package io.github.rusticflare.squarecanvas

import io.kotest.core.spec.style.FunSpec
import io.kotest.data.forAll
import io.kotest.data.row
import io.kotest.engine.spec.tempfile
import io.kotest.matchers.doubles.shouldBeExactly
import java.awt.Color
import java.awt.image.BufferedImage
import java.io.File
import java.net.URL
import javax.imageio.ImageIO
import kotlin.math.abs

class SquareCanvasTest : FunSpec({

    context("Test squareForInstagram") {
        forAll(
            row("small"),
            row("big"),
        ) { folder ->
            test("For a $folder image") {
                val source = javaClass.getResource("/$folder/source.jpg").toFile()
                val target = tempfile(suffix = ".jpg")
                val expected = javaClass.getResource("/$folder/expected.jpg").toFile()

                source.squareForInstagram(target)

                target differenceTo expected shouldBeExactly 0.0
            }
        }
    }
})

private fun URL.toFile() = File(toURI())

private infix fun File.differenceTo(image: File) = difference(ImageIO.read(this), ImageIO.read(image))

private fun difference(image1: BufferedImage, image2: BufferedImage): Double {
    val w1 = image1.width
    val w2 = image2.width
    val h1 = image1.height
    val h2 = image2.height

    check(w1 == w2 && h1 == h2) { "Both images should have same dimensions h1=$h1 w1=$w1 h2=$h2 w2=$w2" }

    var diff: Long = 0
    for (j in 0 until h1) {
        for (i in 0 until w1) {
            //Getting the RGB values of a pixel
            val pixel1 = image1.getRGB(i, j)
            val color1 = Color(pixel1, true)
            val r1: Int = color1.red
            val g1: Int = color1.green
            val b1: Int = color1.blue
            val pixel2 = image2.getRGB(i, j)
            val color2 = Color(pixel2, true)
            val r2: Int = color2.red
            val g2: Int = color2.green
            val b2: Int = color2.blue
            //sum of differences of RGB values of the two images
            val data = abs(r1 - r2) + abs(g1 - g2) + abs(b1 - b2).toLong()
            diff += data
        }
    }
    val avg = diff / (w1 * h1 * 3).toDouble()
    return avg / 255 * 100
}
