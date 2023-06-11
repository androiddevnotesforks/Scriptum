package sgtmelon.scriptum.ui.control.main

import org.junit.Test
import sgtmelon.scriptum.infrastructure.model.key.preference.Theme
import sgtmelon.scriptum.infrastructure.screen.main.MainActivity
import sgtmelon.scriptum.source.ui.tests.ParentUiControlTest
import sgtmelon.scriptum.source.ui.tests.launchMain
import sgtmelon.scriptum.ui.cases.value.ThemeCase
import sgtmelon.test.cappuccino.utils.await

/**
 *
 * Test of fab gradient animation in [MainActivity].
 */
class MainFabTest : ParentUiControlTest(),
    ThemeCase {

    @Test override fun themeLight() = super.themeLight()

    @Test override fun themeDark() = super.themeDark()

    @Test override fun themeSystem() = super.themeSystem()

    override fun startTest(value: Theme) = launchMain({ preferencesRepo.theme = value }) {
        await(TEST_TIME)
    }

    companion object {
        private const val TEST_TIME = 35000L
    }
}