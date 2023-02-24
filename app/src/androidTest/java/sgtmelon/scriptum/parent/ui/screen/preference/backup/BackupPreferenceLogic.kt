package sgtmelon.scriptum.parent.ui.screen.preference.backup

import kotlin.random.Random
import sgtmelon.scriptum.R
import sgtmelon.scriptum.parent.ui.model.PreferenceItem
import sgtmelon.scriptum.parent.ui.model.PreferenceItem.Header
import sgtmelon.scriptum.parent.ui.model.PreferenceItem.Simple
import sgtmelon.scriptum.parent.ui.model.PreferenceItem.Summary
import sgtmelon.scriptum.parent.ui.model.PreferenceItem.Switch
import sgtmelon.scriptum.parent.ui.parts.preferences.PreferenceLogic

/**
 * Logic for [BackupPreferenceScreen].
 */
class BackupPreferenceLogic : PreferenceLogic() {

    override fun getScreenList(): List<PreferenceItem> {
        val list = mutableListOf(
            Header(R.string.pref_header_main),
            Simple(R.string.pref_title_backup_export, isEnabled = TODO())
        )

        val summary = if (Random.nextBoolean()) {
            context.getString(R.string.pref_summary_import_found, TODO())
        } else {
            context.getString(R.string.pref_summary_import_empty)
        }

        list.add(Summary.Text(R.string.pref_title_backup_import, summary, isEnabled = TODO()))

        val skipSwitch = Switch(
            R.string.pref_title_import_skip,
            R.string.pref_summary_import_skip,
            preferencesRepo.isBackupSkipImports
        )
        list.add(Header(R.string.pref_header_options))
        list.add(skipSwitch)

        return list
    }

    /**
     * Needed for describe order of items.
     */
    enum class Part {
        MAIN_HEADER, EXPORT_ITEM, IMPORT_ITEM,
        OPTIONS_HEADER, SKIP_ITEM
    }
}