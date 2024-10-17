package tcc.meuscout.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;

import tcc.meuscout.fragments.TimeFragment;
import tcc.meuscout.toolbar.BaseToolbarCadastro;

public class TimeActivity extends BaseToolbarCadastro {

    @Override
    protected Fragment getFrament() {
        return TimeFragment.newInstance();
    }
}