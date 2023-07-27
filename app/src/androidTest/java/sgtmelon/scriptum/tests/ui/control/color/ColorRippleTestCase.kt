package sgtmelon.scriptum.tests.ui.control.color

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

    override fun startTest(value: Color) {
        setupTheme(theme)

        val initColor =  Color.values().getDifferentValues(value).second
        preferencesRepo.defaultColor = initColor

        launchNotesPreference {
            openColorDialog(initColor) {
                longPress(value)
            }
        }
    }
}