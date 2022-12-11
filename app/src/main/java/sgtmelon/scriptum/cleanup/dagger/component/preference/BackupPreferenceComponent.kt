package sgtmelon.scriptum.cleanup.dagger.component.preference

import androidx.lifecycle.ViewModelStoreOwner
import dagger.BindsInstance
import dagger.Subcomponent
import sgtmelon.scriptum.cleanup.dagger.module.ViewModelModule
import sgtmelon.scriptum.cleanup.dagger.other.ActivityScope
import sgtmelon.scriptum.infrastructure.model.key.PermissionResult
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
        fun set(permissionResult: PermissionResult?): Builder

        fun build(): BackupPreferenceComponent
    }
}