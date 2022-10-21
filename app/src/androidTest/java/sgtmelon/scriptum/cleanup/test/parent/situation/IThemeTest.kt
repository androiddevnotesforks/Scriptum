package sgtmelon.scriptum.cleanup.test.parent.situation

import sgtmelon.scriptum.infrastructure.model.key.preference.Theme


/**
 * Interface describes [Theme] tests.
 */
interface IThemeTest {

    fun themeLight() = startTest(Theme.LIGHT)

    fun themeDark() = startTest(Theme.DARK)

    fun themeSystem() = startTest(Theme.SYSTEM)

    fun startTest(value: Theme)

}