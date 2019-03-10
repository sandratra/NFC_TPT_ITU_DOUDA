package com.sharingame.utility;

import com.sharingame.sharg.R;

public enum ECustomPager {
    HOME(R.string.title_home, R.layout.fragment_home_home),
    NEWS(R.string.title_news, R.layout.fragment_home_news),
    PROFILE(R.string.title_profile, R.layout.fragment_home_user);

    private int mTitleResId;
    private int mLayoutResId;

    ECustomPager(int titleResId, int layoutResId) {
        mTitleResId = titleResId;
        mLayoutResId = layoutResId;
    }

    public int getTitleResId() {
        return mTitleResId;
    }

    public int getLayoutResId() {
        return mLayoutResId;
    }
}
