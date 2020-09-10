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

class SquareForInstagramTest : FunSpec({

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
    val h1 = image1.height
    val w2 = image2.width
    val h2 = image2.height

    check(w1 == w2 && h1 == h2) { "Both images should have same dimensions h1=$h1 w1=$w1 h2=$h2 w2=$w2" }

    val diff = (0 until w1).crossProduct(0 until h1)
        .sumBy { (x, y) -> image1.color(x, y) - image2.color(x, y) }

    val avg = diff / (w1 * h1 * 3).toDouble()
    return avg / 255 * 100
}

private fun IntRange.crossProduct(other: IntRange) =
    asSequence().flatMap { first -> other.asSequence().map { second -> first to second } }

private fun BufferedImage.color(x: Int, y: Int) = Color(getRGB(x, y), true)

private operator fun Color.minus(other: Color) =
    abs(this.red - other.red) + abs(this.green - other.green) + abs(this.blue - other.blue)
