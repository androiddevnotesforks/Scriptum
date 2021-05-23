package sgtmelon.scriptum.ui.screen.preference

import sgtmelon.scriptum.R
import sgtmelon.scriptum.data.item.PreferenceItem
import sgtmelon.scriptum.presentation.screen.ui.impl.preference.PreferenceFragment
import sgtmelon.scriptum.ui.dialog.preference.AboutDialogUi
import sgtmelon.scriptum.ui.dialog.preference.ThemeDialogUi

/**
 * Class for UI control of [PreferenceFragment].
 */
class PreferenceScreen : ParentPreferenceScreen(R.string.title_preference) {

    override fun getScreenList(): List<PreferenceItem> {
        val list = mutableListOf(
            PreferenceItem.Header(R.string.pref_header_app),
            PreferenceItem.Summary(R.string.pref_title_app_theme, provider.theme[preferenceRepo.theme]),
            PreferenceItem.Simple(R.string.pref_title_backup),
            PreferenceItem.Simple(R.string.pref_title_note),
            PreferenceItem.Simple(R.string.pref_title_alarm),
            PreferenceItem.Header(R.string.pref_header_other),
            PreferenceItem.Simple(R.string.pref_title_other_rate),
            PreferenceItem.Simple(R.string.pref_title_other_help),
            PreferenceItem.Simple(R.string.pref_title_other_about)
        )

        if (preferenceRepo.isDeveloper) {
            list.add(PreferenceItem.Simple(R.string.pref_title_other_develop))
        }

        return list
    }

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

    fun openNotification(func: AlarmPreferenceScreen.() -> Unit = {}) {
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

    fun openDeveloper(func: DeveloperScreen.() -> Unit = {}) {
        if (!preferenceRepo.isDeveloper) {
            throw IllegalAccessException("You need turn on developer options")
        }

        getItem(p = 9).Simple().onItemClick()
        DeveloperScreen(func)
    }

    companion object {
        operator fun invoke(func: PreferenceScreen.() -> Unit): PreferenceScreen {
            return PreferenceScreen().apply { assert() }.apply(func)
        }
    }
}