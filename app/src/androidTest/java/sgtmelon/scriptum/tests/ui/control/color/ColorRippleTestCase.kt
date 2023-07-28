package sgtmelon.scriptum.tests.ui.control.color

import org.junit.Before
import sgtmelon.scriptum.infrastructure.dialogs.ColorDialog
import sgtmelon.scriptum.infrastructure.model.key.ThemeDisplayed
import sgtmelon.scriptum.infrastructure.model.key.preference.Color
import sgtmelon.scriptum.source.cases.value.ColorCase
import sgtmelon.scriptum.source.ui.tests.ParentUiTest
import sgtmelon.scriptum.source.ui.tests.launchNotesPreference
import sgtmelon.test.common.getDifferentValues

/**
 * Parent class for test [ColorDialog] ripple effect.
 */
abstract class ColorRippleTestCase(private val theme: ThemeDisplayed) : ParentUiTest(),
    ColorCase {

    @Before override fun setUp() {
        super.setUp()
        commandAutomator.changeLongPress(CHECK_TIME)
    }

    override fun startTest(value: Color) {
        setupTheme(theme)

        val initColor = Color.values().getDifferentValues(value).second
        preferencesRepo.defaultColor = initColor

        launchNotesPreference { openColorDialog(initColor) { selectLong(value) } }
    }

    companion object {
        const val CHECK_TIME = 4000L
    }
}