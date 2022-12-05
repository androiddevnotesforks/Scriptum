package sgtmelon.scriptum.parent.ui.screen.preference.backup

import sgtmelon.scriptum.R
import sgtmelon.scriptum.infrastructure.model.annotation.TestViewTag
import sgtmelon.scriptum.infrastructure.screen.preference.backup.BackupPreferenceFragment
import sgtmelon.scriptum.parent.ui.parts.preferences.PreferencePart

/**
 * Class for UI control of [BackupPreferenceFragment].
 */
class BackupPreferenceScreen : PreferencePart<BackupPreferenceLogic>(
    R.string.pref_title_backup, TestViewTag.PREF_BACKUP
) {

    override val screenLogic = BackupPreferenceLogic()

    companion object {
        inline operator fun invoke(func: BackupPreferenceScreen.() -> Unit): BackupPreferenceScreen {
            return BackupPreferenceScreen().apply { assert() }.apply(func)
        }
    }
}