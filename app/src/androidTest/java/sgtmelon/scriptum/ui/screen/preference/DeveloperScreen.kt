package sgtmelon.scriptum.ui.screen.preference

import sgtmelon.scriptum.R
import sgtmelon.scriptum.data.item.PreferenceItem
import sgtmelon.scriptum.presentation.screen.ui.impl.preference.develop.DevelopFragment

/**
 * Class for UI control of [DevelopFragment].
 */
class DeveloperScreen : ParentPreferenceScreen(R.string.pref_title_other_develop) {

    override fun getScreenList(): List<PreferenceItem> {
        return emptyList()
        //        TODO()
        //        val list = mutableListOf(
        //            PreferenceItem.Header(R.string.pref_header_app),
        //            PreferenceItem.Summary(R.string.pref_title_app_theme, summaryProvider.theme[preferenceRepo.theme]),
        //            PreferenceItem.Simple(R.string.pref_title_backup),
        //            PreferenceItem.Simple(R.string.pref_title_note),
        //            PreferenceItem.Simple(R.string.pref_title_alarm),
        //            PreferenceItem.Header(R.string.pref_header_other),
        //            PreferenceItem.Simple(R.string.pref_title_other_rate),
        //            PreferenceItem.Simple(R.string.pref_title_other_help),
        //            PreferenceItem.Simple(R.string.pref_title_other_about)
        //        )
        //
        //        if (preferenceRepo.isDeveloper) {
        //            list.add(PreferenceItem.Simple(R.string.pref_title_other_develop))
        //        }
        //
        //        return list
    }

    companion object {
        operator fun invoke(func: DeveloperScreen.() -> Unit): DeveloperScreen {
            return DeveloperScreen().apply { assert() }.apply(func)
        }
    }
}