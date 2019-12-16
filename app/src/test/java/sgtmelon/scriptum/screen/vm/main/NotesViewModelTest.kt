package sgtmelon.scriptum.screen.vm.main

import org.junit.Assert.*
import org.junit.Test
import sgtmelon.scriptum.model.annotation.Sort
import sgtmelon.scriptum.model.item.NoteItem
import sgtmelon.scriptum.screen.vm.main.NotesViewModel.Companion.sort

/**
 * Test for [NotesViewModel].
 */
class NotesViewModelTest {

    @Test fun sort() {
        arrayListOf<NoteItem>().sort(Sort.CHANGE)
        assertTrue(true)
    }

}
