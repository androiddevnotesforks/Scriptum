package sgtmelon.scriptum.source.cases.value

import sgtmelon.scriptum.infrastructure.model.key.preference.Color

/**
 * Interface describes [Color] tests.
 */
interface ColorCase {

    fun colorRed() = startTest(Color.RED)

    fun colorPurple() = startTest(Color.PURPLE)

    fun colorIndigo() = startTest(Color.INDIGO)

    fun colorBlue() = startTest(Color.BLUE)

    fun colorTeal() = startTest(Color.TEAL)

    fun colorGreen() = startTest(Color.GREEN)

    fun colorYellow() = startTest(Color.YELLOW)

    fun colorOrange() = startTest(Color.ORANGE)

    fun colorBrown() = startTest(Color.BROWN)

    fun colorBlueGrey() = startTest(Color.BLUE_GREY)

    fun colorWhite() = startTest(Color.WHITE)

    fun startTest(value: Color)

}