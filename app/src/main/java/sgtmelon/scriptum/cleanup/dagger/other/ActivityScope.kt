package sgtmelon.scriptum.cleanup.dagger.other

import javax.inject.Scope

/**
 * Scope for subComponents (activities, fragments, ect.).
 */
@Scope
@Retention(AnnotationRetention.RUNTIME)
annotation class ActivityScope