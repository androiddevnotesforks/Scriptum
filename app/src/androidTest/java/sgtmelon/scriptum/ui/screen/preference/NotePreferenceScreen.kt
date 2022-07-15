package sgtmelon.scriptum.ui.screen.preference

import org.junit.Assert.assertEquals
import sgtmelon.scriptum.R
import sgtmelon.scriptum.cleanup.domain.model.annotation.Color
import sgtmelon.scriptum.cleanup.presentation.screen.ui.impl.preference.NotePreferenceFragment
import sgtmelon.scriptum.ui.dialog.ColorDialogUi
import sgtmelon.scriptum.ui.dialog.preference.SavePeriodDialogUi
import sgtmelon.scriptum.ui.dialog.preference.SortDialogUi
import sgtmelon.scriptum.ui.logic.preference.NotePreferenceLogic

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

    fun openColorDialog(@Color check: Int, func: ColorDialogUi.() -> Unit) {
        getItem(p = 2).Summary().onItemClick()
        ColorDialogUi(func, ColorDialogUi.Place.PREF, check, this)
    }

    override fun onColorDialogResult(@Color check: Int) {
        assertEquals(check, preferenceRepo.defaultColor)
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
        operator fun invoke(func: NotePreferenceScreen.() -> Unit): NotePreferenceScreen {
            return NotePreferenceScreen().apply { assert() }.apply(func)
        }
    }
}