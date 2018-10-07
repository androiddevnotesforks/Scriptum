package sgtmelon.scriptum.element.common;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.navigation.NavigationView;

import androidx.annotation.IdRes;
import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import sgtmelon.scriptum.office.annot.def.DialogDef;

public final class SheetDialog extends BottomSheetDialogFragment implements
        NavigationView.OnNavigationItemSelectedListener {

    // TODO: 24.09.2018 ошибка при тсутствии слоя

    @LayoutRes
    private int layout;
    @IdRes
    private int navigation;

    private NavigationView.OnNavigationItemSelectedListener navigationItemSelectedListener;
    private DialogInterface.OnDismissListener dismissListener;

    public void setArguments(@LayoutRes int layout, @IdRes int navigation) {
        Bundle arg = new Bundle();

        arg.putInt(DialogDef.INIT, layout);
        arg.putInt(DialogDef.VALUE, navigation);

        setArguments(arg);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Bundle arg = getArguments();

        if (savedInstanceState != null) {
            layout = savedInstanceState.getInt(DialogDef.INIT);
            navigation = savedInstanceState.getInt(DialogDef.VALUE);
        } else if (arg != null) {
            layout = arg.getInt(DialogDef.INIT);
            navigation = arg.getInt(DialogDef.VALUE);
        }

        View view = inflater.inflate(layout, container, false);

        NavigationView navigationView = view.findViewById(navigation);
        if (navigationView != null) navigationView.setNavigationItemSelectedListener(this);

        return view;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putInt(DialogDef.INIT, layout);
        outState.putInt(DialogDef.VALUE, navigation);
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);

        dismissListener.onDismiss(dialog);
    }

    public void setNavigationItemSelectedListener(
            NavigationView.OnNavigationItemSelectedListener navigationItemSelectedListener) {
        this.navigationItemSelectedListener = navigationItemSelectedListener;
    }

    public void setDismissListener(DialogInterface.OnDismissListener dismissListener) {
        this.dismissListener = dismissListener;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        navigationItemSelectedListener.onNavigationItemSelected(menuItem);
        return false;
    }

}
