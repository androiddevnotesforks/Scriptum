package sgtmelon.scriptum.domain.useCase.preferences

interface GetSignalSummaryUseCase {

    operator fun invoke(): String

    operator fun invoke(valueArray: BooleanArray): String
}