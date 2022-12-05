package sgtmelon.extensions


/** 10 * 70% / 100% = 7 || 15 * 10% / 100% = 1.5 */
fun Int.getPercent(percent: Int): Float = this * percent / 100f