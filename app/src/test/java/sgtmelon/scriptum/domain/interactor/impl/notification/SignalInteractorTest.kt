package sgtmelon.scriptum.domain.interactor.impl.notification

import io.mockk.*
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Assert.*
import org.junit.Test
import sgtmelon.extension.nextString
import sgtmelon.scriptum.ParentInteractorTest
import sgtmelon.scriptum.TestData
import sgtmelon.scriptum.data.repository.preference.IPreferenceRepo
import sgtmelon.scriptum.data.room.converter.type.IntConverter
import sgtmelon.scriptum.domain.model.annotation.Signal
import sgtmelon.scriptum.domain.model.state.SignalState
import sgtmelon.scriptum.presentation.control.system.callback.IRingtoneControl
import kotlin.random.Random

/**
 * Test for [SignalInteractor].
 */
@ExperimentalCoroutinesApi
class SignalInteractorTest : ParentInteractorTest() {

    @MockK lateinit var ringtoneControl: IRingtoneControl
    @MockK lateinit var preferenceRepo: IPreferenceRepo
    @MockK lateinit var intConverter: IntConverter

    private val melodyList = TestData.Melody.melodyList

    private val interactor by lazy {
        SignalInteractor(ringtoneControl, preferenceRepo, intConverter)
    }
    private val spyInteractor by lazy { spyk(interactor) }

    override fun setUp() {
        super.setUp()

        assertNull(interactor.melodyList)
    }


    @Test fun getTypeCheck() {
        val signal = Random.nextInt()
        val typeCheck = BooleanArray(size = 5) { Random.nextBoolean() }

        every { preferenceRepo.signal } returns signal
        every { intConverter.toArray(signal, Signal.digitCount) } returns typeCheck

        assertArrayEquals(typeCheck, interactor.typeCheck)

        verifySequence {
            preferenceRepo.signal
            intConverter.toArray(signal, Signal.digitCount)
        }
    }

    @Test fun getState() {
        val signal = Random.nextInt()
        val isMelody = Random.nextBoolean()
        val isVibration = Random.nextBoolean()

        val typeCheckArray = booleanArrayOf(isMelody, isVibration)
        val state = SignalState(isMelody, isVibration)

        every { preferenceRepo.signal } returns signal
        every { intConverter.toArray(signal, Signal.digitCount) } returns typeCheckArray

        assertEquals(state, interactor.state)

        verifySequence {
            preferenceRepo.signal
            intConverter.toArray(signal, Signal.digitCount)
        }
    }


    @Test fun getMelodyList() = startCoTest {
        coEvery { ringtoneControl.getByType(any()) } returns melodyList

        assertEquals(melodyList, interactor.getMelodyList())
        assertEquals(melodyList, interactor.melodyList)

        coVerifySequence {
            ringtoneControl.getByType(interactor.typeList)
        }

        coEvery { ringtoneControl.getByType(any()) } returns emptyList()

        assertEquals(melodyList, interactor.getMelodyList())

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

        every { preferenceRepo.melodyUri } returns ""
        assertNull(spyInteractor.getMelodyUri())

        coEvery { spyInteractor.getMelodyList() } returns melodyList

        every { preferenceRepo.melodyUri } returns ""
        assertEquals(wrongReturnUri, spyInteractor.getMelodyUri())

        every { preferenceRepo.melodyUri } returns wrongUri
        assertEquals(wrongReturnUri, spyInteractor.getMelodyUri())

        every { preferenceRepo.melodyUri } returns goodUri
        assertEquals(goodUri, spyInteractor.getMelodyUri())

        coVerifySequence {
            spyInteractor.getMelodyUri()
            spyInteractor.getMelodyList()
            preferenceRepo.melodyUri

            repeat(times = 2) {
                spyInteractor.getMelodyUri()
                spyInteractor.getMelodyList()
                preferenceRepo.melodyUri
                preferenceRepo.melodyUri = wrongReturnUri
            }

            spyInteractor.getMelodyUri()
            spyInteractor.getMelodyList()
            preferenceRepo.melodyUri
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
            preferenceRepo.melodyUri = wrongItem.uri

            spyInteractor.setMelodyUri(melodyItem.title)
            spyInteractor.getMelodyList()
            preferenceRepo.melodyUri = melodyItem.uri
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