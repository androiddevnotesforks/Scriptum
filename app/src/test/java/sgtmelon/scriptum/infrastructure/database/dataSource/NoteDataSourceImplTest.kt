package sgtmelon.scriptum.infrastructure.database.dataSource

import io.mockk.confirmVerified
import io.mockk.impl.annotations.MockK
import org.junit.Test
import sgtmelon.scriptum.cleanup.parent.ParentTest
import sgtmelon.scriptum.infrastructure.database.dao.NoteDao

/**
 * Test for [NoteDataSourceImpl].
 */
class NoteDataSourceImplTest : ParentTest() {

    @MockK lateinit var dao: NoteDao

    private val dataSource by lazy { NoteDataSourceImpl(dao) }

    override fun tearDown() {
        super.tearDown()
        confirmVerified(dao)
    }

    @Test fun insert() {
        TODO()
    }

    @Test fun `delete by entity`() {
        TODO()
    }

    @Test fun `delete by list`() {
        TODO()
    }

    @Test fun `update by entity`() {
        TODO()
    }

    @Test fun `update by list`() {
        TODO()
    }

    @Test fun getRankVisibleCount() {
        TODO()
    }

    @Test fun getBindCount() {
        TODO()
    }

    @Test fun get() {
        TODO()
    }

    @Test fun `getList by idList`() {
        TODO()
    }

    @Test fun `getList by isBin`() {
        TODO()
    }

    @Test fun `getList by sort`() {
        TODO()
    }
}