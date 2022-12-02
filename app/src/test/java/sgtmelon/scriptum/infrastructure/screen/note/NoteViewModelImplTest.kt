package sgtmelon.scriptum.infrastructure.screen.note

import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import io.mockk.verifySequence
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Test
import sgtmelon.scriptum.data.repository.preferences.PreferencesRepo
import sgtmelon.scriptum.infrastructure.model.key.preference.Color
import sgtmelon.scriptum.infrastructure.model.key.preference.NoteType
import sgtmelon.scriptum.testing.parent.ParentTest

/**
 * Test for [NoteViewModelImpl].
 */
class NoteViewModelImplTest : ParentTest() {

    //region Setup

    @MockK lateinit var preferencesRepo: PreferencesRepo

    private val viewModel by lazy { NoteViewModelImpl(preferencesRepo) }

    @After override fun tearDown() {
        super.tearDown()
        confirmVerified(preferencesRepo)
    }

    //endregion

    @Test fun `get defaultColor`() {
        val color = mockk<Color>()

        every { preferencesRepo.defaultColor } returns color

        assertEquals(viewModel.defaultColor, color)

        verifySequence {
            preferencesRepo.defaultColor
        }
    }

    @Test fun `convertType from text`() {
        assertEquals(viewModel.convertType(NoteType.TEXT), NoteType.ROLL)
    }

    @Test fun `convertType from roll`() {
        assertEquals(viewModel.convertType(NoteType.ROLL), NoteType.TEXT)
    }
}