package sgtmelon.scriptum.test.parent.situation

import sgtmelon.scriptum.domain.model.annotation.Color

/**
 * Interface describes [Color] tests.
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


    fun startTest(@Color value: Int)

}