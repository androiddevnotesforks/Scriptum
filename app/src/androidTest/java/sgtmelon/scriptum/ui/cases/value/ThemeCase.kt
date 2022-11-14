package sgtmelon.scriptum.ui.cases.value

import sgtmelon.scriptum.infrastructure.model.key.preference.Theme


/**
 * Interface describes [Theme] tests.
 */
interface ThemeCase {

    fun themeLight() = startTest(Theme.LIGHT)

    fun themeDark() = startTest(Theme.DARK)

    fun themeSystem() = startTest(Theme.SYSTEM)

    fun startTest(value: Theme)

}