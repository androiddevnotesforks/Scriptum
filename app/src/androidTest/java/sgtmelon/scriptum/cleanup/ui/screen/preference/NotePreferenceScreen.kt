package sgtmelon.scriptum.cleanup.ui.screen.preference

import org.junit.Assert.assertEquals
import sgtmelon.scriptum.R
import sgtmelon.scriptum.cleanup.ui.dialog.ColorDialogUi
import sgtmelon.scriptum.cleanup.ui.dialog.preference.SavePeriodDialogUi
import sgtmelon.scriptum.cleanup.ui.dialog.preference.SortDialogUi
import sgtmelon.scriptum.cleanup.ui.logic.preference.NotePreferenceLogic
import sgtmelon.scriptum.infrastructure.model.key.preference.Color
import sgtmelon.scriptum.infrastructure.screen.preference.note.NotePreferenceFragment

/**
 * Class for UI control of [NotePreferenceFragment].
 */
class NotePreferenceScreen : ParentPreferenceScreen<NotePreferenceLogic>(R.string.pref_title_note),
    ColorDialogUi.Callback {

    override val screenLogic = NotePreferenceLogic()

    fun openSortDialog(func: SortDialogUi.() -> Unit = {}) {
        getItem(p = 1).Summary().onItemClick()
        SortDialogUi(func)
    }

    fun openColorDialog(color: Color, func: ColorDialogUi.() -> Unit) {
        getItem(p = 2).Summary().onItemClick()
        ColorDialogUi(func, ColorDialogUi.Place.PREF, color, this)
    }

    override fun onColorDialogResult(color: Color) {
        assertEquals(color, preferencesRepo.defaultColor)
        assert()
    }

    fun onPauseSaveClick() {
        getItem(p = 4).Switch().onItemClick()
        assert()
    }

    fun onAutoSaveClick() {
        getItem(p = 5).Switch().onItemClick()
        assert()
    }

    fun openSavePeriodDialog(func: SavePeriodDialogUi.() -> Unit = {}) {
        getItem(p = 6).Summary().onItemClick()
        SavePeriodDialogUi(func)
    }

    companion object {
        inline operator fun invoke(func: NotePreferenceScreen.() -> Unit): NotePreferenceScreen {
            return NotePreferenceScreen().apply { assert() }.apply(func)
        }
    }
}