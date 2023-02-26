package sgtmelon.extensions

/** 10 * 70% / 100% = 7 || 15 * 10% / 100% = 1.5 */
fun Int.getPercent(percent: Int): Float = this * percent / PERCENT_MAX.toFloat()

fun Float.getPercent(percent: Int): Float = this * percent / PERCENT_MAX

const val PERCENT_MIN = 0
const val PERCENT_MAX = 100