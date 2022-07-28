package sgtmelon.scriptum.domain.useCase.preferences.summary

interface GetSignalSummaryUseCase {

    operator fun invoke(): String

    operator fun invoke(valueArray: BooleanArray): String
}