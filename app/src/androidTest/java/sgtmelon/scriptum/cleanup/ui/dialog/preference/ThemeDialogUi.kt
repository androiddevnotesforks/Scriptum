package sgtmelon.scriptum.cleanup.ui.dialog.preference

import sgtmelon.safedialog.dialog.SingleDialog
import sgtmelon.scriptum.R
import sgtmelon.scriptum.cleanup.ui.dialog.parent.ParentSelectDialogUi
import sgtmelon.scriptum.infrastructure.converter.key.ThemeConverter
import sgtmelon.scriptum.infrastructure.model.key.preference.Theme
import sgtmelon.scriptum.infrastructure.screen.preference.menu.MenuPreferenceFragment
import sgtmelon.test.cappuccino.utils.click

/**
 * Class for UI control of [SingleDialog] which open from [MenuPreferenceFragment] for select theme.
 */
class ThemeDialogUi : ParentSelectDialogUi(
    R.string.pref_title_app_theme,
    R.array.pref_theme
) {

    private val converter = ThemeConverter()

    override val initCheck: Int = preferencesRepo.theme.ordinal
    override var check: Int = initCheck

    fun onClickItem(theme: Theme) = apply {
        val newCheck = converter.toInt(theme)

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