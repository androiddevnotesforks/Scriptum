package sgtmelon.scriptum.tests.scenario.main

import sgtmelon.scriptum.infrastructure.screen.main.MainActivity
import sgtmelon.scriptum.tests.ui.auto.main.MainScrollTopTest

/**
 * Scenarios for scroll top feature in [MainActivity]. When you can simply reach top of list, by
 * second tap on current navigation page.
 */
interface MainScrollTopScenario {

    /** Assert item in list by position after double tab click (feature call). */
    fun tests() = MainScrollTopTest()

}