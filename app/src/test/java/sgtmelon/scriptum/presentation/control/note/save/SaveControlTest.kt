package sgtmelon.scriptum.presentation.control.note.save

import android.content.Context
import android.content.res.Resources
import android.os.Handler
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import io.mockk.spyk
import io.mockk.verifySequence
import org.junit.Assert.*
import org.junit.Test
import sgtmelon.scriptum.ParentTest
import sgtmelon.scriptum.R
import kotlin.random.Random

/**
 * Test for [SaveControl].
 */
class SaveControlTest : ParentTest() {

    @MockK lateinit var context: Context
    @MockK lateinit var resources: Resources

    @MockK lateinit var model: SaveControl.Model
    @MockK lateinit var callback: SaveControl.Callback

    private val saveControl by lazy { SaveControl(context, model, callback) }
    private val spySaveControl by lazy { spyk(saveControl) }

    override fun setUp() {
        super.setUp()

        every { model.autoSaveOn } returns false
    }

    @Test fun periodTime() {
        val array = IntArray(size = 5) { Random.nextInt() }
        val index = array.indices.random()

        every { context.resources } returns resources
        every { resources.getIntArray(R.array.pref_note_save_time_array) } returns array

        every { model.autoSaveOn } returns false

        var saveControl = SaveControl(context, model, callback)
        assertNull(saveControl.periodTime)

        every { model.autoSaveOn } returns true
        every { model.savePeriod } returns -1

        saveControl = SaveControl(context, model, callback)
        assertNull(saveControl.periodTime)

        every { model.savePeriod } returns index

        saveControl = SaveControl(context, model, callback)
        assertEquals(array[index], saveControl.periodTime)

        verifySequence {
            model.autoSaveOn

            repeat(times = 2) {
                model.autoSaveOn
                context.resources
                resources.getIntArray(R.array.pref_note_save_time_array)
                model.savePeriod
            }
        }
    }

    @Test fun needSave() {
        assertTrue(saveControl.needSave)

        saveControl.needSave = false
        assertFalse(saveControl.needSave)

        saveControl.needSave = true
        assertTrue(saveControl.needSave)

        verifySequence {
            model.autoSaveOn
        }
    }

    @Test fun setSaveEvent() {
        val array = IntArray(size = 5) { Random.nextInt() }
        val index = array.indices.random()
        val handler = mockk<Handler>(relaxUnitFun = true)

        every { model.autoSaveOn } returns false

        val saveControl = SaveControl(context, model, callback)

        saveControl.setSaveEvent(Random.nextBoolean())

        every { model.autoSaveOn } returns true
        every { context.resources } returns resources
        every { resources.getIntArray(R.array.pref_note_save_time_array) } returns array
        every { model.savePeriod } returns -1

        val firstSpySaveControl = spyk(SaveControl(context, model, callback))
        firstSpySaveControl.saveHandler = handler

        firstSpySaveControl.setSaveEvent(isWork = false)
        firstSpySaveControl.setSaveEvent(isWork = true)

        every { model.savePeriod } returns index
        every { handler.postDelayed(any(), array[index].toLong()) } returns true

        val secondSpySaveControl = spyk(SaveControl(context, model, callback))
        secondSpySaveControl.saveHandler = handler

        secondSpySaveControl.setSaveEvent(isWork = false)
        secondSpySaveControl.setSaveEvent(isWork = true)

        verifySequence {
            model.autoSaveOn
            model.autoSaveOn


            model.autoSaveOn
            model.savePeriod
            firstSpySaveControl.saveHandler = handler

            firstSpySaveControl.setSaveEvent(isWork = false)
            model.autoSaveOn
            handler.removeCallbacksAndMessages(null)

            firstSpySaveControl.setSaveEvent(isWork = true)
            model.autoSaveOn
            handler.removeCallbacksAndMessages(null)


            model.autoSaveOn
            model.savePeriod
            secondSpySaveControl.saveHandler = handler

            secondSpySaveControl.setSaveEvent(isWork = false)
            model.autoSaveOn
            handler.removeCallbacksAndMessages(null)

            secondSpySaveControl.setSaveEvent(isWork = true)
            model.autoSaveOn
            handler.removeCallbacksAndMessages(null)
            handler.postDelayed(any(), array[index].toLong())
        }
    }

    @Test fun onPauseSave() {
        every { model.pauseSaveOn } returns false
        saveControl.onPauseSave()

        every { model.pauseSaveOn } returns true
        saveControl.needSave = false
        saveControl.onPauseSave()

        saveControl.needSave = true
        saveControl.onPauseSave()

        verifySequence {
            model.autoSaveOn

            repeat(times = 2) { model.pauseSaveOn }

            model.pauseSaveOn
            callback.onResultSaveControl()
        }
    }

}