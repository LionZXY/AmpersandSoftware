package ru.lionzxy.ampersand

import org.knowm.xchart.QuickChart
import org.knowm.xchart.SwingWrapper
import org.knowm.xchart.XYChart
import ru.lionzxy.ampersand.ui.MainGUI
import ru.lionzxy.ampersand.utils.ListWithTimeLimit
import java.util.concurrent.TimeUnit
import javax.swing.SwingUtilities

class ChartGUI : Thread() {
    private val listWithTimeLimit = ListWithTimeLimit<Int?>(PLOT_WIDTH_MILLIS)
    private var initialTime: Long = 0
    private val chart: XYChart
    private val mainGui: MainGUI<XYChart>
    private var isDirty = false

    init {
        initialTime = System.currentTimeMillis()
        val initdata = getData()

        chart = QuickChart.getChart("Ampersand", "time (ms)", "power (µW)", "power", initdata[0], initdata[1])
        chart.getStyler().setYAxisMin(0.0)
        chart.getStyler().setYAxisMax(15000.0)
        mainGui = MainGUI(chart)
    }

    @Synchronized
    fun addValue(value: Int) {
        listWithTimeLimit.add(value, System.currentTimeMillis())
        isDirty = true
    }

    @Synchronized
    override fun start() {
        super.start()
        mainGui.start()
    }

    override fun run() {
        while (!isInterrupted) {
            try {
                sleep(SLEEP_TIME)
                if (isDirty) {
                    onUpdate()
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    @Synchronized
    private fun onUpdate() {
        val data = getData()
        SwingUtilities.invokeLater {
            chart.updateXYSeries("power", data[0], data[1], null)
            mainGui.repaint()
        }
        isDirty = false
    }

    private fun getData(): Array<DoubleArray?> {
        val local: MutableList<Pair<Long?, Int?>> = ArrayList(listWithTimeLimit)
        if (local.isEmpty()) {
            return arrayOf(doubleArrayOf(0.0), doubleArrayOf(0.0))
        }
        val xData = DoubleArray(local.size)
        val yData = DoubleArray(local.size)

        for (i in local.indices) {
            val point = local[i]
            xData[i] = (point.first!! - initialTime).toDouble()
            yData[i] = point.second!!.toDouble()
        }

        return arrayOf(xData, yData)
    }

    companion object {
        private val PLOT_WIDTH_MILLIS = TimeUnit.MINUTES.toMillis(1)
        private const val FPS = 30
        private val SLEEP_TIME = (1000 / FPS).toLong()
    }
}
