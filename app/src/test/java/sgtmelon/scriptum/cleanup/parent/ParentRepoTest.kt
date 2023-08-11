package sgtmelon.scriptum.cleanup.parent

import io.mockk.confirmVerified
import io.mockk.impl.annotations.MockK
import org.junit.After
import sgtmelon.scriptum.data.dataSource.database.AlarmDataSource
import sgtmelon.scriptum.data.dataSource.database.NoteDataSource
import sgtmelon.scriptum.data.dataSource.database.RankDataSource
import sgtmelon.scriptum.data.dataSource.database.RollDataSource
import sgtmelon.scriptum.data.dataSource.database.RollVisibleDataSource

/**
 * Parent test class for repositories who works with dataSource's.
 */
abstract class ParentRepoTest : sgtmelon.tests.uniter.ParentTest() {

    @MockK lateinit var noteDataSource: NoteDataSource
    @MockK lateinit var rollDataSource: RollDataSource
    @MockK lateinit var rollVisibleDataSource: RollVisibleDataSource
    @MockK lateinit var rankDataSource: RankDataSource
    @MockK lateinit var alarmDataSource: AlarmDataSource

    @After override fun tearDown() {
        super.tearDown()
        confirmVerified(
            noteDataSource, rollDataSource, rollVisibleDataSource,
            rankDataSource, alarmDataSource
        )
    }
}