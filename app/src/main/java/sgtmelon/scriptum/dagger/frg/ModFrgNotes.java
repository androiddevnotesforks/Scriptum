package sgtmelon.scriptum.dagger.frg;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.FragmentManager;
import dagger.Module;
import dagger.Provides;
import sgtmelon.scriptum.R;
import sgtmelon.scriptum.databinding.FrgNotesBinding;
import sgtmelon.scriptum.element.dialog.DlgOptionNote;
import sgtmelon.scriptum.office.annot.def.DefDlg;

@Module(includes = ModFrg.class)
class ModFrgNotes {

    @Provides
    FrgNotesBinding getFrgNotesBinding(LayoutInflater inflater, ViewGroup container) {
        return DataBindingUtil.inflate(inflater, R.layout.frg_notes, container, false);
    }

    @Provides
    DlgOptionNote getDlgOptionNote(FragmentManager fm) {
        DlgOptionNote dlgOptionNote = (DlgOptionNote) fm.findFragmentByTag(DefDlg.OPTIONS);
        if (dlgOptionNote == null) dlgOptionNote = new DlgOptionNote();

        return dlgOptionNote;
    }

}
