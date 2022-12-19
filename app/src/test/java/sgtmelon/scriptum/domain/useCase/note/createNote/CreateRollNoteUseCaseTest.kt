package sgtmelon.scriptum.domain.useCase.note.createNote

import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import io.mockk.verifySequence
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Test
import sgtmelon.scriptum.cleanup.domain.model.item.NoteItem
import sgtmelon.scriptum.data.repository.preferences.PreferencesRepo
import sgtmelon.scriptum.infrastructure.model.key.preference.Color
import sgtmelon.scriptum.testing.parent.ParentTest

/**
 * Test for [CreateRollNoteUseCase].
 */
class CreateRollNoteUseCaseTest : ParentTest() {

    @MockK lateinit var repository: PreferencesRepo

    private val useCase by lazy { CreateRollNoteUseCase(repository) }

    @After override fun tearDown() {
        super.tearDown()
        confirmVerified(repository)
    }

    @Test fun invoke() {
        val defaultColor = mockk<Color>()

        every { repository.defaultColor } returns defaultColor

        assertEquals(useCase(), NoteItem.Roll(color = defaultColor))

        verifySequence {
            repository.defaultColor
        }
    }
}