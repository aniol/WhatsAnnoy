package cat.wuyingren.whatsannoy.adapters;


import android.content.Context;
import android.content.res.Resources;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.ViewGroup;

import org.holoeverywhere.util.SparseArray;

import java.util.Locale;

import cat.wuyingren.whatsannoy.R;
import cat.wuyingren.whatsannoy.fragments.NowFragment;
import cat.wuyingren.whatsannoy.fragments.RandomFragment;
import cat.wuyingren.whatsannoy.fragments.ScheduleFragment;
import cat.wuyingren.whatsannoy.utils.SystemUtils;

/**
 * A {@link android.support.v4.app.FragmentPagerAdapter} that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */
public class SectionsPagerAdapter extends FragmentStatePagerAdapter {

    private Context context;
    private NowFragment nowFragment; //= new NowFragment();
    private ScheduleFragment scheduleFragment;//= new ScheduleFragment();
    private RandomFragment randomFragment;// = new RandomFragment();
    SparseArray<Fragment> registeredFragments = new SparseArray<Fragment>();
    private int sdkVer;

    public SectionsPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        // getItem is called to instantiate the fragment for the given page.
        switch(position) {
            case 0:
                nowFragment = NowFragment.newInstance();
                return nowFragment;
            case 1:
                sdkVer = SystemUtils.getSdkInt();
                scheduleFragment = ScheduleFragment.newInstance(sdkVer);
                return scheduleFragment;
            case 2:
                randomFragment = RandomFragment.newInstance();
                return randomFragment;
            default:
                return null;
        }
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        Fragment fragment = (Fragment) super.instantiateItem(container, position);
        registeredFragments.put(position, fragment);
        return fragment;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {

        super.destroyItem(container, position, object);
        registeredFragments.remove(position);
    }

    public Fragment getRegisteredFragment(int position) {
        return registeredFragments.get(position);
    }

    @Override
    public int getCount() {
        // Show 3 total pages.
        return 3;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        Locale l = Locale.getDefault();
        Resources res = context.getResources();
        switch (position) {
            case 0:
                return res.getString(R.string.title_section1).toUpperCase(l);
            case 1:
                return res.getString(R.string.title_section2).toUpperCase(l);
            case 2:
                return res.getString(R.string.title_section3).toUpperCase(l);
        }
        return null;
    }

    public void setContext(Context context) {
        this.context = context;
    }


}

