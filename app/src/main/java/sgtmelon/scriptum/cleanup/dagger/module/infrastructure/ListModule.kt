package sgtmelon.scriptum.cleanup.dagger.module.infrastructure

import dagger.Module
import dagger.Provides
import javax.inject.Named
import sgtmelon.scriptum.cleanup.domain.model.item.NoteItem
import sgtmelon.scriptum.cleanup.domain.model.item.NotificationItem
import sgtmelon.scriptum.cleanup.domain.model.item.RankItem
import sgtmelon.scriptum.cleanup.domain.model.item.RollItem
import sgtmelon.scriptum.develop.infrastructure.model.PrintItem
import sgtmelon.scriptum.infrastructure.model.data.IdlingTag
import sgtmelon.scriptum.infrastructure.screen.parent.list.ListStorageImpl

@Module
class ListModule {

    @Provides
    @Named("Rank")
    fun provideRankListStorage(): ListStorageImpl<RankItem> {
        val (change, next) = IdlingTag.List.RANK
        return ListStorageImpl(change, next)
    }

    @Provides
    @Named("Note")
    fun provideNoteListStorage(): ListStorageImpl<NoteItem> {
        val (change, next) = IdlingTag.List.NOTE
        return ListStorageImpl(change, next)
    }

    @Provides
    @Named("Roll")
    fun provideRollListStorage(): ListStorageImpl<RollItem> {
        val (change, next) = IdlingTag.List.ROLL
        return ListStorageImpl(change, next)
    }

    @Provides
    @Named("Notification")
    fun provideNotificationListStorage(): ListStorageImpl<NotificationItem> {
        val (change, next) = IdlingTag.List.NOTIFICATION
        return ListStorageImpl(change, next)
    }

    @Provides
    @Named("Print")
    fun providePrintListStorage(): ListStorageImpl<PrintItem> {
        val (change, next) = IdlingTag.List.PRINT
        return ListStorageImpl(change, next)
    }

}