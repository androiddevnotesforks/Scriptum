package sgtmelon.scriptum.parent.ui.screen.preference

import sgtmelon.scriptum.R
import sgtmelon.scriptum.cleanup.ui.dialog.preference.AboutDialogUi
import sgtmelon.scriptum.cleanup.ui.dialog.preference.ThemeDialogUi
import sgtmelon.scriptum.cleanup.ui.screen.preference.AlarmPreferenceScreen
import sgtmelon.scriptum.cleanup.ui.screen.preference.BackupPreferenceScreen
import sgtmelon.scriptum.cleanup.ui.screen.preference.NotePreferenceScreen
import sgtmelon.scriptum.infrastructure.model.annotation.TestViewTag
import sgtmelon.scriptum.infrastructure.screen.preference.menu.MenuPreferenceFragment
import sgtmelon.scriptum.parent.ui.parts.preferences.PreferencePart

/**
 * Class for UI control of [MenuPreferenceFragment].
 */
class MenuPreferenceScreen : PreferencePart<MenuPreferenceLogic>(
    R.string.title_preference, TestViewTag.PREF_MENU
) {

    override val screenLogic = MenuPreferenceLogic()

    fun openThemeDialog(func: ThemeDialogUi.() -> Unit = {}) {
        getItem(p = 1).Summary().onItemClick()
        ThemeDialogUi(func)
    }

    fun openBackup(func: BackupPreferenceScreen.() -> Unit = {}) {
        getItem(p = 2).Simple().onItemClick()
        BackupPreferenceScreen(func)
    }

    fun openNote(func: NotePreferenceScreen.() -> Unit = {}) {
        getItem(p = 3).Simple().onItemClick()
        NotePreferenceScreen(func)
    }

    fun openAlarm(func: AlarmPreferenceScreen.() -> Unit = {}) {
        getItem(p = 4).Simple().onItemClick()
        AlarmPreferenceScreen(func)
    }

    fun openPrivacyPolicy() = getItem(p = 5).Simple().onItemClick()

    fun openRate() = getItem(p = 6).Simple().onItemClick()

    fun openAboutDialog(func: AboutDialogUi.() -> Unit = {}) {
        getItem(p = 8).Simple().onItemClick()
        AboutDialogUi(func)
    }

    companion object {
        inline operator fun invoke(func: MenuPreferenceScreen.() -> Unit): MenuPreferenceScreen {
            return MenuPreferenceScreen().apply { assert() }.apply(func)
        }
    }
}