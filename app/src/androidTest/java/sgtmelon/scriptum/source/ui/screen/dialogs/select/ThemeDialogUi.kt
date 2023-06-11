package sgtmelon.scriptum.source.ui.screen.dialogs.select

import sgtmelon.safedialog.dialog.SingleDialog
import sgtmelon.scriptum.R
import sgtmelon.scriptum.infrastructure.converter.key.ThemeConverter
import sgtmelon.scriptum.infrastructure.model.key.preference.Theme
import sgtmelon.scriptum.infrastructure.screen.preference.menu.MenuPreferenceFragment
import sgtmelon.scriptum.source.ui.parts.dialog.SelectDialogPart
import sgtmelon.test.cappuccino.utils.click

/**
 * Class for UI control of [SingleDialog] which open from [MenuPreferenceFragment] for select theme.
 */
class ThemeDialogUi : SelectDialogPart<Theme>(
    R.string.pref_title_app_theme,
    R.array.pref_theme
) {

    override val initCheck: Int = preferencesRepo.theme.ordinal
    override var check: Int = initCheck

    override fun click(value: Theme) {
        val newCheck = ThemeConverter().toInt(value)

        check = newCheck
        getItem(newCheck).click()
        assert()
    }

    companion object {
        inline operator fun invoke(func: ThemeDialogUi.() -> Unit): ThemeDialogUi {
            return ThemeDialogUi().apply { waitOpen { assert() } }.apply(func)
        }
    }
}