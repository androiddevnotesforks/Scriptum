package sgtmelon.scriptum.cleanup.dagger.component.preference

import androidx.lifecycle.ViewModelStoreOwner
import dagger.BindsInstance
import dagger.Subcomponent
import sgtmelon.scriptum.cleanup.dagger.module.infrastructure.ViewModelModule
import sgtmelon.scriptum.cleanup.dagger.other.ActivityScope
import sgtmelon.scriptum.infrastructure.screen.preference.backup.BackupPreferenceFragment

/**
 * Component for [BackupPreferenceFragment].
 */
@ActivityScope
@Subcomponent(modules = [ViewModelModule::class])
interface BackupPreferenceComponent {

    fun inject(fragment: BackupPreferenceFragment)

    @Subcomponent.Builder
    interface Builder {
        @BindsInstance
        fun set(owner: ViewModelStoreOwner): Builder

        @BindsInstance
        fun set(isFilesAutoFetch: Boolean): Builder

        fun build(): BackupPreferenceComponent
    }
}