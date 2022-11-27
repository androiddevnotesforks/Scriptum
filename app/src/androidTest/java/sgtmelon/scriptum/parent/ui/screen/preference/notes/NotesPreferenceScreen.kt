package sgtmelon.scriptum.parent.ui.screen.preference.notes

import org.junit.Assert.assertEquals
import sgtmelon.scriptum.R
import sgtmelon.scriptum.infrastructure.model.annotation.TestViewTag
import sgtmelon.scriptum.infrastructure.model.key.preference.Color
import sgtmelon.scriptum.infrastructure.screen.preference.note.NotesPreferenceFragment
import sgtmelon.scriptum.parent.ui.parts.preferences.PreferencePart
import sgtmelon.scriptum.parent.ui.screen.dialogs.ColorDialogUi
import sgtmelon.scriptum.parent.ui.screen.dialogs.select.SavePeriodDialogUi
import sgtmelon.scriptum.parent.ui.screen.dialogs.select.SortDialogUi
import sgtmelon.scriptum.parent.ui.screen.preference.notes.NotesPreferenceLogic.Part

/**
 * Class for UI control of [NotesPreferenceFragment].
 */
class NotesPreferenceScreen : PreferencePart<NotesPreferenceLogic>(
    R.string.pref_title_note, TestViewTag.PREF_NOTE
), ColorDialogUi.Callback {

    override val screenLogic = NotesPreferenceLogic()

    fun openSortDialog(func: SortDialogUi.() -> Unit = {}) {
        getItem(Part.SORT_ITEM).Summary().onItemClick()
        SortDialogUi(func)
    }

    fun openColorDialog(color: Color, func: ColorDialogUi.() -> Unit) {
        getItem(Part.COLOR_ITEM).Summary().onItemClick()
        ColorDialogUi(func, ColorDialogUi.Place.PREF, color, this)
    }

    override fun onColorDialogResult(color: Color) {
        assertEquals(color, preferencesRepo.defaultColor)
        assert()
    }

    fun switchPauseSave() {
        getItem(Part.ON_PAUSE_ITEM).Switch().onItemClick()
        assert()
    }

    fun switchAutoSave() {
        getItem(Part.ON_AUTO_ItEM).Switch().onItemClick()
        assert()
    }

    fun openSavePeriodDialog(func: SavePeriodDialogUi.() -> Unit = {}) {
        getItem(Part.SAVE_PERIOD_ITEM).Summary().onItemClick()
        SavePeriodDialogUi(func)
    }

    companion object {
        inline operator fun invoke(func: NotesPreferenceScreen.() -> Unit): NotesPreferenceScreen {
            return NotesPreferenceScreen().apply { assert() }.apply(func)
        }
    }
}