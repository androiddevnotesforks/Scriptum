package sgtmelon.scriptum.domain.useCase.rank

import sgtmelon.scriptum.cleanup.data.repository.room.callback.RankRepo

class GetRankDialogNamesUseCase(
    private val withoutCategoryName: String,
    private val repository: RankRepo
) {

    /**
     * Return array with all available category names.
     */
    suspend operator fun invoke(): Array<String> {
        val list = mutableListOf(withoutCategoryName)
        list.addAll(repository.getNameList())
        return list.toTypedArray()
    }
}