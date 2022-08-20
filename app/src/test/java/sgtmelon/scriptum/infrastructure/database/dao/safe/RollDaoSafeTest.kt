package sgtmelon.scriptum.infrastructure.database.dao.safe

import io.mockk.confirmVerified
import io.mockk.impl.annotations.MockK
import org.junit.Test
import sgtmelon.scriptum.cleanup.parent.ParentTest
import sgtmelon.scriptum.infrastructure.database.dao.RollDao

/**
 * Test for RollDaoSafe.
 */
@Suppress("DEPRECATION")
class RollDaoSafeTest : ParentTest() {

    @MockK lateinit var dao: RollDao

    override fun tearDown() {
        super.tearDown()
        confirmVerified(dao)
    }

    @Test fun todo() {
        TODO()
    }
}