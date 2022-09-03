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
import sgtmelon.scriptum.cleanup.dagger.component.preference.NotePreferenceComponent
import sgtmelon.scriptum.cleanup.dagger.component.preference.PreferenceComponent
import sgtmelon.scriptum.cleanup.dagger.component.preference.develop.DevelopPreferenceComponent
import sgtmelon.scriptum.cleanup.dagger.component.preference.develop.PrintDevelopComponent
import sgtmelon.scriptum.cleanup.dagger.component.preference.develop.ServiceDevelopComponent
import sgtmelon.scriptum.cleanup.dagger.component.service.SystemComponent
import sgtmelon.scriptum.cleanup.dagger.module.base.ParserModule
import sgtmelon.scriptum.cleanup.dagger.module.base.RepoModule
import sgtmelon.scriptum.cleanup.dagger.module.base.data.DataSourceModule
import sgtmelon.scriptum.cleanup.dagger.module.base.data.RepositoryModule
import sgtmelon.scriptum.cleanup.dagger.module.base.domain.BackupUseCaseModule
import sgtmelon.scriptum.cleanup.dagger.module.base.domain.GetSummaryUseCaseModule
import sgtmelon.scriptum.cleanup.dagger.module.base.domain.NoteUseCaseModule
import sgtmelon.scriptum.cleanup.dagger.module.base.domain.UseCaseModule
import sgtmelon.scriptum.cleanup.dagger.module.base.infrastructure.ContextModule
import sgtmelon.scriptum.cleanup.dagger.module.base.infrastructure.ConverterModule
import sgtmelon.scriptum.cleanup.dagger.module.base.infrastructure.PreferencesModule
import sgtmelon.scriptum.cleanup.dagger.module.base.infrastructure.RoomModule
import sgtmelon.scriptum.cleanup.presentation.screen.ui.ScriptumApplication

/**
 * Parent component for application.
 */
@Singleton
@Component(modules = [
    ContextModule::class,
    PreferencesModule::class,
    RoomModule::class,
    ConverterModule::class,

    DataSourceModule::class,
    RepositoryModule::class,

    NoteUseCaseModule::class,
    BackupUseCaseModule::class,
    GetSummaryUseCaseModule::class,
    UseCaseModule::class,

    RepoModule::class,
    ParserModule::class
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

    //region Preferences

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