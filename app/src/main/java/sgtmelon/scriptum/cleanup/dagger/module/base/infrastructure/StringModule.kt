package sgtmelon.scriptum.cleanup.dagger.module.base.infrastructure

import android.content.Context
import dagger.Module
import dagger.Provides
import javax.inject.Named
import sgtmelon.scriptum.R

@Module
class StringModule {

    @Provides
    @Named("WithoutCategoryName")
    fun provideWithoutCategoryName(context: Context): String {
        return context.getString(R.string.dialog_item_rank_empty)
    }
}