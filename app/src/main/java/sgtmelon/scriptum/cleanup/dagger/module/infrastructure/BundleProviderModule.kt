package sgtmelon.scriptum.cleanup.dagger.module.infrastructure

import dagger.Module
import dagger.Provides
import sgtmelon.scriptum.data.repository.preferences.PreferencesRepo
import sgtmelon.scriptum.infrastructure.converter.key.ColorConverter
import sgtmelon.scriptum.infrastructure.converter.key.NoteStateConverter
import sgtmelon.scriptum.infrastructure.converter.key.NoteTypeConverter
import sgtmelon.scriptum.infrastructure.screen.note.NoteBundleProvider

@Module
class BundleProviderModule {

    @Provides
    fun provideNoteBundleProvider(
        repository: PreferencesRepo,
        typeConverter: NoteTypeConverter,
        colorConverter: ColorConverter,
        stateConverter: NoteStateConverter
    ): NoteBundleProvider {
        val defaultColor = repository.defaultColor
        return NoteBundleProvider(defaultColor, typeConverter, colorConverter, stateConverter)
    }
}