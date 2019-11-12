package sgtmelon.scriptum.interactor.main

import android.content.Context

import sgtmelon.scriptum.interactor.ParentInteractor
import sgtmelon.scriptum.interactor.callback.main.IRankInteractor
import sgtmelon.scriptum.model.item.RankItem
import sgtmelon.scriptum.repository.rank.IRankRepo
import sgtmelon.scriptum.repository.rank.RankRepo
import sgtmelon.scriptum.room.converter.RankConverter
import sgtmelon.scriptum.room.entity.NoteEntity
import sgtmelon.scriptum.screen.vm.main.RankViewModel

/**
 * Interactor for [RankViewModel]
 */
class RankInteractor(context: Context) : ParentInteractor(context), IRankInteractor {

    // TODO #TEST write unit test

    private val converter = RankConverter()

    private val iRankRepo: IRankRepo = RankRepo(context)

    override fun insert(name: String): RankItem {
        val id = iRankRepo.insert(name)

        return RankItem(id, name = name)
    }

    override fun getList() = converter.toItem(iRankRepo.getList())

    override fun delete(item: RankItem) = iRankRepo.delete(converter.toEntity(item))

    override suspend fun update(item: RankItem) = iRankRepo.update(converter.toEntity(item))

    override fun updatePosition(list: List<RankItem>) {
        val noteIdSet = mutableSetOf<Long>()

        list.forEachIndexed { i, item ->
            /**
             * If [RankItem.position] incorrect (out of order) when update it.
             */
            if (item.position != i) {
                item.position = i

                /**
                 * Add id to [Set] of [NoteEntity.id] where need update [NoteEntity.rankPs].
                 */
                item.noteId.forEach { noteIdSet.add(it) }
            }
        }

        iRankRepo.updatePosition(converter.toEntity(list), noteIdSet.toList())
    }

}