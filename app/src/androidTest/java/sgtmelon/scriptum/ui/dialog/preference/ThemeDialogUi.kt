package sgtmelon.scriptum.ui.dialog.preference

import sgtmelon.safedialog.SingleDialog
import sgtmelon.scriptum.R
import sgtmelon.scriptum.basic.extension.click
import sgtmelon.scriptum.domain.model.annotation.Theme
import sgtmelon.scriptum.presentation.screen.ui.impl.preference.PreferenceFragment

/**
 * Class for UI control of [SingleDialog] which open from [PreferenceFragment] for select theme.
 */
class ThemeDialogUi : ParentSelectDialogUi(
    R.string.pref_title_app_theme,
    R.array.pref_text_app_theme
) {

    override val initCheck: Int = preferenceRepo.theme
    override var check: Int = preferenceRepo.theme

    fun onClickItem(@Theme value: Int) = apply {
        check = value

        getItem(textArray[value]).click()
        assert()
    }

    companion object {
        operator fun invoke(func: ThemeDialogUi.() -> Unit): ThemeDialogUi {
            return ThemeDialogUi().apply { waitOpen { assert() } }.apply(func)
        }
    }
}