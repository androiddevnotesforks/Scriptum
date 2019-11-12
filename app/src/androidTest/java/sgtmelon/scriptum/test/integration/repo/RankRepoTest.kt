package sgtmelon.scriptum.test.integration.repo

import androidx.test.ext.junit.runners.AndroidJUnit4

import org.junit.Test
import org.junit.Assert.*
import org.junit.runner.RunWith
import sgtmelon.scriptum.repository.rank.IRankRepo
import sgtmelon.scriptum.repository.rank.RankRepo
import sgtmelon.scriptum.test.ParentIntegrationTest

/**
 * Integration test for [RankRepo]
 */
@RunWith(AndroidJUnit4::class)
class RankRepoTest : ParentIntegrationTest()  {

    private val iRepo: IRankRepo = RankRepo(context)

    @Test fun insertWithUnique() {
        val name = data.uniqueString

        assertEquals(1, iRepo.insert(name))
        assertEquals(-1, iRepo.insert(name))
    }

    @Test fun getList() {
        TODO(reason = "#TEST write test")
    }

    @Test fun delete() {
        TODO(reason = "#TEST write test")
    }

    @Test fun update() {
        TODO(reason = "#TEST write test")
    }

    @Test fun updatePosition() {
        TODO(reason = "#TEST write test")
    }

    @Test fun updateConnection() {
        TODO(reason = "#TEST write test")
    }

}