package sgtmelon.scriptum.app.selection;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.selection.ItemKeyProvider;
import sgtmelon.scriptum.app.model.repo.RepoNote;

public class SelNoteKeyProvider extends ItemKeyProvider<RepoNote> {

    private List<RepoNote> list = new ArrayList<>();

    /**
     * Creates a new provider with the given scope.
     * SCOPE_MAPPED - ко всем данным. Позволяет реализовать Shift+click выбор данных
     * SCOPE_CACHED - к данным, которые были недавно или сейчас на экране. Экономит память
     *
     * @param scope Scope can't be changed at runtime.
     */
    public SelNoteKeyProvider(int scope) {
        super(scope);
    }

    public void update(List<RepoNote> list) {
        this.list = list;
    }

    @Nullable
    @Override
    public RepoNote getKey(int position) {
        return list.get(position);
    }

    @Override
    public int getPosition(@NonNull RepoNote key) {
        long id = key.getItemNote().getId();

        for (int i = 0; i < list.size(); i++) {
            RepoNote repoNote = list.get(i);
            if (repoNote.getItemNote().getId() == id) {
                return i;
            }
        }
        return -1;
    }

}
