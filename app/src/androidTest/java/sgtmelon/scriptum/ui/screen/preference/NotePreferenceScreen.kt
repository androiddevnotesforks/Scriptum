package sgtmelon.scriptum.ui.screen.preference

import org.junit.Assert.assertEquals
import sgtmelon.scriptum.R
import sgtmelon.scriptum.data.item.PreferenceItem
import sgtmelon.scriptum.domain.model.annotation.Color
import sgtmelon.scriptum.presentation.screen.ui.impl.preference.NotePreferenceFragment
import sgtmelon.scriptum.ui.dialog.ColorDialogUi

/**
 * Class for UI control of [NotePreferenceFragment].
 */
class NotePreferenceScreen : ParentPreferenceScreen(R.string.pref_title_note),
    ColorDialogUi.Callback {

    override fun getScreenList(): List<PreferenceItem> = listOf(
        PreferenceItem.Header(R.string.pref_header_common),
        PreferenceItem.Summary(R.string.pref_title_note_sort, provider.sort[preferenceRepo.sort]),
        PreferenceItem.Summary(R.string.pref_title_note_color, provider.color[preferenceRepo.defaultColor]),
        PreferenceItem.Header(R.string.pref_header_save),
        PreferenceItem.Switch(R.string.pref_title_note_save_pause, R.string.pref_summary_note_save_pause, preferenceRepo.pauseSaveOn),
        PreferenceItem.Switch(R.string.pref_title_note_save_auto, R.string.pref_summary_note_save_auto, preferenceRepo.autoSaveOn),
        PreferenceItem.Summary(R.string.pref_title_note_save_period, provider.savePeriod[preferenceRepo.savePeriod], preferenceRepo.autoSaveOn)
    )

    fun openColorDialog(@Color check: Int, func: ColorDialogUi.() -> Unit) {
        getItem(p = 2).Summary().onItemClick()
        ColorDialogUi(func, ColorDialogUi.Place.PREF, check, this)
    }

    override fun onColorDialogResult(@Color check: Int) {
        assertEquals(check, preferenceRepo.defaultColor)
        assert()
    }

    companion object {
        operator fun invoke(func: NotePreferenceScreen.() -> Unit): NotePreferenceScreen {
            return NotePreferenceScreen().apply { assert() }.apply(func)
        }
    }
}