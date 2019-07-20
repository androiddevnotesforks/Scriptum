package sgtmelon.scriptum.test.integration.dao

import androidx.test.ext.junit.runners.AndroidJUnit4
import junit.framework.Assert.assertNull
import org.junit.Test
import org.junit.runner.RunWith
import sgtmelon.scriptum.room.dao.RankDao
import sgtmelon.scriptum.test.ParentIntegrationTest
import kotlin.random.Random

/**
 * Integration test for [RankDao]
 *
 * @author SerjantArbuz
 */
@RunWith(AndroidJUnit4::class)
class RankDaoTest : ParentIntegrationTest() {

    // TODO тест записи rankEntity с одинаковыми именами



    @Test fun getOnWrongId() = inTheRoom { assertNull(getRankDao()[arrayListOf(Random.nextLong())]) }

}