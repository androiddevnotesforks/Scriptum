package sgtmelon.scriptum.cleanup.domain.interactor.impl.note

import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Test
import sgtmelon.scriptum.FastTest
import sgtmelon.scriptum.infrastructure.preferences.IPreferenceRepo
import sgtmelon.scriptum.parent.ParentInteractorTest

/**
 * Test for [NoteInteractor].
 */
@ExperimentalCoroutinesApi
class NoteInteractorTest : ParentInteractorTest() {

    @MockK lateinit var preferenceRepo: IPreferenceRepo

    private val interactor by lazy { NoteInteractor(preferenceRepo) }

    @Test fun getDefaultColor() = FastTest.getDefaultColor(preferenceRepo) {
        interactor.defaultColor
    }
}