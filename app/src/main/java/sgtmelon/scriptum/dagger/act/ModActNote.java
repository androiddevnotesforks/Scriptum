package sgtmelon.scriptum.dagger.act;

import android.content.Context;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProviders;
import dagger.Module;
import dagger.Provides;
import sgtmelon.scriptum.app.control.SaveNote;
import sgtmelon.scriptum.app.view.frg.note.FrgRoll;
import sgtmelon.scriptum.app.view.frg.note.FrgText;
import sgtmelon.scriptum.app.viewModel.VmActNote;
import sgtmelon.scriptum.office.annot.def.DefFrg;

@Module(includes = ModAct.class)
class ModActNote {

    @Provides
    VmActNote provideVmActNote(AppCompatActivity activity) {
        return ViewModelProviders.of(activity).get(VmActNote.class);
    }

    @Provides
    SaveNote provideSaveNote(Context context) {
        return new SaveNote(context);
    }

}
