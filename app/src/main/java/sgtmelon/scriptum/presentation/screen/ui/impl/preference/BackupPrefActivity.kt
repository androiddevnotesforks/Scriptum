package sgtmelon.scriptum.presentation.screen.ui.impl.preference

import android.content.Context
import android.content.Intent
import sgtmelon.scriptum.R
import sgtmelon.scriptum.presentation.factory.FragmentFactory
import sgtmelon.scriptum.presentation.screen.ui.ParentPreferenceActivity

/**
 * Screen for display [BackupPrefFragment]
 */
class BackupPrefActivity : ParentPreferenceActivity(
    R.layout.activity_preference_backup,
    R.id.backup_pref_parent_container,
    R.id.backup_pref_fragment_container,
    R.string.pref_title_backup
) {

    override val tag: String = FragmentFactory.Preference.Tag.BACKUP

    override val fragment by lazy { fragmentFactory.getBackupFragment() }

    companion object {
        operator fun get(context: Context) = Intent(context, BackupPrefActivity::class.java)
    }
}