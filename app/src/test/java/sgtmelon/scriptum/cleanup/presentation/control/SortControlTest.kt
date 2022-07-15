package sgtmelon.scriptum.cleanup.presentation.control

import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Assert.assertEquals
import org.junit.Test
import sgtmelon.scriptum.TestData
import sgtmelon.scriptum.cleanup.domain.model.annotation.Sort
import sgtmelon.scriptum.parent.ParentCoTest

/**
 * Test for [SortControl].
 */
@ExperimentalCoroutinesApi
class SortControlTest : ParentCoTest() {

    private val data = TestData.Note

    @Test fun sortList() = startCoTest {
        with(data) {
            assertEquals(changeList, SortControl.sortList(itemList, Sort.CHANGE))
            assertEquals(createList, SortControl.sortList(itemList, Sort.CREATE))
            assertEquals(rankList, SortControl.sortList(itemList, Sort.RANK))
            assertEquals(colorList, SortControl.sortList(itemList, Sort.COLOR))
        }
    }

}