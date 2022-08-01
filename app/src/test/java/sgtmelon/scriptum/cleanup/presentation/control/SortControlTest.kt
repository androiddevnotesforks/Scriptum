package sgtmelon.scriptum.cleanup.presentation.control

import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Assert.assertEquals
import org.junit.Test
import sgtmelon.scriptum.cleanup.TestData
import sgtmelon.scriptum.cleanup.parent.ParentCoTest
import sgtmelon.scriptum.infrastructure.model.key.Sort

/**
 * Test for [SortControl].
 */
@ExperimentalCoroutinesApi
class SortControlTest : ParentCoTest() {

    private val data = TestData.Note

    @Test fun sortList() = startCoTest {
        with(data) {
            assertEquals(changeList, SortControl.sortList(changeList.shuffled(), Sort.CHANGE))
            assertEquals(createList, SortControl.sortList(createList.shuffled(), Sort.CREATE))
            assertEquals(rankList, SortControl.sortList(rankList.shuffled(), Sort.RANK))
            assertEquals(colorList, SortControl.sortList(colorList.shuffled(), Sort.COLOR))
        }
    }
}