package ru.lionzxy.ampersand.utils

object MeasureDecoder {
    fun decode(buffer: List<UByte>): Pair<UInt, UInt>? {
        if (buffer.size < 3) {
            return null
        }

        var first = buffer[0].toUInt()
        first += (buffer[1] and 15u).toUInt() shl 8

        var second = buffer[1].toUInt() shr 4
        second += buffer[2].toUInt() shl 4

        return first to second
    }
}

fun main() {
    val pair = MeasureDecoder.decode(listOf(19u, 16u, 2u))
    if (pair == null) {
        print("Array too small")
        return
    }
    println("${pair.first} ${pair.second}")
}