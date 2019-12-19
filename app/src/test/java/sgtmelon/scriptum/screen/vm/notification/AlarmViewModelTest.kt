package sgtmelon.scriptum.screen.vm.notification

import org.junit.Assert.*
import org.junit.Test
import sgtmelon.scriptum.R
import sgtmelon.scriptum.model.annotation.Repeat
import sgtmelon.scriptum.screen.vm.notification.AlarmViewModel.Companion.getRepeatById
import kotlin.random.Random


/**
 * Test for [AlarmViewModel]
 */
class AlarmViewModelTest {

    @Test fun getRepeatById() {
        assertEquals(Repeat.MIN_10, getRepeatById(R.id.item_repeat_0))
        assertEquals(Repeat.MIN_30, getRepeatById(R.id.item_repeat_1))
        assertEquals(Repeat.MIN_60, getRepeatById(R.id.item_repeat_2))
        assertEquals(Repeat.MIN_180, getRepeatById(R.id.item_repeat_3))
        assertEquals(Repeat.MIN_1440, getRepeatById(R.id.item_repeat_4))
        assertNull(getRepeatById(Random.nextInt()))
    }
    
}