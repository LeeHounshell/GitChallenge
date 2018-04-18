package com.harlie.leehounshell.gitchallenge.util;
 
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.harlie.leehounshell.gitchallenge.view.UserOneTabFragment;
import com.harlie.leehounshell.gitchallenge.view.UserTwoTabFragment;

import java.util.ArrayList;
import java.util.List;

public class PagerAdapter extends FragmentStatePagerAdapter {
    private final static String TAG = "LEE: <" + PagerAdapter.class.getSimpleName() + ">";

    private FragmentManager fragmentManager;
    private final List<Fragment> mFragmentList = new ArrayList<>();
    private final List<String> mFragmentTitleList = new ArrayList<>();
    private int mNumOfTabs;
 
    public PagerAdapter(FragmentManager fragmentManager, int NumOfTabs) {
        super(fragmentManager);
        this.fragmentManager = fragmentManager;
        this.mNumOfTabs = NumOfTabs;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                UserOneTabFragment tab1 = new UserOneTabFragment();
                tab1.setUserNumber(1);
                return tab1;
            case 1:
                UserTwoTabFragment tab2 = new UserTwoTabFragment();
                tab2.setUserNumber(2);
                return tab2;
            default:
                return null;
        }
    }
 
    @Override
    public int getCount() {
        return mNumOfTabs;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mFragmentTitleList.get(position);
    }

    public void addFragment(Fragment fragment, String title) {
        mFragmentList.add(fragment);
        mFragmentTitleList.add(title);
    }
}
