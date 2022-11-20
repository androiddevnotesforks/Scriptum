package sgtmelon.scriptum.cleanup.ui.screen.preference

import sgtmelon.scriptum.R
import sgtmelon.scriptum.cleanup.ui.logic.preference.BackupPreferenceLogic
import sgtmelon.scriptum.infrastructure.screen.preference.backup.BackupPreferenceFragment
import sgtmelon.scriptum.parent.ui.parts.preferences.ParentPreferencePart

/**
 * Class for UI control of [BackupPreferenceFragment].
 */
class BackupPreferenceScreen :
    ParentPreferencePart<BackupPreferenceLogic>(R.string.pref_title_backup) {

    override val screenLogic = BackupPreferenceLogic()

    companion object {
        inline operator fun invoke(func: BackupPreferenceScreen.() -> Unit): BackupPreferenceScreen {
            return BackupPreferenceScreen().apply { assert() }.apply(func)
        }
    }
}