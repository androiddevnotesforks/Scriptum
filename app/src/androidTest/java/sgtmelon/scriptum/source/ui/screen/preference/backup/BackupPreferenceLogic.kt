package sgtmelon.scriptum.source.ui.screen.preference.backup

import sgtmelon.scriptum.R
import sgtmelon.scriptum.source.ui.model.PreferenceItem
import sgtmelon.scriptum.source.ui.model.PreferenceItem.Header
import sgtmelon.scriptum.source.ui.model.PreferenceItem.Simple
import sgtmelon.scriptum.source.ui.model.PreferenceItem.Summary
import sgtmelon.scriptum.source.ui.model.PreferenceItem.Switch
import sgtmelon.scriptum.source.ui.parts.preferences.PreferenceLogic
import sgtmelon.test.common.halfChance

/**
 * Logic for [BackupPreferenceScreen].
 */
class BackupPreferenceLogic(
    private val callback: BackupPreferenceCallback
) : PreferenceLogic() {

    override fun getScreenList(): List<PreferenceItem> = with(callback) {
        val list = mutableListOf<PreferenceItem>(Header(R.string.pref_header_main))

        val exportItem = if (isExportPermissionGranted) {
            Simple(R.string.pref_title_backup_export, isExportEnabled)
        } else {
            val summary = context.getString(R.string.button_request_permission)
            Summary.Text(R.string.pref_title_backup_export, summary, isExportEnabled)

        }
        list.add(exportItem)

        val summary = if (isImportPermissionGranted) {
            if (halfChance()) {
                context.getString(R.string.pref_summary_import_found, TODO())
            } else {
                context.getString(R.string.pref_summary_import_empty)
            }
        } else {
            context.getString(R.string.button_request_permission)
        }

        list.add(Summary.Text(R.string.pref_title_backup_import, summary, isImportEnabled))

        val skipSwitch = Switch(
            R.string.pref_title_import_skip,
            R.string.pref_summary_import_skip,
            preferencesRepo.isBackupSkip
        )
        list.add(Header(R.string.pref_header_options))
        list.add(skipSwitch)

        return list
    }

    /** Needed for describe order of items. */
    enum class Part {
        MAIN_HEADER, EXPORT_ITEM, IMPORT_ITEM,
        OPTIONS_HEADER, SKIP_ITEM
    }
}