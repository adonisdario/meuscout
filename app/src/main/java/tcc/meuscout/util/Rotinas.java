package tcc.meuscout.util;

import android.view.View;
import android.view.ViewGroup;

public class Rotinas {

    public static void desabilitarTela(boolean enable, ViewGroup vg) {
        for(int i = 0; i < vg.getChildCount(); ++i) {
            View child = vg.getChildAt(i);
            child.setEnabled(enable);
            if (child instanceof ViewGroup) {
                desabilitarTela(enable, (ViewGroup)child);
            }
        }

    }
}
