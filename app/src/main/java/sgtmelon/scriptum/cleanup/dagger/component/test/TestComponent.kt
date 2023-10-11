package sgtmelon.scriptum.cleanup.dagger.component.test

import dagger.Subcomponent
import sgtmelon.scriptum.data.repository.preferences.PreferencesRepo
import sgtmelon.scriptum.domain.useCase.note.GetCopyTextUseCase
import sgtmelon.scriptum.infrastructure.database.Database
import sgtmelon.scriptum.infrastructure.preferences.Preferences

/**
 * Component only for test usage.
 */
@Subcomponent
interface TestComponent {

    val preferences: Preferences

    val preferencesRepo: PreferencesRepo

    val database: Database

    val getCopyText: GetCopyTextUseCase

}