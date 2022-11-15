package sgtmelon.scriptum.ui.auto.notes

import sgtmelon.scriptum.cleanup.domain.model.item.NoteItem
import sgtmelon.scriptum.parent.ui.screen.main.NotesScreen
import sgtmelon.scriptum.parent.ui.tests.ParentUiTest

inline fun ParentUiTest.startNotesListTest(
    count: Int = 15,
    crossinline func: NotesScreen.(list: MutableList<NoteItem>) -> Unit = {}
) {
    val list = db.fillNotes(count)
    launch { mainScreen { openNotes { func(list) } } }
}

inline fun <T : NoteItem> ParentUiTest.startNotesItemTest(
    item: T,
    crossinline func: NotesScreen.(T) -> Unit
) {
    launch { mainScreen { openNotes { func(item) } } }
}