package sgtmelon.scriptum.develop.infrastructure.screen.develop

import kotlinx.coroutines.flow.Flow

interface DevelopViewModel {

    val randomNoteId: Flow<Long?>

    fun resetPreferences()
}