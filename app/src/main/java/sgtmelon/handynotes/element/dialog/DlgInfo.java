package sgtmelon.handynotes.element.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import sgtmelon.handynotes.R;
import sgtmelon.handynotes.office.annot.Dlg;

public class DlgInfo extends DialogFragment implements View.OnClickListener {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Context context = getContext();

        if (savedInstanceState != null) {
            click = savedInstanceState.getInt(Dlg.VALUE);
        }

        View view = LayoutInflater.from(context).inflate(R.layout.view_about, null);

        ImageView logo = view.findViewById(R.id.viewAbout_iv_logo);
        logo.setOnClickListener(this);

        return new AlertDialog.Builder(context)
                .setView(view)
                .setCancelable(true)
                .create();
    }

    private View.OnClickListener logoClick;

    public void setLogoClick(View.OnClickListener logoClick) {
        this.logoClick = logoClick;
    }

    private final static int show = 9;
    private int click = 0;

    @Override
    public void onClick(View view) {
        if (++click == show) {
            click = 0;
            logoClick.onClick(view);
            getDialog().cancel();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putInt(Dlg.VALUE, click);
    }
}
