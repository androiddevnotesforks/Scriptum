package sgtmelon.scriptum.infrastructure.develop.screen.develop

import kotlinx.coroutines.flow.Flow

interface DevelopViewModel {

    val randomNoteId: Flow<Long>

    fun resetPreferences()
}