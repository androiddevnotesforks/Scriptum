package sgtmelon.scriptum.interactor.main

import android.content.Context
import sgtmelon.scriptum.interactor.ParentInteractor
import sgtmelon.scriptum.interactor.callback.main.IRankInteractor
import sgtmelon.scriptum.model.item.NoteItem
import sgtmelon.scriptum.model.item.RankItem
import sgtmelon.scriptum.repository.rank.IRankRepo
import sgtmelon.scriptum.repository.rank.RankRepo
import sgtmelon.scriptum.screen.vm.main.RankViewModel

/**
 * Interactor for [RankViewModel]
 */
class RankInteractor(context: Context) : ParentInteractor(context), IRankInteractor {

    // TODO #TEST write unit test

    private val iRankRepo: IRankRepo = RankRepo(context)

    override fun insert(name: String): RankItem {
        val id = iRankRepo.insert(name)

        return RankItem(id, name = name)
    }

    override fun getList() = iRankRepo.getList()

    override fun delete(item: RankItem) = iRankRepo.delete(item)

    override suspend fun update(item: RankItem) = iRankRepo.update(item)

    override fun updatePosition(list: List<RankItem>) {
        val noteIdSet = mutableSetOf<Long>()

        list.forEachIndexed { i, item ->
            /**
             * If [RankItem.position] incorrect (out of order) when update it.
             */
            if (item.position != i) {
                item.position = i

                /**
                 * Add id to [Set] of [NoteItem.id] where need update [NoteItem.rankPs].
                 */
                item.noteId.forEach { noteIdSet.add(it) }
            }
        }

        iRankRepo.updatePosition(list, noteIdSet.toList())
    }

}