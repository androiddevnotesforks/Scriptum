package sgtmelon.scriptum.ui.dialog.preference

import sgtmelon.safedialog.dialog.SingleDialog
import sgtmelon.scriptum.R
import sgtmelon.scriptum.basic.extension.click
import sgtmelon.scriptum.cleanup.domain.model.annotation.Theme
import sgtmelon.scriptum.cleanup.presentation.screen.ui.impl.preference.PreferenceFragment
import sgtmelon.scriptum.ui.dialog.parent.ParentSelectDialogUi

/**
 * Class for UI control of [SingleDialog] which open from [PreferenceFragment] for select theme.
 */
class ThemeDialogUi : ParentSelectDialogUi(
    R.string.pref_title_app_theme,
    R.array.pref_text_app_theme
) {

    override val initCheck: Int = appPreferences.theme
    override var check: Int = initCheck

    fun onClickItem(@Theme position: Int) = apply {
        check = position

        getItem(position).click()
        assert()
    }

    companion object {
        operator fun invoke(func: ThemeDialogUi.() -> Unit): ThemeDialogUi {
            return ThemeDialogUi().apply { waitOpen { assert() } }.apply(func)
        }
    }
}