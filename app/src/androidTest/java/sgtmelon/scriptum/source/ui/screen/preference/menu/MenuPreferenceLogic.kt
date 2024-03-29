package sgtmelon.scriptum.source.ui.screen.preference.menu

import sgtmelon.scriptum.R
import sgtmelon.scriptum.source.ui.model.PreferenceItem
import sgtmelon.scriptum.source.ui.model.PreferenceItem.Header
import sgtmelon.scriptum.source.ui.model.PreferenceItem.Simple
import sgtmelon.scriptum.source.ui.model.PreferenceItem.Summary
import sgtmelon.scriptum.source.ui.parts.preferences.PreferenceLogic

/**
 * Logic for [MenuPreferenceScreen].
 */
class MenuPreferenceLogic : PreferenceLogic() {

    override fun getScreenList(): List<PreferenceItem> {
        val list = mutableListOf(
            Header(R.string.pref_header_app),
            Summary.Text(R.string.pref_title_app_theme, summary.getTheme(preferencesRepo.theme)),
            Simple(R.string.pref_title_backup),
            Simple(R.string.pref_title_note),
            Simple(R.string.pref_title_alarm),
            Header(R.string.pref_header_other),
            Simple(R.string.pref_title_policy),
            Simple(R.string.pref_title_rate),
            Simple(R.string.pref_title_about)
        )

        if (preferencesRepo.isDeveloper) {
            list.add(Simple(R.string.pref_title_developer))
        }

        return list
    }

    /** Needed for describe order of items. */
    enum class Part {
        APP_HEADER, THEME_ITEM, BACKUP_ITEM, NOTES_ITEM, ALARM_ITEM,
        OTHER_HEADER, PRIVACY_ITEM, RATE_ITEM, ABOUT_ITEM
    }
}