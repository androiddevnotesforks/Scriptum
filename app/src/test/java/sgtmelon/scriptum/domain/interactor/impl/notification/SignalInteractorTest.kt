package sgtmelon.scriptum.domain.interactor.impl.notification

import android.media.RingtoneManager
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.verifySequence
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Assert.assertArrayEquals
import org.junit.Assert.assertEquals
import org.junit.Test
import sgtmelon.extension.nextString
import sgtmelon.scriptum.ParentInteractorTest
import sgtmelon.scriptum.data.repository.preference.IPreferenceRepo
import sgtmelon.scriptum.data.room.converter.type.IntConverter
import sgtmelon.scriptum.domain.model.annotation.Signal
import sgtmelon.scriptum.domain.model.item.MelodyItem
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

    private val interactor by lazy {
        SignalInteractor(ringtoneControl, preferenceRepo, intConverter)
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
        val firstCheck = 2
        val firstState = SignalState(isMelody = false, isVibration = true)

        val secondCheck = 1
        val secondState = SignalState(isMelody = true, isVibration = false)

        every { preferenceRepo.signal } returns firstCheck
        assertEquals(firstState, interactor.state)

        every { preferenceRepo.signal } returns secondCheck
        assertEquals(secondState, interactor.state)

        verifySequence {
            preferenceRepo.signal
            preferenceRepo.signal
        }
    }

    @Test fun getMelodyUri() {
        val wrongUri = Random.nextString()
        val wrongReturnUri = melodyList.first().uri

        val goodUri = melodyList.random().uri

        setEveryRingtone()

        every { preferenceRepo.melodyUri } returns ""
        assertEquals(wrongReturnUri, interactor.getMelodyUri())

        every { preferenceRepo.melodyUri } returns wrongUri
        assertEquals(wrongReturnUri, interactor.getMelodyUri())

        every { preferenceRepo.melodyUri } returns goodUri
        assertEquals(goodUri, interactor.getMelodyUri())

        verifySequence {
            repeat(times = 2) {
                ringtoneControl.getByType(RingtoneManager.TYPE_ALARM)
                ringtoneControl.getByType(RingtoneManager.TYPE_RINGTONE)

                preferenceRepo.melodyUri
                preferenceRepo.melodyUri = wrongReturnUri
            }

            ringtoneControl.getByType(RingtoneManager.TYPE_ALARM)
            ringtoneControl.getByType(RingtoneManager.TYPE_RINGTONE)

            preferenceRepo.melodyUri
        }
    }

    @Test fun setMelodyUri() {
        val wrongUri = Random.nextString()
        val goodUri = melodyList.random().uri

        setEveryRingtone()

        interactor.setMelodyUri(wrongUri)
        interactor.setMelodyUri(goodUri)

        verifySequence {
            ringtoneControl.getByType(RingtoneManager.TYPE_ALARM)
            ringtoneControl.getByType(RingtoneManager.TYPE_RINGTONE)

            preferenceRepo.melodyUri = melodyList.first().uri

            ringtoneControl.getByType(RingtoneManager.TYPE_ALARM)
            ringtoneControl.getByType(RingtoneManager.TYPE_RINGTONE)

            preferenceRepo.melodyUri = goodUri
        }
    }

    @Test fun getMelodyCheck() {
        val wrongUri = Random.nextString()
        val wrongReturnUri = melodyList.first().uri

        val goodUri = melodyList.random().uri

        val wrongIndex = 0
        val goodIndex = melodyList.indexOfFirst { it.uri == goodUri }

        setEveryRingtone()

        every { preferenceRepo.melodyUri } returns ""
        assertEquals(wrongIndex, interactor.melodyCheck)

        every { preferenceRepo.melodyUri } returns wrongUri
        assertEquals(wrongIndex, interactor.melodyCheck)

        every { preferenceRepo.melodyUri } returns goodUri
        assertEquals(goodIndex, interactor.melodyCheck)

        verifySequence {
            repeat(times = 2) {
                ringtoneControl.getByType(RingtoneManager.TYPE_ALARM)
                ringtoneControl.getByType(RingtoneManager.TYPE_RINGTONE)

                preferenceRepo.melodyUri
                preferenceRepo.melodyUri = wrongReturnUri
            }

            ringtoneControl.getByType(RingtoneManager.TYPE_ALARM)
            ringtoneControl.getByType(RingtoneManager.TYPE_RINGTONE)

            preferenceRepo.melodyUri
        }
    }

    @Test fun getMelodyList() {
        setEveryRingtone()

        assertEquals(melodyList, interactor.melodyList)

        verifySequence {
            ringtoneControl.getByType(RingtoneManager.TYPE_ALARM)
            ringtoneControl.getByType(RingtoneManager.TYPE_RINGTONE)
        }
    }

    private fun setEveryRingtone() {
        every { ringtoneControl.getByType(RingtoneManager.TYPE_ALARM) } returns alarmList
        every { ringtoneControl.getByType(RingtoneManager.TYPE_RINGTONE) } returns ringtoneList
    }


    private val alarmList = listOf(
            MelodyItem(title = "alarm_title_0", uri = "alarm_uri_0", id = "alarm_id_0"),
            MelodyItem(title = "alarm_title_1", uri = "alarm_uri_1", id = "alarm_id_1")
    )

    private val ringtoneList = listOf(
            MelodyItem(title = "ringtone_title_0", uri = "ringtone_uri_0", id = "ringtone_id_0"),
            MelodyItem(title = "ringtone_title_1", uri = "ringtone_uri_1", id = "ringtone_id_1")
    )

    private val melodyList = ArrayList<MelodyItem>().apply {
        addAll(alarmList)
        addAll(ringtoneList)
    }.sortedBy { it.title }

}