package sgtmelon.scriptum.infrastructure.database.dao.safe

import io.mockk.confirmVerified
import io.mockk.impl.annotations.MockK
import org.junit.Test
import sgtmelon.scriptum.cleanup.parent.ParentTest
import sgtmelon.scriptum.infrastructure.database.annotation.DaoConst
import sgtmelon.scriptum.infrastructure.database.dao.NoteDao
import sgtmelon.test.common.OverflowDelegator

/**
 * Test for NoteDaoSafe.
 */
@Suppress("DEPRECATION")
class NoteDaoSafeTest : ParentTest() {

    @MockK lateinit var dao: NoteDao

    private val overflowDelegator = OverflowDelegator(DaoConst.OVERFLOW_COUNT)

    override fun tearDown() {
        super.tearDown()
        confirmVerified(dao)
    }

    @Test fun todo() {
        TODO()
    }
}