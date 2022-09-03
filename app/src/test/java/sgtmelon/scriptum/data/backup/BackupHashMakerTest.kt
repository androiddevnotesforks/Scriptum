package sgtmelon.scriptum.data.backup

import org.junit.Assert.assertEquals
import org.junit.Test
import sgtmelon.scriptum.cleanup.parent.ParentTest

/**
 * Test for [BackupHashMaker].
 */
class BackupHashMakerTest : ParentTest() {

    private val hashMaker = BackupHashMaker()

    /**
     * Get results from website: https://emn178.github.io/online-tools/sha256.html
     */
    @Test fun get() {
        val map = mapOf(
            "" to "e3b0c44298fc1c149afbf4c8996fb92427ae41e4649b934ca495991b7852b855",
            "Тest of SHA256." to "86cdf15b207ef2bca14242cb700f6a67038431ea3af004b2741527ee75135d4c",
            "My name is Alexey" to "262d410b26232a1e948b317aa013b3210a324db7c442460410ec0e8ede35a990",
            "Подушечка из манго самая мягкая" to "62b8d1d61be6acb1a76f55a4d2fd866ef8dd68cd3a6fb23b9f032b7f376f09c7"
        )

        for (it in map) {
            assertEquals(hashMaker.get(it.key), it.value)
        }
    }
}