package sgtmelon.scriptum.app.injection.module;

import androidx.recyclerview.selection.ItemKeyProvider;
import androidx.recyclerview.selection.SelectionTracker;
import androidx.recyclerview.selection.StorageStrategy;
import androidx.recyclerview.widget.RecyclerView;
import dagger.Module;
import dagger.Provides;
import sgtmelon.scriptum.app.injection.ScopeApp;
import sgtmelon.scriptum.app.model.repo.RepoNote;
import sgtmelon.scriptum.app.selection.SelNoteKeyProvider;
import sgtmelon.scriptum.app.selection.SelNoteLookup;

@Module
public class ModSel {

    // TODO: 20.09.2018 подключи

    private final RecyclerView recyclerView;

    public ModSel(RecyclerView recyclerView) {
        this.recyclerView = recyclerView;
    }

    @Provides
    @ScopeApp
    public SelNoteKeyProvider provideSelNoteKeyProvider() {
        return new SelNoteKeyProvider(ItemKeyProvider.SCOPE_CACHED);
    }

    @Provides
    @ScopeApp
    public SelectionTracker<RepoNote> provideSelectionTracker(SelNoteKeyProvider keyProvider){
        return new SelectionTracker.Builder<>(
                "selection_tracker_id",
                recyclerView,
                keyProvider,
                new SelNoteLookup(recyclerView),
                StorageStrategy.createParcelableStorage(RepoNote.class)
        ).build();
    }

}
