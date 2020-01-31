package sgtmelon.scriptum.screen.vm.main

import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Assert.assertEquals
import org.junit.Test
import sgtmelon.scriptum.ParentTest
import sgtmelon.scriptum.TestData
import sgtmelon.scriptum.model.annotation.Sort
import sgtmelon.scriptum.screen.vm.main.NotesViewModel.Companion.sort

/**
 * Test for [NotesViewModel].
 */
@ExperimentalCoroutinesApi
class NotesViewModelTest : ParentTest() {

    private val data = TestData.Note

    @Test fun sort() = with(data) {
        assertEquals(changeList, unsortedList.sort(Sort.CHANGE))
        assertEquals(createList, unsortedList.sort(Sort.CREATE))
        assertEquals(rankList, unsortedList.sort(Sort.RANK))
        assertEquals(colorList, unsortedList.sort(Sort.COLOR))
    }

}