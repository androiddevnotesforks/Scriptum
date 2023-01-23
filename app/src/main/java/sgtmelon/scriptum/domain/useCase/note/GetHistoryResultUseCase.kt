package sgtmelon.scriptum.domain.useCase.note

import sgtmelon.scriptum.data.noteHistory.model.HistoryAction
import sgtmelon.scriptum.domain.model.result.HistoryResult

class GetHistoryResultUseCase {

    operator fun invoke(it: HistoryAction, isUndo: Boolean): HistoryResult {
        return when (it) {
            is HistoryAction.Name -> HistoryResult.Name(it.value[isUndo], it.cursor[isUndo])
            is HistoryAction.Rank -> HistoryResult.Rank(it.id[isUndo], it.position[isUndo])
            is HistoryAction.Color -> HistoryResult.Color(it.value[isUndo])
            is HistoryAction.Text.Enter -> TODO()
            is HistoryAction.Roll.Enter -> TODO()
            is HistoryAction.Roll.List.Add -> TODO()
            is HistoryAction.Roll.List.Remove -> TODO()
            is HistoryAction.Roll.Move -> TODO()
        }
    }
}