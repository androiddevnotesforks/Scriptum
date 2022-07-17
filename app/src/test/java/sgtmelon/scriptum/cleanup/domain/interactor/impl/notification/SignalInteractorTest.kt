package sgtmelon.scriptum.cleanup.domain.interactor.impl.notification

import io.mockk.coEvery
import io.mockk.coVerifySequence
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import io.mockk.spyk
import io.mockk.verifySequence
import kotlin.random.Random
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.After
import org.junit.Assert.assertArrayEquals
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test
import sgtmelon.common.utils.nextString
import sgtmelon.scriptum.TestData
import sgtmelon.scriptum.cleanup.presentation.control.system.callback.IRingtoneControl
import sgtmelon.scriptum.getRandomSize
import sgtmelon.scriptum.infrastructure.converter.SignalConverter
import sgtmelon.scriptum.infrastructure.model.MelodyItem
import sgtmelon.scriptum.infrastructure.model.state.SignalState
import sgtmelon.scriptum.infrastructure.preferences.Preferences
import sgtmelon.scriptum.parent.ParentInteractorTest

/**
 * Test for [SignalInteractor].
 */
@ExperimentalCoroutinesApi
class SignalInteractorTest : ParentInteractorTest() {

    @MockK lateinit var ringtoneControl: IRingtoneControl
    @MockK lateinit var preferences: Preferences
    @MockK lateinit var signalConverter: SignalConverter

    private val melodyList = TestData.Melody.melodyList

    private val interactor by lazy {
        SignalInteractor(ringtoneControl, preferences, signalConverter)
    }
    private val spyInteractor by lazy { spyk(interactor) }

    @Before override fun setup() {
        super.setup()
        assertNull(interactor.melodyList)
    }

    @After override fun tearDown() {
        super.tearDown()
        confirmVerified(ringtoneControl, preferences, signalConverter)
    }


    @Test fun getTypeCheck() {
        val value = nextString()
        val size = getRandomSize()
        val typeCheck = BooleanArray(size) { Random.nextBoolean() }

        every { preferences.signal } returns value
        every { signalConverter.toArray(value) } returns typeCheck

        assertArrayEquals(interactor.typeCheck, typeCheck)

        verifySequence {
            preferences.signal
            signalConverter.toArray(value)
        }
    }

    @Test fun getState() {
        val value = nextString()
        val state = mockk<SignalState>()

        every { preferences.signal } returns value
        every { signalConverter.toState(value) } returns state
        assertEquals(interactor.state, state)

        verifySequence {
            preferences.signal
            signalConverter.toState(value)
        }
    }


    @Test fun getMelodyList() = startCoTest {
        val list = mockk<List<MelodyItem>>()

        coEvery { ringtoneControl.getByType(any()) } returns list

        assertEquals(list, interactor.getMelodyList())
        assertEquals(list, interactor.melodyList)

        coEvery { ringtoneControl.getByType(any()) } returns emptyList()

        assertEquals(list, interactor.getMelodyList())

        coVerifySequence {
            ringtoneControl.getByType(interactor.typeList)
        }
    }

    @Test fun resetMelodyList() {
        interactor.melodyList = mockk()

        assertNotNull(interactor.melodyList)
        interactor.resetMelodyList()
        assertNull(interactor.melodyList)
    }

    @Test fun getMelodyUri() = startCoTest {
        val wrongUri = nextString()
        val wrongReturnUri = melodyList.first().uri
        val goodUri = melodyList.random().uri

        coEvery { spyInteractor.getMelodyList() } returns emptyList()

        every { preferences.melodyUri } returns ""
        assertNull(spyInteractor.getMelodyUri())

        coEvery { spyInteractor.getMelodyList() } returns melodyList

        every { preferences.melodyUri } returns ""
        assertEquals(wrongReturnUri, spyInteractor.getMelodyUri())

        every { preferences.melodyUri } returns wrongUri
        assertEquals(wrongReturnUri, spyInteractor.getMelodyUri())

        every { preferences.melodyUri } returns goodUri
        assertEquals(goodUri, spyInteractor.getMelodyUri())

        coVerifySequence {
            spyInteractor.getMelodyUri()
            spyInteractor.getMelodyList()
            preferences.melodyUri

            repeat(times = 2) {
                spyInteractor.getMelodyUri()
                spyInteractor.getMelodyList()
                preferences.melodyUri
                preferences.melodyUri = wrongReturnUri
            }

            spyInteractor.getMelodyUri()
            spyInteractor.getMelodyList()
            preferences.melodyUri
        }
    }

    @Test fun setMelodyUri() = startCoTest {
        val wrongTitle = nextString()
        val wrongItem = melodyList.first()
        val melodyItem = melodyList.random()

        coEvery { spyInteractor.getMelodyList() } returns emptyList()

        assertNull(spyInteractor.setMelodyUri(wrongTitle))

        coEvery { spyInteractor.getMelodyList() } returns melodyList

        assertEquals(wrongItem.title, spyInteractor.setMelodyUri(wrongTitle))
        assertEquals(melodyItem.title, spyInteractor.setMelodyUri(melodyItem.title))

        coVerifySequence {
            spyInteractor.setMelodyUri(wrongTitle)
            spyInteractor.getMelodyList()

            spyInteractor.setMelodyUri(wrongTitle)
            spyInteractor.getMelodyList()
            preferences.melodyUri = wrongItem.uri

            spyInteractor.setMelodyUri(melodyItem.title)
            spyInteractor.getMelodyList()
            preferences.melodyUri = melodyItem.uri
        }
    }

    @Test fun getMelodyCheck() = startCoTest {
        val index = melodyList.indices.random()

        coEvery { spyInteractor.getMelodyList() } returns emptyList()
        coEvery { spyInteractor.getMelodyUri() } returns nextString()

        assertNull(spyInteractor.getMelodyCheck())

        coEvery { spyInteractor.getMelodyList() } returns melodyList
        coEvery { spyInteractor.getMelodyUri() } returns nextString()

        assertNull(spyInteractor.getMelodyCheck())

        coEvery { spyInteractor.getMelodyUri() } returns melodyList[index].uri

        assertEquals(index, spyInteractor.getMelodyCheck())

        coVerifySequence {
            repeat(times = 3) {
                spyInteractor.getMelodyCheck()
                spyInteractor.getMelodyList()
                spyInteractor.getMelodyUri()
            }
        }
    }
}