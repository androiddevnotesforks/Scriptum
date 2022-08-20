package sgtmelon.scriptum.infrastructure.database.dataSource

import sgtmelon.scriptum.cleanup.data.dataSource.database.RankDataSource
import sgtmelon.scriptum.infrastructure.database.dao.RankDao

class RankDataSourceImpl(private val dao: RankDao) : RankDataSource {
}