package sgtmelon.scriptum.cleanup.ui.screen.preference

import org.junit.Assert.assertEquals
import sgtmelon.scriptum.R
import sgtmelon.scriptum.cleanup.ui.dialog.ColorDialogUi
import sgtmelon.scriptum.cleanup.ui.logic.preference.NotesPreferenceLogic
import sgtmelon.scriptum.infrastructure.model.annotation.TestViewTag
import sgtmelon.scriptum.infrastructure.model.key.preference.Color
import sgtmelon.scriptum.infrastructure.screen.preference.note.NotesPreferenceFragment
import sgtmelon.scriptum.parent.ui.parts.preferences.PreferencePart
import sgtmelon.scriptum.parent.ui.screen.dialogs.select.SavePeriodDialogUi
import sgtmelon.scriptum.parent.ui.screen.dialogs.select.SortDialogUi

/**
 * Class for UI control of [NotesPreferenceFragment].
 */
class NotesPreferenceScreen : PreferencePart<NotesPreferenceLogic>(
    R.string.pref_title_note, TestViewTag.PREF_NOTE
), ColorDialogUi.Callback {

    override val screenLogic = NotesPreferenceLogic()

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
        inline operator fun invoke(func: NotesPreferenceScreen.() -> Unit): NotesPreferenceScreen {
            return NotesPreferenceScreen().apply { assert() }.apply(func)
        }
    }
}