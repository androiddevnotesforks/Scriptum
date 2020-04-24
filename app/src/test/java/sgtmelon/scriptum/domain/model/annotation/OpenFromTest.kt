package sgtmelon.scriptum.domain.model.annotation

import org.junit.Assert.assertFalse
import org.junit.Test
import sgtmelon.scriptum.ParentTest

/**
 * Test for [OpenFrom].
 */
class OpenFromTest : ParentTest() {

    @Test fun valueCheck() {
        val list = ArrayList<String>()

        listOf(OpenFrom.INTENT_KEY, OpenFrom.ALARM, OpenFrom.BIND, OpenFrom.INFO).forEach {
            assertFalse(list.contains(it))
            list.add(it)
        }
    }

}