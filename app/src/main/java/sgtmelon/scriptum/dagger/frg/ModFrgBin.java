package sgtmelon.scriptum.dagger.frg;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.FragmentManager;
import dagger.Module;
import dagger.Provides;
import sgtmelon.scriptum.R;
import sgtmelon.scriptum.databinding.FrgBinBinding;
import sgtmelon.scriptum.element.dialog.DlgOptionBin;
import sgtmelon.scriptum.element.dialog.common.DlgMessage;
import sgtmelon.scriptum.office.annot.def.DefDlg;
import sgtmelon.scriptum.office.st.StOpen;

@Module(includes = ModFrg.class)
class ModFrgBin {

    @Provides
    FrgBinBinding getFrgBinBinding(LayoutInflater inflater, ViewGroup container) {
        return DataBindingUtil.inflate(inflater, R.layout.frg_bin, container, false);
    }

    @Provides
    StOpen getStOpen() {
        return new StOpen();
    }

    @Provides
    DlgMessage getDlgClearBin(FragmentManager fm) {
        DlgMessage dlgClearBin = (DlgMessage) fm.findFragmentByTag(DefDlg.CLEAR_BIN);
        if (dlgClearBin == null) dlgClearBin = new DlgMessage();

        return dlgClearBin;
    }

    @Provides
    DlgOptionBin getDlgOptionBin(FragmentManager fm) {
        DlgOptionBin dlgOptionBin = (DlgOptionBin) fm.findFragmentByTag(DefDlg.OPTIONS);
        if (dlgOptionBin == null) dlgOptionBin = new DlgOptionBin();

        return dlgOptionBin;
    }

}
