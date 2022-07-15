package sgtmelon.scriptum.cleanup.domain.interactor.impl.note

import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Test
import sgtmelon.scriptum.FastTest
import sgtmelon.scriptum.infrastructure.preferences.AppPreferences
import sgtmelon.scriptum.parent.ParentInteractorTest

/**
 * Test for [NoteInteractor].
 */
@ExperimentalCoroutinesApi
class NoteInteractorTest : ParentInteractorTest() {

    @MockK lateinit var preferenceRepo: AppPreferences

    private val interactor by lazy { NoteInteractor(preferenceRepo) }

    @Test fun getDefaultColor() = FastTest.getDefaultColor(preferenceRepo) {
        interactor.defaultColor
    }
}