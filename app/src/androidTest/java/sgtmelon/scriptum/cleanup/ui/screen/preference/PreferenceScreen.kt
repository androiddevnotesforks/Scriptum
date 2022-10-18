package sgtmelon.scriptum.cleanup.ui.screen.preference

import sgtmelon.scriptum.R
import sgtmelon.scriptum.cleanup.ui.dialog.preference.AboutDialogUi
import sgtmelon.scriptum.cleanup.ui.dialog.preference.ThemeDialogUi
import sgtmelon.scriptum.cleanup.ui.logic.preference.PreferenceLogic
import sgtmelon.scriptum.cleanup.ui.screen.preference.help.HelpPreferenceScreen
import sgtmelon.scriptum.infrastructure.screen.preference.menu.MenuPreferenceFragment

/**
 * Class for UI control of [MenuPreferenceFragment].
 */
class PreferenceScreen : ParentPreferenceScreen<PreferenceLogic>(R.string.title_preference) {

    override val screenLogic = PreferenceLogic()

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

    fun openRate() = getItem(p = 6).Simple().onItemClick()

    fun openHelp(func: HelpPreferenceScreen.() -> Unit = {}) {
        getItem(p = 7).Simple().onItemClick()
        HelpPreferenceScreen(func)
    }

    fun openAboutDialog(func: AboutDialogUi.() -> Unit = {}) {
        getItem(p = 8).Simple().onItemClick()
        AboutDialogUi(func)
    }

    companion object {
        operator fun invoke(func: PreferenceScreen.() -> Unit): PreferenceScreen {
            return PreferenceScreen().apply { assert() }.apply(func)
        }
    }
}