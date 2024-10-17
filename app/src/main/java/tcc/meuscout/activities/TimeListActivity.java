package tcc.meuscout.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;

import tcc.meuscout.fragments.TimeListFragment;
import tcc.meuscout.toolbar.BaseToolbarLista;

public class TimeListActivity extends BaseToolbarLista {

    @Override
    protected Fragment getFrament() {
        return TimeListFragment.newInstance();
    }
}