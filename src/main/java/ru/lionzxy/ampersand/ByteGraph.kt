package ru.lionzxy.ampersand

import org.knowm.xchart.BitmapEncoder
import org.knowm.xchart.QuickChart
import org.knowm.xchart.XYChartBuilder
import org.knowm.xchart.internal.chartpart.Chart
import java.awt.Color
import java.awt.Font

private const val ONE_FRAME_WIDTH = 10

fun main() {
    val bytes = "START".toByteArray(Charsets.US_ASCII)
    val chart = getChart(bytes)
    BitmapEncoder.saveBitmapWithDPI(
        chart,
        "./bits",
        BitmapEncoder.BitmapFormat.PNG,
        300
    );

}

private fun getChart(bytes: ByteArray): Chart<*, *> {
    val bits = bytes.map { it.toBits().toList() }.flatten()
    val points = getPoints(bits)

    val chart = XYChartBuilder()
        .width(ONE_FRAME_WIDTH * bits.size)
        .height(ONE_FRAME_WIDTH + 8)
        .build()

    chart.addSeries(
        "bits",
        points.map { it.first.toDouble() },
        points.map { it.second.toDouble() }
    )

    with(chart.styler) {
        isCursorEnabled = false
        isToolTipsEnabled = false
        isLegendVisible = false
        isChartTitleVisible = false
        isChartTitleBoxVisible = false
        isPlotBorderVisible = false
        isYAxisTicksVisible = false
        isYAxisTitleVisible = false
        isXAxisTicksVisible = false
        isXAxisTitleVisible = false
        isAxisTicksLineVisible = false
        isAxisTicksMarksVisible = false
        isPlotGridHorizontalLinesVisible = false
        isPlotGridVerticalLinesVisible = false
        isPlotGridLinesVisible = false
        isToolTipsAlwaysVisible = false
        isPlotTicksMarksVisible = false
        setMarkerSize(0)
        chartPadding = 0
        chartTitlePadding = 0
        legendPadding = 0
        axisTickPadding = 0
        annotationTextPanelPadding = 0
        axisTitlePadding = 0

        plotGridLinesColor = Color.BLACK
        xAxisTickMarksColor = Color.BLACK
        yAxisTickMarksColor = Color.BLACK

    }


    return chart
}

private fun getPoints(bits: List<Int>): List<Pair<Int, Int>> {
    val points = mutableListOf<Pair<Int, Int>>()
    points.add(0 to 0)
    var prevPoint = 0

    for (i in 0..<bits.size) {
        val bit = bits[i]
        val index = i + 1
        if (bit == prevPoint) {
            points.add(index to bit)
        } else {
            points.add(index to prevPoint)
            points.add(index to bit)
        }
        prevPoint = bit
    }

    return points
}


fun Byte.toBits(): IntArray {
    val bits = IntArray(Byte.SIZE_BITS)
    for (i in 0..<Byte.SIZE_BITS) {
        bits[i] = (this.toInt() shr i) and 1
    }
    return bits
}
