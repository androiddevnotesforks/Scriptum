package sgtmelon.scriptum.presentation.control.note.save

import android.content.Context
import android.content.res.Resources
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.spyk
import io.mockk.verifySequence
import org.junit.Assert.assertEquals
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

    @Test fun periodTime() {
        val array = IntArray(size = 5) { Random.nextInt() }
        val index = array.indices.random()

        every { context.resources } returns resources
        every { resources.getIntArray(R.array.pref_note_save_time_array) } returns array

        every { model.autoSaveOn } returns false

        var saveControl = SaveControl(context, model, callback)
        assertEquals(SaveControl.ND_PERIOD, saveControl.periodTime)

        every { model.autoSaveOn } returns true
        every { model.savePeriod } returns -1

        saveControl = SaveControl(context, model, callback)
        assertEquals(SaveControl.ND_PERIOD, saveControl.periodTime)

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
        TODO()
    }

    @Test fun setSaveEvent() {
        TODO()
    }

    @Test fun onSaveRunnable() {
        TODO()
    }

    @Test fun onPauseSave() {
        TODO()
    }

}