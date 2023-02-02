package sgtmelon.scriptum.domain.useCase.note

import sgtmelon.scriptum.data.noteHistory.model.HistoryAction as Action
import sgtmelon.scriptum.domain.model.result.HistoryResult as Result

class GetHistoryResultUseCase {

    operator fun invoke(it: Action, isUndo: Boolean): Result {
        return when (it) {
            is Action.Name -> Result.Name(it.value[isUndo], it.cursor[isUndo])
            is Action.Rank -> Result.Rank(it.id[isUndo], it.position[isUndo])
            is Action.Color -> Result.Color(it.value[isUndo])
            is Action.Text.Enter -> Result.Text.Enter(it.value[isUndo], it.cursor[isUndo])
            is Action.Roll.Enter -> TODO()
            is Action.Roll.List.Add -> TODO()
            is Action.Roll.List.Remove -> TODO()
            is Action.Roll.Move -> TODO()
        }
    }
}