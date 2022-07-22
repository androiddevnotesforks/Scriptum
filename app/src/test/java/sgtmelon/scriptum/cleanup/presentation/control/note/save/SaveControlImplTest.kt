package sgtmelon.scriptum.cleanup.presentation.control.note.save

import android.content.res.Resources
import android.os.Handler
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import io.mockk.spyk
import io.mockk.verifySequence
import kotlin.random.Random
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import sgtmelon.scriptum.R
import sgtmelon.scriptum.infrastructure.model.state.NoteSaveState
import sgtmelon.scriptum.parent.ParentTest

/**
 * Test for [SaveControlImpl].
 */
class SaveControlImplTest : ParentTest() {

    @MockK lateinit var resources: Resources

    @MockK lateinit var saveState: NoteSaveState
    @MockK lateinit var callback: SaveControlImpl.Callback

    private val saveControl by lazy { SaveControlImpl(resources, saveState, callback) }
    private val spySaveControl by lazy { spyk(saveControl) }

    @Before override fun setup() {
        super.setup()

        every { saveState.isAutoSaveOn } returns false
    }

    @Test fun periodTime() {
        val array = IntArray(size = 5) { Random.nextInt() }
        val index = array.indices.random()

        every { resources.getIntArray(R.array.pref_note_save_time_array) } returns array

        every { saveState.isAutoSaveOn } returns false

        var saveControl = SaveControlImpl(resources, saveState, callback)
        assertNull(saveControl.periodTime)

        every { saveState.isAutoSaveOn } returns true
        every { saveState.savePeriod } returns -1

        saveControl = SaveControlImpl(resources, saveState, callback)
        assertNull(saveControl.periodTime)

        every { saveState.savePeriod } returns index

        saveControl = SaveControlImpl(resources, saveState, callback)
        assertEquals(array[index], saveControl.periodTime)

        verifySequence {
            saveState.isAutoSaveOn

            repeat(times = 2) {
                saveState.isAutoSaveOn
                resources.getIntArray(R.array.pref_note_save_time_array)
                saveState.savePeriod
            }
        }
    }

    @Test fun needSave() {
        assertTrue(saveControl.isNeedSave)

        saveControl.isNeedSave = false
        assertFalse(saveControl.isNeedSave)

        saveControl.isNeedSave = true
        assertTrue(saveControl.isNeedSave)

        verifySequence {
            saveState.isAutoSaveOn
        }
    }

    @Test fun setSaveEvent() {
        val array = IntArray(size = 5) { Random.nextInt() }
        val index = array.indices.random()
        val handler = mockk<Handler>(relaxUnitFun = true)

        every { saveState.isAutoSaveOn } returns false

        val saveControl = SaveControlImpl(resources, saveState, callback)

        saveControl.changeAutoSaveWork(Random.nextBoolean())

        every { saveState.isAutoSaveOn } returns true
        every { resources.getIntArray(R.array.pref_note_save_time_array) } returns array
        every { saveState.savePeriod } returns -1

        val firstSpySaveControl = spyk(SaveControlImpl(resources, saveState, callback))
        firstSpySaveControl.saveHandler = handler

        firstSpySaveControl.changeAutoSaveWork(isWork = false)
        firstSpySaveControl.changeAutoSaveWork(isWork = true)

        every { saveState.savePeriod } returns index
        every { handler.postDelayed(any(), array[index].toLong()) } returns true

        val secondSpySaveControl = spyk(SaveControlImpl(resources, saveState, callback))
        secondSpySaveControl.saveHandler = handler

        secondSpySaveControl.changeAutoSaveWork(isWork = false)
        secondSpySaveControl.changeAutoSaveWork(isWork = true)

        verifySequence {
            saveState.isAutoSaveOn
            saveState.isAutoSaveOn


            saveState.isAutoSaveOn
            saveState.savePeriod
            firstSpySaveControl.saveHandler = handler

            firstSpySaveControl.changeAutoSaveWork(isWork = false)
            saveState.isAutoSaveOn
            handler.removeCallbacksAndMessages(null)

            firstSpySaveControl.changeAutoSaveWork(isWork = true)
            saveState.isAutoSaveOn
            handler.removeCallbacksAndMessages(null)


            saveState.isAutoSaveOn
            saveState.savePeriod
            secondSpySaveControl.saveHandler = handler

            secondSpySaveControl.changeAutoSaveWork(isWork = false)
            saveState.isAutoSaveOn
            handler.removeCallbacksAndMessages(null)

            secondSpySaveControl.changeAutoSaveWork(isWork = true)
            saveState.isAutoSaveOn
            handler.removeCallbacksAndMessages(null)
            handler.postDelayed(any(), array[index].toLong())
        }
    }

    @Test fun onPauseSave() {
        every { saveState.isPauseSaveOn } returns false
        saveControl.onPauseSave()

        every { saveState.isPauseSaveOn } returns true
        saveControl.isNeedSave = false
        saveControl.onPauseSave()

        saveControl.isNeedSave = true
        saveControl.onPauseSave()

        verifySequence {
            saveState.isAutoSaveOn

            repeat(times = 2) { saveState.isPauseSaveOn }

            saveState.isPauseSaveOn
            callback.onResultSaveControl()
        }
    }

}