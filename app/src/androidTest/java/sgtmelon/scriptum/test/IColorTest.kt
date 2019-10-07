package sgtmelon.scriptum.test

import sgtmelon.scriptum.model.annotation.Color

/**
 * Interface describes color tests
 */
interface IColorTest {

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


    fun startTest(@Color color: Int)

}