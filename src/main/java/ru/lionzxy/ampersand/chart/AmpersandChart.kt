package ru.lionzxy.ampersand.chart

import com.jidesoft.chart.Chart
import com.jidesoft.chart.DefaultAutoRanger
import com.jidesoft.chart.model.ChartModel
import com.jidesoft.chart.model.DefaultChartModel
import com.jidesoft.chart.style.ChartStyle
import java.awt.BorderLayout
import java.awt.Color
import java.awt.Dimension
import javax.swing.JPanel
import javax.swing.SizeRequirements

private const val CHART_NAME_POWER = "Мощность"
private const val CHART_NAME_CURRENT = "Сила тока"
private const val CHART_NAME_VOLTAGE = "Напряжение"

class AmpersandChart() : JPanel() {
    private val chart = Chart()

    private val initialTime = System.currentTimeMillis()

    private var powerChartModel: DefaultChartModel? = null
        set(value) {
            field = updateChart(field, value)
        }
    var isPowerChartEnabled: Boolean
        get() = powerChartModel != null
        set(value) {
            powerChartModel = onEnabled(CHART_NAME_POWER, value)
        }

    private var currentChartModel: DefaultChartModel? = null
        set(value) {
            field = updateChart(field, value)
        }
    var isCurrentChartEnabled: Boolean
        get() = currentChartModel != null
        set(value) {
            currentChartModel = onEnabled(CHART_NAME_CURRENT, value)
        }

    private var voltageChartModel: DefaultChartModel? = null
        set(value) {
            field = updateChart(field, value)
        }
    var isVoltageChartEnabled: Boolean
        get() = voltageChartModel != null
        set(value) {
            voltageChartModel = onEnabled(CHART_NAME_VOLTAGE, value)
        }

    init {
        layout = BorderLayout()
        chart.autoRanger = DefaultAutoRanger(null, 0.0, null, 5.0)
        chart.isAutoRanging = true
        add(chart, BorderLayout.CENTER)
        isPowerChartEnabled = true
    }

    fun update(
        snapshot: List<Pair<Long, ChartPoint>>,
    ) {
        if (snapshot.isEmpty()) {
            return
        }
        powerChartModel?.clearPoints()
        currentChartModel?.clearPoints()
        voltageChartModel?.clearPoints()

        snapshot.forEachIndexed { index, point ->
            val timePoint = (point.first - initialTime).toInt()
            powerChartModel?.addPoint(timePoint, point.second.power)
            currentChartModel?.addPoint(timePoint, point.second.current)
            voltageChartModel?.addPoint(timePoint, point.second.voltage)
        }
    }

    private fun updateChart(currentState: DefaultChartModel?, newState: DefaultChartModel?): DefaultChartModel? {
        if (currentState == null) {
            if (newState != null) {
                val chartStyle = ChartStyle(Color.blue, false, true)
                chart.addModel(newState, chartStyle)
            }
        } else {
            if (newState == null) {
                chart.removeModel(currentState)
            } else {
                chart.replaceModel(currentState, newState)
            }
        }
        return newState
    }

    private fun onEnabled(chartName: String, newValue: Boolean): DefaultChartModel? {
        return if (newValue) {
            DefaultChartModel(chartName).apply {
                addPoint(0, 0)
            }
        } else {
            null
        }
    }
}