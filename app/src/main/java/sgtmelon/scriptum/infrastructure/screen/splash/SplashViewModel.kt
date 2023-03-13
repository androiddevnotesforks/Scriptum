package sgtmelon.scriptum.infrastructure.screen.splash

import sgtmelon.scriptum.cleanup.domain.model.item.NoteItem
import sgtmelon.scriptum.infrastructure.model.key.preference.NoteType

interface SplashViewModel {

    fun getNewNote(type: NoteType): NoteItem

}