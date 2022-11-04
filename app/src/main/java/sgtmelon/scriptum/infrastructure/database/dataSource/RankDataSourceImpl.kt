package sgtmelon.scriptum.infrastructure.database.dataSource

import sgtmelon.scriptum.cleanup.data.room.entity.RankEntity
import sgtmelon.scriptum.cleanup.domain.model.item.RankItem
import sgtmelon.scriptum.data.dataSource.database.RankDataSource
import sgtmelon.scriptum.infrastructure.database.dao.RankDao
import sgtmelon.scriptum.infrastructure.database.dao.safe.insertSafe

class RankDataSourceImpl(private val dao: RankDao) : RankDataSource {

    override suspend fun insert(entity: RankEntity): Long? = dao.insertSafe(entity)

    override suspend fun delete(item: RankItem) = dao.delete(item.name)

    override suspend fun update(entity: RankEntity) = dao.update(entity)

    override suspend fun update(list: List<RankEntity>) = dao.update(list)

    override suspend fun getCount(): Int = dao.getCount()

    override suspend fun get(id: Long): RankEntity? = dao.get(id)

    override suspend fun getList(): List<RankEntity> = dao.getList()

    override suspend fun getIdVisibleList(): List<Long> = dao.getIdVisibleList()

    override suspend fun getNameList(): List<String> = dao.getNameList()

    override suspend fun getId(position: Int): Long? = dao.getId(position)
}