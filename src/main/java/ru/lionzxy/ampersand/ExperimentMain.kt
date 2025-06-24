package ru.lionzxy.ampersand

import ru.lionzxy.ampersand.chart.ChartPoint
import java.util.*

object ExperimentMain {
    private val RANDOM = Random()

    @Throws(InterruptedException::class)
    @JvmStatic
    fun main(args: Array<String>) {
        val chartGUI = UpdateLooper()
        chartGUI.start()

        var baseline = 0.442
        while (true) {
            val iterationNear = RANDOM.nextInt(3000)
            baseline = baseline + RANDOM.nextDouble() * 0.1 - 0.05
            for (i in 1..iterationNear) {
                val newValue: Double

                if (RANDOM.nextDouble() < 0.1) {
                    newValue = (baseline + RANDOM.nextDouble() * 0.5 - 0.25)
                } else {
                    newValue = (baseline + RANDOM.nextDouble() * 0.1 - 0.05)
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
}
