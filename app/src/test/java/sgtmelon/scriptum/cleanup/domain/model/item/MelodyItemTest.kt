package sgtmelon.scriptum.cleanup.domain.model.item

import org.junit.Assert.assertEquals
import org.junit.Test
import sgtmelon.common.utils.nextString
import sgtmelon.scriptum.infrastructure.model.MelodyItem
import sgtmelon.scriptum.parent.ParentTest

/**
 * Test for [MelodyItem].
 */
class MelodyItemTest : ParentTest() {

    @Test fun secondConstructor() {
        val title = nextString()
        val uri = nextString()
        val id = nextString()
        val resultUri = "$uri/$id"

        val item = MelodyItem(title, uri, id)

        assertEquals(title, item.title)
        assertEquals(resultUri, item.uri)
    }
}