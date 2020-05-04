package sgtmelon.scriptum.domain.model.key

import org.junit.Assert.assertEquals
import org.junit.Test
import sgtmelon.scriptum.ParentTest

/**
 * Test for [MainPage].
 */
class MainPageTest : ParentTest() {

    @Test fun position() {
        assertEquals(0, MainPage.RANK.ordinal)
        assertEquals(1, MainPage.NOTES.ordinal)
        assertEquals(2, MainPage.BIN.ordinal)
    }

}