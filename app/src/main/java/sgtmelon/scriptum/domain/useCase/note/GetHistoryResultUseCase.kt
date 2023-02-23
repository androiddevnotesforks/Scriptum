package sgtmelon.scriptum.domain.useCase.note

import sgtmelon.scriptum.data.noteHistory.model.HistoryAction as Action
import sgtmelon.scriptum.domain.model.result.HistoryResult as Result

/**
 * Convert not specific history [Action] to specific history [Result].
 */
class GetHistoryResultUseCase {

    operator fun invoke(it: Action, isUndo: Boolean): Result {
        return when (it) {
            is Action.Name -> Result.Name(it.value[isUndo], it.cursor[isUndo])
            is Action.Rank -> Result.Rank(it.id[isUndo], it.position[isUndo])
            is Action.Color -> Result.Color(it.value[isUndo])
            is Action.Text.Enter -> Result.Text.Enter(it.value[isUndo], it.cursor[isUndo])
            is Action.Roll.Enter -> Result.Roll.Enter(it.p, it.value[isUndo], it.cursor[isUndo])
            is Action.Roll.List.Add -> {
                if (isUndo) Result.Roll.Remove(it.p) else Result.Roll.Add(it.p, it.item)
            }
            is Action.Roll.List.Remove -> {
                if (isUndo) Result.Roll.Add(it.p, it.item) else Result.Roll.Remove(it.p)
            }
            is Action.Roll.Move -> Result.Roll.Move(it.value[!isUndo], it.value[isUndo])
        }
    }
}