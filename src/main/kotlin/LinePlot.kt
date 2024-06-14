import org.jcodec.api.awt.AWTSequenceEncoder
import org.jetbrains.kotlinx.dataframe.api.*
import org.jetbrains.kotlinx.kandy.dsl.plot
import org.jetbrains.kotlinx.kandy.letsplot.export.toBufferedImage
import org.jetbrains.kotlinx.kandy.letsplot.feature.layout
import org.jetbrains.kotlinx.kandy.letsplot.layers.line
import java.awt.image.BufferedImage
import java.io.File
import java.io.IOException
import kotlin.math.PI
import kotlin.math.sin
import kotlin.random.Random

fun main() {
    val pointsNumber = 200
    val args = (1..pointsNumber).map { it / 100.0 * 2* PI }
    // Create a DataFrame with data on average temperatures in various cities
    var df = dataFrameOf(
        "arg" to args,
        "sin(x)" to args.map { sin(it) }
    )
    println(args.size)

    val images = mutableListOf<BufferedImage>()
    for (i in 1..<args.size) {
        var slicedDF = df[0..i]
        // Construct a plot using the data from the DataFrame
        val frame = slicedDF.plot {
            // Add bars to the plot
            // Each bar represents the average temperature in a city
            line {
                x("arg")// Set the cities' data on the X-axis
                y("sin(x)") { // Set the temperatures' data on the Y-axis
                    axis.name = "angle" // Assign a name to the Y-axis
                }
            }
            // Set the title of the plot
            layout.title = "Kandy sin(x) Plot Example"
        }.toBufferedImage()  // Save plot as PNG image
        images.add(frame)

//        df = df.update { it["average_temperature"] }
//            .cast<Double>().with { it.plus(Random.nextDouble(-10.0,10.0)) }
    }
    createVideo(images)

}
fun createVideo(images: MutableList<BufferedImage>) {
    val fps = 10
    try {
        val encoder: AWTSequenceEncoder = AWTSequenceEncoder.createSequenceEncoder(
            File("lets-plot-images/sin_animation.mp4"), fps
        ) // 25 fps
        for (image in images) {
            encoder.encodeImage(image)
        }
        encoder.finish()
    } catch (e: IOException) {
        e.printStackTrace()
    }
}