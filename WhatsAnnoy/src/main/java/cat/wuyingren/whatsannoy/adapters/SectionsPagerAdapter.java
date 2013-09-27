/** Copyright (c) 2013 Jordi López <@wuyingren> & <@aniol>
 *
 * MIT License:
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING
 * FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 *
 */
package cat.wuyingren.whatsannoy.adapters;


import android.content.Context;
import android.content.Intent;
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
 * @author Jordi López (wuyingren)
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

    public void updateUIFromService(Intent intent) {
        if(intent!=null && intent.getExtras()!=null) {
        randomFragment.updateUI(intent);
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

