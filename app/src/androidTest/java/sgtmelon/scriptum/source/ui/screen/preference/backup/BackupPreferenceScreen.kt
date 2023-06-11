package sgtmelon.scriptum.source.ui.screen.preference.backup

import sgtmelon.scriptum.R
import sgtmelon.scriptum.infrastructure.model.annotation.TestViewTag
import sgtmelon.scriptum.infrastructure.screen.preference.backup.BackupPreferenceFragment
import sgtmelon.scriptum.source.ui.parts.preferences.PreferencePart

/**
 * Class for UI control of [BackupPreferenceFragment].
 */
class BackupPreferenceScreen : PreferencePart<BackupPreferenceLogic>(
    R.string.pref_title_backup, TestViewTag.PREF_BACKUP
), BackupPreferenceCallback {

    override val screenLogic = BackupPreferenceLogic(callback = this)

    // TODO complete screen
    override val isExportPermissionGranted: Boolean get() = false
    override val isImportPermissionGranted: Boolean get() = false
    override val isExportEnabled: Boolean get() = true
    override val isImportEnabled: Boolean get() = true

    companion object {
        inline operator fun invoke(
            func: BackupPreferenceScreen.() -> Unit
        ): BackupPreferenceScreen {
            return BackupPreferenceScreen().apply { assert() }.apply(func)
        }
    }
}