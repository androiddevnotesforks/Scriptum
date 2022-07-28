package sgtmelon.scriptum.cleanup.domain.interactor.impl.notification

import io.mockk.coEvery
import io.mockk.coVerifySequence
import io.mockk.confirmVerified
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import io.mockk.spyk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test
import sgtmelon.scriptum.TestData
import sgtmelon.scriptum.infrastructure.model.MelodyItem
import sgtmelon.scriptum.infrastructure.provider.RingtoneProvider
import sgtmelon.scriptum.parent.ParentInteractorTest

/**
 * Test for [SignalInteractor].
 */
@ExperimentalCoroutinesApi
class SignalInteractorTest : ParentInteractorTest() {

    @MockK lateinit var ringtoneProvider: RingtoneProvider

    private val melodyList = TestData.Melody.melodyList

    private val interactor by lazy {
        SignalInteractor(ringtoneProvider)
    }
    private val spyInteractor by lazy { spyk(interactor) }

    @Before override fun setUp() {
        super.setUp()
        assertNull(interactor.melodyList)
    }

    @After override fun tearDown() {
        super.tearDown()
        confirmVerified(ringtoneProvider)
    }


    @Test fun getMelodyList() = startCoTest {
        val list = mockk<List<MelodyItem>>()

        coEvery { ringtoneProvider.getByType(any()) } returns list

        assertEquals(list, interactor.getMelodyList())
        assertEquals(list, interactor.melodyList)

        coEvery { ringtoneProvider.getByType(any()) } returns emptyList()

        assertEquals(list, interactor.getMelodyList())

        coVerifySequence {
            ringtoneProvider.getByType(interactor.typeList)
        }
    }

    @Test fun resetMelodyList() {
        interactor.melodyList = mockk()

        assertNotNull(interactor.melodyList)
        interactor.resetMelodyList()
        assertNull(interactor.melodyList)
    }
}