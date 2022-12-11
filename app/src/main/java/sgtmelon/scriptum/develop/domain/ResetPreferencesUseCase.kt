package sgtmelon.scriptum.develop.domain

import sgtmelon.scriptum.develop.data.DevelopRepo

class ResetPreferencesUseCase(private val repository: DevelopRepo) {

    operator fun invoke() = repository.resetPreferences()
}