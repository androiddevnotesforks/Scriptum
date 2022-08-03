package sgtmelon.scriptum.parent.provider

/**
 * Class for fast providing of date values.
 */
object DateProvider {

    const val DATE_1 = "1234-01-02 03:04:05"
    const val DATE_2 = "1345-02-03 04:05:06"
    const val DATE_3 = "1456-03-04 05:06:07"
    const val DATE_4 = "1567-04-05 06:07:08"
    const val DATE_5 = "1998-08-25 07:08:09"

    val list = listOf(DATE_1, DATE_2, DATE_3, DATE_4, DATE_5)

    fun nextDate(): String = list.random()
}