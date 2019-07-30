package sgtmelon.scriptum.room.converter

import kotlin.math.abs
import kotlin.math.pow

/**
 * Конвертер из числа в булевый массив
 *
 * @author SerjantArbuz
 */
class IntConverter {

    fun toInt(array: BooleanArray): Int {
        var value = 0

        array.forEachIndexed { i, bool ->
            if (bool) value += 2.0.pow(i.toDouble()).toInt()
        }

        return value
    }

    fun toArray(value: Int, minSize: Int = 0) = ArrayList<Boolean>().apply {
        var temp = abs(value)
        var i = 0

        while (temp > 0) {
            add(element = temp % 2 != 0)
            temp /= 2
            i++
        }

        if (i < minSize) {
            repeat(times = minSize - i) { add(false) }
        }
    }.toBooleanArray()

}