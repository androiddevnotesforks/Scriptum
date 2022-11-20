package sgtmelon.scriptum.cleanup.ui.logic.preference

import sgtmelon.scriptum.R
import sgtmelon.scriptum.cleanup.testData.item.PreferenceItem
import sgtmelon.scriptum.cleanup.testData.item.PreferenceItem.Header
import sgtmelon.scriptum.cleanup.testData.item.PreferenceItem.Switch
import sgtmelon.scriptum.cleanup.ui.logic.parent.PreferenceLogic
import sgtmelon.scriptum.cleanup.ui.screen.preference.BackupPreferenceScreen

/**
 * Logic for [BackupPreferenceScreen].
 */
class BackupPreferenceLogic : PreferenceLogic() {

    override fun getScreenList(): List<PreferenceItem> {
        val list = mutableListOf<PreferenceItem>(
            Header(R.string.pref_header_main)
        )



        list.add(Header(R.string.pref_header_options))
        list.add(
            Switch(
            R.string.pref_title_import_skip,
            R.string.pref_summary_import_skip,
                preferencesRepo.isBackupSkipImports
        ))

        return emptyList()
    }
}