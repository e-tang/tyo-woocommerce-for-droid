package au.com.tyo.inventory.ui.page;

import android.app.Activity;

import au.com.tyo.app.ui.page.Page;
import au.com.tyo.inventory.R;

/**
 * Created by Eric Tang (eric.tang@tyo.com.au) on 19/12/17.
 */

public class PageInitializer extends au.com.tyo.app.ui.page.PageInitializer {

    @Override
    public void initializePageOnConstruct(Page page) {
        Activity activity = page.getActivity();

        page.setToolbarColor(activity.getResources().getColor(R.color.toolbarColor));
        page.setTitleTextColor(activity.getResources().getColor(R.color.white));
    }

    @Override
    public void initializePageOnActivityStart(Page page) {

    }

}
