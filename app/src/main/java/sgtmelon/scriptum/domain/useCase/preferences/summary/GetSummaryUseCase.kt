package sgtmelon.scriptum.domain.useCase.preferences.summary

interface GetSummaryUseCase {

    operator fun invoke(): String

    operator fun invoke(value: Int): String
}