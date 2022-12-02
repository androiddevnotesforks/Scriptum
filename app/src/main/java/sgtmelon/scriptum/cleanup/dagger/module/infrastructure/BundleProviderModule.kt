package sgtmelon.scriptum.cleanup.dagger.module.infrastructure

import dagger.Module
import dagger.Provides
import sgtmelon.scriptum.infrastructure.converter.key.ColorConverter
import sgtmelon.scriptum.infrastructure.converter.key.NoteTypeConverter
import sgtmelon.scriptum.infrastructure.screen.note.NoteBundleProvider

@Module
class BundleProviderModule {

    @Provides
    fun provideNoteBundleProvider(
        typeConverter: NoteTypeConverter,
        colorConverter: ColorConverter
    ): NoteBundleProvider {
        return NoteBundleProvider(typeConverter, colorConverter)
    }
}