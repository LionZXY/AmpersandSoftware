package ru.lionzxy.ampersand.chart

import org.knowm.xchart.XYChart

private const val CHART_NAME_POWER = "Мощность"
private const val CHART_NAME_CURRENT = "Сила тока"
private const val CHART_NAME_VOLTAGE = "Напряжение"

private const val WIDTH = 1800
private const val HEIGHT = 400

class AmpersandChart(
) : XYChart(WIDTH, HEIGHT) {
    private val initialTime = System.currentTimeMillis()

    var isPowerChartEnabled: Boolean = true
        set(value) {
            field = updateEnabledState(CHART_NAME_POWER, field, value)
        }

    var isCurrentChartEnabled: Boolean = false
        set(value) {
            field = updateEnabledState(CHART_NAME_CURRENT, field, value)
        }

    var isVoltageChartEnabled: Boolean = false
        set(value) {
            field = updateEnabledState(CHART_NAME_VOLTAGE, field, value)
        }

    init {
        styler.yAxisMin = 0.0
        styler.yAxisMax = 5.0
    }

    init {
        if (isPowerChartEnabled) {
            addSeries(CHART_NAME_POWER, DoubleArray(1) { 0.0 })
        }
        if (isCurrentChartEnabled) {
            addSeries(CHART_NAME_CURRENT, DoubleArray(1) { 0.0 })
        }
        if (isVoltageChartEnabled) {
            addSeries(CHART_NAME_VOLTAGE, DoubleArray(1) { 0.0 })
        }
    }

    fun update(
        snapshot: List<Pair<Long, ChartPoint>>,
    ) {
        if (snapshot.isEmpty()) {
            return
        }
        val timeDataX = DoubleArray(snapshot.size)
        val powerDataY = DoubleArray(snapshot.size)
        val currentDataY = DoubleArray(snapshot.size)
        val voltageDataY = DoubleArray(snapshot.size)

        snapshot.forEachIndexed { index, point ->
            timeDataX[index] = (point.first - initialTime).toDouble()
            powerDataY[index] = point.second.power
            currentDataY[index] = point.second.current
            voltageDataY[index] = point.second.voltage
        }

        if (isPowerChartEnabled) {
            updateXYSeries(CHART_NAME_POWER, timeDataX, powerDataY, null)
        }
        if (isCurrentChartEnabled) {
            updateXYSeries(CHART_NAME_CURRENT, timeDataX, currentDataY, null)
        }
        if (isVoltageChartEnabled) {
            updateXYSeries(CHART_NAME_VOLTAGE, timeDataX, voltageDataY, null)
        }
    }

    private fun updateEnabledState(seriesName: String, currentState: Boolean, newState: Boolean): Boolean {
        if (currentState) {
            if (newState.not()) {
                removeSeries(seriesName)
            }
        } else {
            if (newState) {
                addSeries(seriesName, DoubleArray(1) { 0.0 })
            }
        }
        return newState
    }
}