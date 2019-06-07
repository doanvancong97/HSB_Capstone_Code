package capstone.sonnld.hairsalonbooking.fragment;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

public class PagerAdapter extends FragmentStatePagerAdapter {


    int mNumOfTab;

    public PagerAdapter(FragmentManager fm, int mNumOfTab) {
        super(fm);
        this.mNumOfTab = mNumOfTab;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                TabViewSalon tabViewSalon = new TabViewSalon();
                return tabViewSalon;
            case 1:
                TabBooking tabBooking = new TabBooking();
                return tabBooking;
            default:
                return null;
        }


    }

    @Override
    public int getCount() {
        return mNumOfTab;
    }
}
