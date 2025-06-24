package ru.lionzxy.ampersand

import ru.lionzxy.ampersand.chart.ChartPoint
import java.util.*
import kotlin.random.Random

fun main(args: Array<String>) {
    val chartGUI = UpdateLooper()
    chartGUI.start()

    var baseline = 0.442
    while (true) {
        val iterationNear = Random.nextInt(3000)
        baseline = baseline + Random.nextDouble() * 0.1 - 0.05
        for (i in 1..iterationNear) {
            val newValue: Double

            if (Random.nextDouble() < 0.1) {
                newValue = (baseline + Random.nextDouble() * 0.5 - 0.25)
            } else {
                newValue = (baseline + Random.nextDouble() * 0.1 - 0.05)
            }
            chartGUI.addValue(
                ChartPoint(
                    voltage = newValue,
                    current = newValue
                )
            );
            Thread.sleep(1L)
        }
    }
}
