package sgtmelon.scriptum.cleanup.domain.interactor.impl.note

import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Test
import sgtmelon.scriptum.FastTest
import sgtmelon.scriptum.infrastructure.preferences.Preferences
import sgtmelon.scriptum.parent.ParentInteractorTest

/**
 * Test for [NoteInteractor].
 */
@ExperimentalCoroutinesApi
class NoteInteractorTest : ParentInteractorTest() {

    @MockK lateinit var preferences: Preferences

    private val interactor by lazy { NoteInteractor(preferences) }

    @Test fun getDefaultColor() = FastTest.getDefaultColor(preferences) {
        interactor.defaultColor
    }
}