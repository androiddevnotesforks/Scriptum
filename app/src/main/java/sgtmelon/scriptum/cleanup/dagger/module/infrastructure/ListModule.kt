package sgtmelon.scriptum.cleanup.dagger.module.infrastructure

import dagger.Module
import dagger.Provides
import javax.inject.Named
import sgtmelon.scriptum.cleanup.domain.model.item.NoteItem
import sgtmelon.scriptum.cleanup.domain.model.item.NotificationItem
import sgtmelon.scriptum.cleanup.domain.model.item.RankItem
import sgtmelon.scriptum.cleanup.domain.model.item.RollItem
import sgtmelon.scriptum.develop.infrastructure.model.PrintItem
import sgtmelon.scriptum.infrastructure.screen.parent.list.ListStorageImpl

@Module
class ListModule {

    @Provides
    @Named("Rank")
    fun provideRankListStorage(): ListStorageImpl<RankItem> = ListStorageImpl()

    @Provides
    @Named("Note")
    fun provideNoteListStorage(): ListStorageImpl<NoteItem> = ListStorageImpl()

    @Provides
    @Named("Roll")
    fun provideRollListStorage(): ListStorageImpl<RollItem> = ListStorageImpl()

    @Provides
    @Named("Notification")
    fun provideNotificationListStorage(): ListStorageImpl<NotificationItem> = ListStorageImpl()

    @Provides
    @Named("Print")
    fun providePrintListStorage(): ListStorageImpl<PrintItem> = ListStorageImpl()

}