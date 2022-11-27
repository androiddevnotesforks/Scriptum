package sgtmelon.scriptum.parent.ui.screen.preference

import sgtmelon.scriptum.R
import sgtmelon.scriptum.cleanup.ui.screen.preference.BackupPreferenceScreen
import sgtmelon.scriptum.infrastructure.model.annotation.TestViewTag
import sgtmelon.scriptum.infrastructure.screen.preference.menu.MenuPreferenceFragment
import sgtmelon.scriptum.parent.ui.parts.preferences.PreferencePart
import sgtmelon.scriptum.parent.ui.screen.dialogs.preference.AboutDialogUi
import sgtmelon.scriptum.parent.ui.screen.dialogs.select.ThemeDialogUi
import sgtmelon.scriptum.parent.ui.screen.preference.MenuPreferenceLogic.Part
import sgtmelon.scriptum.parent.ui.screen.preference.alarm.AlarmPreferenceScreen
import sgtmelon.scriptum.parent.ui.screen.preference.notes.NotesPreferenceScreen

/**
 * Class for UI control of [MenuPreferenceFragment].
 */
class MenuPreferenceScreen : PreferencePart<MenuPreferenceLogic>(
    R.string.title_preference, TestViewTag.PREF_MENU
) {

    override val screenLogic = MenuPreferenceLogic()

    fun openThemeDialog(func: ThemeDialogUi.() -> Unit = {}) {
        getItem(Part.THEME_ITEM).Summary().onItemClick()
        ThemeDialogUi(func)
    }

    fun openBackup(func: BackupPreferenceScreen.() -> Unit = {}) {
        getItem(Part.BACKUP_ITEM).Simple().onItemClick()
        BackupPreferenceScreen(func)
    }

    fun openNotes(func: NotesPreferenceScreen.() -> Unit = {}) {
        getItem(Part.NOTES_ITEM).Simple().onItemClick()
        NotesPreferenceScreen(func)
    }

    fun openAlarm(func: AlarmPreferenceScreen.() -> Unit = {}) {
        getItem(Part.ALARM_ITEM).Simple().onItemClick()
        AlarmPreferenceScreen(func)
    }

    fun openPrivacyPolicy() = getItem(Part.PRIVACY_ITEM).Simple().onItemClick()

    fun openRate() = getItem(Part.RATE_ITEM).Simple().onItemClick()

    fun openAboutDialog(func: AboutDialogUi.() -> Unit = {}) {
        getItem(Part.ABOUT_ITEM).Simple().onItemClick()
        AboutDialogUi(func)
    }

    companion object {
        inline operator fun invoke(func: MenuPreferenceScreen.() -> Unit): MenuPreferenceScreen {
            return MenuPreferenceScreen().apply { assert() }.apply(func)
        }
    }
}