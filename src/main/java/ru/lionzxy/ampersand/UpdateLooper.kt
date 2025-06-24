package ru.lionzxy.ampersand

import ru.lionzxy.ampersand.chart.AmpersandChart
import ru.lionzxy.ampersand.chart.ChartPoint
import ru.lionzxy.ampersand.ui.MainGUI
import ru.lionzxy.ampersand.utils.ListWithTimeLimit
import java.util.concurrent.TimeUnit
import javax.swing.SwingUtilities

class UpdateLooper : Thread() {
    private val listWithTimeLimit = ListWithTimeLimit<ChartPoint>(PLOT_WIDTH_MILLIS)
    private var initialTime: Long = 0
    private val chart: AmpersandChart
    private val mainGui: MainGUI
    private var isDirty = false

    init {
        initialTime = System.currentTimeMillis()

        chart = AmpersandChart()
        mainGui = MainGUI(chart)
    }

    @Synchronized
    fun addValue(point: ChartPoint) {
        listWithTimeLimit.add(point, System.currentTimeMillis())
        isDirty = true
    }

    @Synchronized
    override fun start() {
        super.start()
        mainGui.start()
    }

    fun onChangeStartState(onChange: (Boolean) -> Unit) {
        mainGui.onChangeStartState(onChange)
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
        val snapshot = listWithTimeLimit.threadSafeSnapshot()
        SwingUtilities.invokeLater {
            chart.update(snapshot)
            mainGui.repaint()
        }
        isDirty = false
    }

    companion object {
        private val PLOT_WIDTH_MILLIS = 3 * 1000L
        private const val FPS = 30
        private val SLEEP_TIME = (1000 / FPS).toLong()
    }
}
