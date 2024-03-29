package sgtmelon.scriptum.infrastructure.screen.note.save

import org.junit.Test
import sgtmelon.tests.uniter.ParentTest

/**
 * Test for [NoteSaveImpl].
 */
class NoteSaveImplTest : ParentTest() {

    @Test fun todo() {
        TODO()
    }

    //    @MockK lateinit var resources: Resources
    //
    //    @MockK lateinit var saveState: NoteSaveState
    //    @MockK lateinit var callback: NoteAutoSaveImpl.Callback
    //
    //    private val saveControl by lazy { NoteAutoSaveImpl(resources, saveState, callback) }
    //
    //    @After override fun tearDown() {
    //        super.tearDown()
    //        confirmVerified(resources, saveState, callback)
    //    }
    //
    //    //region Help functions
    //
    //    private fun mockkPeriodTime(): Pair<SavePeriod, Long> {
    //        val array = IntArray(SavePeriod.values().size) { (100..200).random() }
    //        val savePeriod = mockk<SavePeriod>()
    //        val ordinal = array.indices.random()
    //
    //        every { resources.getIntArray(R.array.pref_note_save_period_array) } returns array
    //        every { saveState.savePeriod } returns savePeriod
    //        every { savePeriod.ordinal } returns ordinal
    //
    //        return savePeriod to array[ordinal].toLong()
    //    }
    //
    //    private fun verifyPeriodTime(period: SavePeriod) {
    //        resources.getIntArray(R.array.pref_note_save_period_array)
    //        saveState.savePeriod
    //        period.ordinal
    //    }
    //
    //    //endregion
    //
    //    @Test fun `not comparable intArray size and enum ordinal`() {
    //        val savePeriod = mockk<SavePeriod>()
    //        val ordinal = Random.nextInt()
    //
    //        every { resources.getIntArray(R.array.pref_note_save_period_array) } returns intArrayOf()
    //        every { saveState.savePeriod } returns savePeriod
    //        every { savePeriod.ordinal } returns ordinal
    //
    //        FastMock.fireExtensions()
    //        every { any<ArrayIndexOutOfBoundsException>().record() } returns mockk()
    //
    //        /** toString needed for somehow call class initialization */
    //        saveControl.toString()
    //
    //        verifySequence {
    //            resources.getIntArray(R.array.pref_note_save_period_array)
    //            saveState.savePeriod
    //            savePeriod.ordinal
    //            any<ArrayIndexOutOfBoundsException>().record()
    //        }
    //    }
    //
    //    @Test fun `normal setup situation with intArray and repeat enum`() {
    //        val (period, _) = mockkPeriodTime()
    //
    //        /** toString needed for somehow call class initialization */
    //        saveControl.toString()
    //
    //        verifySequence {
    //            verifyPeriodTime(period)
    //        }
    //    }
    //
    //    @Test fun isNeedSave() {
    //        val (period, _) = mockkPeriodTime()
    //
    //        assertTrue(saveControl.isNeedSave)
    //
    //        saveControl.isNeedSave = false
    //        assertFalse(saveControl.isNeedSave)
    //
    //        saveControl.isNeedSave = true
    //        assertTrue(saveControl.isNeedSave)
    //
    //        verifySequence {
    //            verifyPeriodTime(period)
    //        }
    //    }
    //
    //    @Test fun `changeAutoSaveWork looping`() {
    //        val times = (2..5).random()
    //        val (period, time) = mockkPeriodTime()
    //        every { saveState.isAutoSaveOn } returns true
    //
    //        saveControl.changeAutoSaveWork(isWork = true)
    //
    //        verify(Ordering.SEQUENCE, timeout = (time + LAG_TIME) * times) {
    //            verifyPeriodTime(period)
    //
    //            repeat(times) {
    //                saveState.isAutoSaveOn
    //                callback.onAutoSave()
    //            }
    //
    //            saveState.isAutoSaveOn
    //        }
    //    }
    //
    //    /**
    //     * Check that second function call will finish first autosave coroutine.
    //     */
    //    @Test fun `changeAutoSaveWork double call`() {
    //        val (period, time) = mockkPeriodTime()
    //        every { saveState.isAutoSaveOn } returns true
    //
    //        saveControl.changeAutoSaveWork(isWork = true)
    //        saveControl.changeAutoSaveWork(isWork = true)
    //
    //        verify(Ordering.SEQUENCE, timeout = time + LAG_TIME) {
    //            verifyPeriodTime(period)
    //
    //            // From first function
    //            saveState.isAutoSaveOn
    //            // From second function
    //            saveState.isAutoSaveOn
    //            callback.onAutoSave()
    //            // From second function but next loop iteration
    //            saveState.isAutoSaveOn
    //        }
    //    }
    //
    //    @Test fun `changeAutoSaveWork without autoSave option`() {
    //        val (period, _) = mockkPeriodTime()
    //        every { saveState.isAutoSaveOn } returns false
    //
    //        saveControl.changeAutoSaveWork(Random.nextBoolean())
    //
    //        verifySequence {
    //            verifyPeriodTime(period)
    //
    //            saveState.isAutoSaveOn
    //        }
    //    }
    //
    //    @Test fun onPauseSave() {
    //        val (period, _) = mockkPeriodTime()
    //        every { saveState.isPauseSaveOn } returns true
    //
    //        saveControl.isNeedSave = false
    //        saveControl.onPauseSave()
    //
    //        saveControl.isNeedSave = true
    //        saveControl.onPauseSave()
    //
    //        verifySequence {
    //            verifyPeriodTime(period)
    //
    //            saveState.isPauseSaveOn
    //
    //            saveState.isPauseSaveOn
    //            callback.onAutoSave()
    //        }
    //    }
    //
    //    @Test fun `onPauseSave without pauseSave option`() {
    //        val (period, _) = mockkPeriodTime()
    //        every { saveState.isPauseSaveOn } returns false
    //
    //        saveControl.onPauseSave()
    //
    //        verifySequence {
    //            verifyPeriodTime(period)
    //            saveState.isPauseSaveOn
    //        }
    //    }
    //
    //    companion object {
    //        private const val LAG_TIME = 30
    //    }
}