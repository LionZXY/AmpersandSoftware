package ru.lionzxy.ampersand.chart

data class ChartPoint(
    val voltage: Double,
    val current: Double
) {
    val power: Double
        get() = voltage * current
}