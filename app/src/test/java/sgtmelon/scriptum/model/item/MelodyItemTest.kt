package sgtmelon.scriptum.model.item

import org.junit.Assert.*
import org.junit.Test

/**
 * Test for [MelodyItem]
 */
class MelodyItemTest {

    @Test fun secondConstructor() {
        val item = MelodyItem(TITLE, URI, ID)

        assertEquals(TITLE, item.title)
        assertEquals(RESULT_URI, item.uri)
    }

    private companion object {
        const val PREFIX = "TEST"

        const val TITLE = "${PREFIX}_TITLE"
        const val URI = "${PREFIX}_URI"
        const val ID = "${PREFIX}_ID"

        const val RESULT_URI = "${URI}/${ID}"
    }

}