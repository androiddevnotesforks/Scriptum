package sgtmelon.scriptum.ui.logic.preference

import sgtmelon.scriptum.R
import sgtmelon.scriptum.cleanup.data.item.PreferenceItem
import sgtmelon.scriptum.cleanup.data.item.PreferenceItem.Header
import sgtmelon.scriptum.cleanup.data.item.PreferenceItem.Switch
import sgtmelon.scriptum.ui.logic.parent.ParentPreferenceLogic
import sgtmelon.scriptum.ui.screen.preference.BackupPreferenceScreen

/**
 * Logic for [BackupPreferenceScreen].
 */
class BackupPreferenceLogic : ParentPreferenceLogic() {

    override fun getScreenList(): List<PreferenceItem> {
        val list = mutableListOf<PreferenceItem>(
            Header(R.string.pref_header_main)
        )



        list.add(Header(R.string.pref_header_options))
        list.add(Switch(
            R.string.pref_title_backup_skip,
            R.string.pref_summary_backup_skip,
            preferenceRepo.importSkip
        ))

        return emptyList()
    }
}