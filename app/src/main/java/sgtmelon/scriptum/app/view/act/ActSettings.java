package sgtmelon.scriptum.app.view.act;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import sgtmelon.scriptum.R;
import sgtmelon.scriptum.app.view.frg.FrgSettings;
import sgtmelon.scriptum.office.Help;
import sgtmelon.scriptum.office.blank.BlankAct;

public class ActSettings extends BlankAct {

    private static final String TAG = "ActSettings";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_settings);
        Log.i(TAG, "onCreate");

        setupToolbar();

        FrgSettings frgSettings = new FrgSettings();
        getFragmentManager().beginTransaction().replace(R.id.actSettings_fl_container, frgSettings).commit();
    }

    private void setupToolbar() {
        Log.i(TAG, "setupToolbar");

        Toolbar toolbar = findViewById(R.id.incToolbar_tb);
        toolbar.setTitle(getString(R.string.title_act_settings));

        Drawable arrowBack = Help.Draw.get(this, R.drawable.ic_cancel_off, R.attr.clIcon);
        toolbar.setNavigationIcon(arrowBack);
        toolbar.setNavigationOnClickListener(view -> finish());
    }

}
