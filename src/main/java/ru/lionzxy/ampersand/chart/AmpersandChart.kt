package ru.lionzxy.ampersand.chart

import org.knowm.xchart.XYChart
import ru.lionzxy.ampersand.utils.ListWithTimeLimit


private const val WIDTH = 600
private const val HEIGHT = 400

class AmpersandChart : XYChart(WIDTH, HEIGHT) {
    fun update(data: ListWithTimeLimit<ChartPoint>) {

    }
}