package sgtmelon.scriptum.test.parent.situation

import sgtmelon.scriptum.cleanup.domain.model.annotation.Theme

/**
 * Interface describes [Theme] tests.
 */
interface IThemeTest {

    fun themeLight() = startTest(Theme.LIGHT)

    fun themeDark() = startTest(Theme.DARK)

    fun themeSystem() = startTest(Theme.SYSTEM)

    fun startTest(@Theme value: Int)

}