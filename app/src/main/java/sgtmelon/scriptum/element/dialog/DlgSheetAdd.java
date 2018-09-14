package sgtmelon.scriptum.element.dialog;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.navigation.NavigationView;

import androidx.annotation.NonNull;
import sgtmelon.scriptum.R;

public class DlgSheetAdd extends BottomSheetDialogFragment implements NavigationView.OnNavigationItemSelectedListener {

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.sheet_add, container, false);

        NavigationView navigationView = view.findViewById(R.id.sheetAdd_nv_menu);
        navigationView.setNavigationItemSelectedListener(this);

        return view;
    }

    private NavigationView.OnNavigationItemSelectedListener navigationItemSelectedListener;

    public void setNavigationItemSelectedListener(NavigationView.OnNavigationItemSelectedListener navigationItemSelectedListener) {
        this.navigationItemSelectedListener = navigationItemSelectedListener;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        navigationItemSelectedListener.onNavigationItemSelected(menuItem);
        return false;
    }

}
