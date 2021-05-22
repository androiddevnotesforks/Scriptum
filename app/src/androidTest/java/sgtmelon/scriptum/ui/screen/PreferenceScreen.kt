package sgtmelon.scriptum.ui.screen

import sgtmelon.scriptum.R
import sgtmelon.scriptum.basic.extension.click
import sgtmelon.scriptum.basic.extension.isDisplayed
import sgtmelon.scriptum.data.item.PreferenceItem
import sgtmelon.scriptum.presentation.provider.SummaryProvider
import sgtmelon.scriptum.presentation.screen.ui.impl.preference.PreferenceActivity
import sgtmelon.scriptum.presentation.screen.ui.impl.preference.PreferenceFragment
import sgtmelon.scriptum.ui.IPressBack
import sgtmelon.scriptum.ui.ParentRecyclerScreen
import sgtmelon.scriptum.ui.dialog.preference.ThemeDialogUi
import sgtmelon.scriptum.ui.item.PreferenceItemUi
import sgtmelon.scriptum.ui.part.toolbar.SimpleToolbar

/**
 * Class for UI control of [PreferenceActivity], [PreferenceFragment].
 */
class PreferenceScreen : ParentRecyclerScreen(R.id.recycler_view),
        IPressBack {

    //region Views

    private val parentContainer = getViewById(R.id.preference_parent_container)
    private val toolbar = SimpleToolbar(R.string.title_preference, withBack = true)

    private val summaryProvider = SummaryProvider(context.resources)

    private fun getScreenList(): List<PreferenceItem> {
        val list = mutableListOf(
            PreferenceItem.Header(R.string.pref_header_app),
            PreferenceItem.Summary(R.string.pref_title_app_theme, summaryProvider.theme[preferenceRepo.theme]),
            PreferenceItem.Simple(R.string.pref_title_backup),
            PreferenceItem.Simple(R.string.pref_title_note),
            PreferenceItem.Simple(R.string.pref_title_alarm),
            PreferenceItem.Header(R.string.pref_header_other),
            PreferenceItem.Simple(R.string.pref_title_other_rate),
            PreferenceItem.Simple(R.string.pref_title_other_help),
            PreferenceItem.Simple(R.string.pref_title_other_about)
        )

        if (preferenceRepo.isDeveloper) {
            list.add(PreferenceItem.Simple(R.string.pref_title_other_develop))
        }

        return list
    }

    private fun getItem(p: Int) = PreferenceItemUi(recyclerView, p)

    //endregion

    fun onClickClose() {
        toolbar.getToolbarButton().click()
    }

    fun openThemeDialog(func: ThemeDialogUi.() -> Unit = {}) {
        getItem(p = 1).Summary().onItemClick()
        ThemeDialogUi(func)
    }

    //region Assertion

    fun assert() = apply {
        parentContainer.isDisplayed()
        toolbar.assert()

        for ((i, item) in getScreenList().withIndex()) {
            getItem(i).assert(item)
        }
    }

    //endregion

    companion object {
        operator fun invoke(func: PreferenceScreen.() -> Unit): PreferenceScreen {
            return PreferenceScreen().assert().apply(func)
        }
    }
}