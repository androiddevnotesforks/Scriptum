package sgtmelon.scriptum.cleanup.dagger.component

import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton
import sgtmelon.scriptum.cleanup.dagger.component.main.BinComponent
import sgtmelon.scriptum.cleanup.dagger.component.main.MainComponent
import sgtmelon.scriptum.cleanup.dagger.component.main.NotesComponent
import sgtmelon.scriptum.cleanup.dagger.component.main.RankComponent
import sgtmelon.scriptum.cleanup.dagger.component.note.NoteComponent
import sgtmelon.scriptum.cleanup.dagger.component.note.RollNoteComponent
import sgtmelon.scriptum.cleanup.dagger.component.note.TextNoteComponent
import sgtmelon.scriptum.cleanup.dagger.component.preference.AlarmPreferenceComponent
import sgtmelon.scriptum.cleanup.dagger.component.preference.BackupPreferenceComponent
import sgtmelon.scriptum.cleanup.dagger.component.preference.HelpDescriptionComponent
import sgtmelon.scriptum.cleanup.dagger.component.preference.MainPreferenceComponent
import sgtmelon.scriptum.cleanup.dagger.component.preference.NotePreferenceComponent
import sgtmelon.scriptum.cleanup.dagger.component.preference.PreferenceComponent
import sgtmelon.scriptum.cleanup.dagger.component.preference.develop.DevelopPreferenceComponent
import sgtmelon.scriptum.cleanup.dagger.component.preference.develop.PrintDevelopComponent
import sgtmelon.scriptum.cleanup.dagger.component.preference.develop.ServiceDevelopComponent
import sgtmelon.scriptum.cleanup.dagger.component.service.SystemComponent
import sgtmelon.scriptum.cleanup.dagger.module.RepoModule
import sgtmelon.scriptum.cleanup.dagger.module.data.BackupModule
import sgtmelon.scriptum.cleanup.dagger.module.data.DataSourceModule
import sgtmelon.scriptum.cleanup.dagger.module.data.RepositoryModule
import sgtmelon.scriptum.cleanup.dagger.module.domain.AlarmUseCaseModule
import sgtmelon.scriptum.cleanup.dagger.module.domain.BackupUseCaseModule
import sgtmelon.scriptum.cleanup.dagger.module.domain.GetSummaryUseCaseModule
import sgtmelon.scriptum.cleanup.dagger.module.domain.MainUseCaseModule
import sgtmelon.scriptum.cleanup.dagger.module.domain.NoteUseCaseModule
import sgtmelon.scriptum.cleanup.dagger.module.domain.RankUseCaseModule
import sgtmelon.scriptum.cleanup.dagger.module.domain.UseCaseModule
import sgtmelon.scriptum.cleanup.dagger.module.infrastructure.ContextModule
import sgtmelon.scriptum.cleanup.dagger.module.infrastructure.ConverterModule
import sgtmelon.scriptum.cleanup.dagger.module.infrastructure.PreferencesModule
import sgtmelon.scriptum.cleanup.dagger.module.infrastructure.RoomModule
import sgtmelon.scriptum.cleanup.dagger.module.infrastructure.StringModule
import sgtmelon.scriptum.cleanup.presentation.screen.ui.ScriptumApplication

/**
 * Parent component for application.
 */
@Singleton
@Component(modules = [
    ContextModule::class,
    StringModule::class,
    PreferencesModule::class,
    RoomModule::class,
    ConverterModule::class,

    MainUseCaseModule::class,
    NoteUseCaseModule::class,
    RankUseCaseModule::class,
    AlarmUseCaseModule::class,
    BackupUseCaseModule::class,
    GetSummaryUseCaseModule::class,
    UseCaseModule::class,

    DataSourceModule::class,
    RepositoryModule::class,
    BackupModule::class,

    RepoModule::class
])
interface ScriptumComponent {

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

    //region Preferences

    fun getMainPreferenceBuilder(): MainPreferenceComponent.Builder

    fun getPreferenceBuilder(): PreferenceComponent.Builder

    fun getBackupPrefBuilder(): BackupPreferenceComponent.Builder

    fun getNotePrefBuilder(): NotePreferenceComponent.Builder

    fun getAlarmPrefBuilder(): AlarmPreferenceComponent.Builder

    fun getHelpDescriptionBuilder(): HelpDescriptionComponent.Builder

    fun getDevelopBuilder(): DevelopPreferenceComponent.Builder

    fun getPrintBuilder(): PrintDevelopComponent.Builder

    fun getServiceBuilder(): ServiceDevelopComponent.Builder

    //endregion

    fun getSystemBuilder(): SystemComponent.Builder


    @Component.Builder
    interface Builder {
        @BindsInstance
        fun set(application: ScriptumApplication): Builder

        fun build(): ScriptumComponent
    }
}