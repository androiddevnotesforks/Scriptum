package sgtmelon.scriptum.source.utils

const val NEXT_HOUR = 60
const val NEXT_DAY = NEXT_HOUR * 24
const val NEXT_WEEK = NEXT_DAY * 7
const val NEXT_MONTH = NEXT_DAY * 30
const val NEXT_YEAR = NEXT_MONTH * 12

val nextArray = arrayOf(NEXT_HOUR, NEXT_DAY, NEXT_WEEK, NEXT_MONTH, NEXT_YEAR)

const val LAST_HOUR = -60
const val LAST_DAY = LAST_HOUR * 24
const val LAST_MONTH = LAST_DAY * 30
const val LAST_YEAR = LAST_MONTH * 12

val lastArray = arrayListOf(LAST_HOUR, LAST_DAY, LAST_MONTH, LAST_YEAR)