package sgtmelon.scriptum.infrastructure.model.item

import org.junit.Assert.assertEquals
import org.junit.Test
import sgtmelon.test.common.nextString
import sgtmelon.tests.uniter.ParentTest

/**
 * Test for [MelodyItem].
 */
class MelodyItemTest : ParentTest() {

    @Test fun `second constructor creation of full uri`() {
        val title = nextString()
        val uri = nextString()
        val id = nextString()
        val resultUri = "$uri/$id"

        val item = MelodyItem(title, uri, id)

        assertEquals(item.title, title)
        assertEquals(item.uri, resultUri)
    }
}