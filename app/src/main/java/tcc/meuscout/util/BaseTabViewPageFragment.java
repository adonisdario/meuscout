package tcc.meuscout.util;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;

public abstract class BaseTabViewPageFragment extends BaseFragment {
    private int mLayoutFragment;
    private int mNumItens;
    protected TabLayout mTabLayout;
    protected ViewPager mViewPager;

    public BaseTabViewPageFragment(int layoutFragment, int numItens) {
        this.mLayoutFragment = layoutFragment;
        this.mNumItens = numItens;
    }

    protected int getFragmentLayout() throws Exception {
        return this.mLayoutFragment;
    }

    protected abstract Fragment getItemAba(int var1);

    protected abstract CharSequence getPageTitleAbas(int var1);

    protected abstract int getMyPagerView();

    protected abstract int getMyTabLayout();

    protected String makeFragmentName(int position) {
        return "android:switcher:" + this.getMyPagerView() + ":" + position;
    }

    protected void inicializar() throws Exception {
        BaseTabViewPageFragment.BasePagerAdapter basePagerAdapter = new BaseTabViewPageFragment.BasePagerAdapter(this.getFragmentManager());
        this.mViewPager = (ViewPager) this.getView().findViewById(this.getMyPagerView());
        this.mTabLayout = (TabLayout) this.getView().findViewById(this.getMyTabLayout());
        this.mTabLayout.setupWithViewPager(this.mViewPager);
        if (this.mViewPager != null) {
            this.mViewPager.setAdapter(basePagerAdapter);
        }

    }

    private class BasePagerAdapter extends FragmentPagerAdapter {
        private FragmentManager mFragmentManager;

        protected FragmentManager getFragmentManager() {
            return this.mFragmentManager;
        }

        public BasePagerAdapter(@NonNull FragmentManager fm) {
            super(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
            this.mFragmentManager = fm;
        }

        @NonNull
        public Fragment getItem(int position) {
            return BaseTabViewPageFragment.this.getItemAba(position);
        }

        public int getCount() {
            return BaseTabViewPageFragment.this.mNumItens;
        }

        @Nullable
        public CharSequence getPageTitle(int position) {
            return BaseTabViewPageFragment.this.getPageTitleAbas(position);
        }
    }
}