package sgtmelon.scriptum.cleanup.ui.dialog.preference

import sgtmelon.safedialog.dialog.SingleDialog
import sgtmelon.scriptum.R
import sgtmelon.scriptum.cleanup.presentation.screen.ui.impl.preference.PreferenceFragment
import sgtmelon.scriptum.cleanup.ui.dialog.parent.ParentSelectDialogUi
import sgtmelon.scriptum.infrastructure.converter.key.ThemeConverter
import sgtmelon.scriptum.infrastructure.model.key.Theme
import sgtmelon.test.cappuccino.utils.click

/**
 * Class for UI control of [SingleDialog] which open from [PreferenceFragment] for select theme.
 */
class ThemeDialogUi : ParentSelectDialogUi(
    R.string.pref_title_app_theme,
    R.array.pref_app_theme
) {

    private val converter = ThemeConverter()

    override val initCheck: Int = preferences.theme
    override var check: Int = initCheck

    fun onClickItem(theme: Theme) = apply {
        val newCheck = converter.toInt(theme)

        check = newCheck
        getItem(newCheck).click()
        assert()
    }

    companion object {
        operator fun invoke(func: ThemeDialogUi.() -> Unit): ThemeDialogUi {
            return ThemeDialogUi().apply { waitOpen { assert() } }.apply(func)
        }
    }
}