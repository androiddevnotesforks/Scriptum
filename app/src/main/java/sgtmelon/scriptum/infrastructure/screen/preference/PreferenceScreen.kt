package sgtmelon.scriptum.infrastructure.screen.preference

import androidx.annotation.StringRes
import sgtmelon.scriptum.R

/**
 * Class identifying opened preference screen.
 */
enum class PreferenceScreen(@StringRes val titleId: Int) {
    MENU(R.string.title_preference),
    BACKUP(R.string.pref_title_backup),
    NOTES(R.string.pref_title_note),
    ALARM(R.string.pref_title_alarm),
    HELP(R.string.pref_title_help),
    DEVELOP(R.string.pref_title_developer),
    SERVICE(R.string.pref_header_service)
}