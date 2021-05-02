package sgtmelon.scriptum.dagger.component

import dagger.BindsInstance
import dagger.Component
import sgtmelon.scriptum.dagger.component.main.BinComponent
import sgtmelon.scriptum.dagger.component.main.MainComponent
import sgtmelon.scriptum.dagger.component.main.NotesComponent
import sgtmelon.scriptum.dagger.component.main.RankComponent
import sgtmelon.scriptum.dagger.component.note.NoteComponent
import sgtmelon.scriptum.dagger.component.note.RollNoteComponent
import sgtmelon.scriptum.dagger.component.note.TextNoteComponent
import sgtmelon.scriptum.dagger.component.preference.BackupPrefComponent
import sgtmelon.scriptum.dagger.component.preference.DevelopComponent
import sgtmelon.scriptum.dagger.component.preference.PreferenceComponent
import sgtmelon.scriptum.dagger.component.preference.PrintComponent
import sgtmelon.scriptum.dagger.component.service.EternalComponent
import sgtmelon.scriptum.dagger.module.base.ContextModule
import sgtmelon.scriptum.dagger.module.base.ConverterModule
import sgtmelon.scriptum.dagger.module.base.ProviderModule
import sgtmelon.scriptum.dagger.module.base.RepoModule
import sgtmelon.scriptum.presentation.screen.ui.ScriptumApplication
import javax.inject.Singleton

/**
 * Parent component for application.
 */
@Singleton
@Component(modules = [
    ContextModule::class,
    RepoModule::class,
    ProviderModule::class,
    ConverterModule::class
])
interface ScriptumComponent {

    fun getAppBuilder(): AppComponent.Builder

    fun getSplashBuilder(): SplashComponent.Builder

    fun getIntroBuilder(): IntroComponent.Builder


    fun getMainBuilder(): MainComponent.Builder

    fun getRankBuilder(): RankComponent.Builder

    fun getNotesBuilder(): NotesComponent.Builder

    fun getBinBuilder(): BinComponent.Builder


    fun getNoteBuilder(): NoteComponent.Builder

    fun getTextNoteBuilder(): TextNoteComponent.Builder

    fun getRollNoteBuilder(): RollNoteComponent.Builder


    fun getNotificationBuilder(): NotificationComponent.Builder

    fun getAlarmBuilder(): AlarmComponent.Builder


    fun getPreferenceBuilder(): PreferenceComponent.Builder

    fun getBackupPrefBuilder(): BackupPrefComponent.Builder

    fun getDevelopBuilder(): DevelopComponent.Builder

    fun getPrintBuilder(): PrintComponent.Builder


    fun getEternalBuilder(): EternalComponent.Builder


    @Component.Builder
    interface Builder {
        @BindsInstance
        fun set(application: ScriptumApplication): Builder

        fun build(): ScriptumComponent
    }
}