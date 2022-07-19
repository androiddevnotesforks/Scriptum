package sgtmelon.scriptum.domain.useCase.preferences

interface GetSummaryUseCase {

    operator fun invoke(): String

    operator fun invoke(value: Int): String
}