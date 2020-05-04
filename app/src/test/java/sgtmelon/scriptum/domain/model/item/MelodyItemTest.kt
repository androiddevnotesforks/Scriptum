package sgtmelon.scriptum.domain.model.item

import org.junit.Assert.assertEquals
import org.junit.Test
import sgtmelon.scriptum.ParentTest

/**
 * Test for [MelodyItem].
 */
class MelodyItemTest : ParentTest() {

    @Test fun secondConstructor() {
        val item = MelodyItem(TITLE, URI, ID)

        assertEquals(TITLE, item.title)
        assertEquals(RESULT_URI, item.uri)
    }


    companion object {
        private const val PREFIX = "TEST"

        private const val TITLE = "${PREFIX}_TITLE"
        private const val URI = "${PREFIX}_URI"
        private const val ID = "${PREFIX}_ID"

        private const val RESULT_URI = "${URI}/${ID}"
    }

}