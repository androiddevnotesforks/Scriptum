package sgtmelon.scriptum.domain.useCase.main

import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Test
import sgtmelon.scriptum.cleanup.TestData
import sgtmelon.scriptum.testing.parent.ParentTest
import sgtmelon.scriptum.infrastructure.model.key.preference.Sort

/**
 * Test for [SortNoteListUseCase].
 */
class SortNoteListUseCaseTest : ParentTest() {

    private val useCase = SortNoteListUseCase()

    @Test fun invoke() {
        runBlocking {
            with(TestData.Note) {
                assertEquals(changeList, useCase(changeList.shuffled(), Sort.CHANGE))
                assertEquals(createList, useCase(createList.shuffled(), Sort.CREATE))
                assertEquals(rankList, useCase(rankList.shuffled(), Sort.RANK))
                assertEquals(colorList, useCase(colorList.shuffled(), Sort.COLOR))
            }
        }
    }
}
