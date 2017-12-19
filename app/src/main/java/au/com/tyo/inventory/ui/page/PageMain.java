/*
 * Copyright (c) 2017 TYONLINE TECHNOLOGY PTY. LTD. (TYO Lab)
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package au.com.tyo.inventory.ui.page;

import android.app.Activity;

import au.com.tyo.android.adapter.ListViewItemAdapter;
import au.com.tyo.app.ui.page.PageCommonList;
import au.com.tyo.inventory.BuildConfig;
import au.com.tyo.inventory.Controller;

/**
 * Created by Eric Tang (eric.tang@tyo.com.au) on 27/11/17.
 */

public class PageMain extends PageCommonList<Controller> {

    private ListViewItemAdapter adapter;

    /**
     * @param controller
     * @param activity
     */
    public PageMain(Controller controller, Activity activity) {
        super(controller, activity);
    }

    @Override
    protected void createAdapter() {
        super.createAdapter();

        adapter = getListAdapter();
    }

    @Override
    public void onActivityStart() {
        super.onActivityStart();

        if (!getController().hasUserLoggedIn()) {
            getController().getUi().gotoLoginPage();

            finish();
        }

        startBackgroundTask();
    }

    @Override
    public void run() {
        //
//        try {
//            Thread.sleep(3000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
        // for debugging
        if (BuildConfig.DEBUG)
            getController().getAppData().getApi().createCurlCommands();

        getController().getAppData().loadProducts();

    }

    @Override
    protected void onPageBackgroundTaskFinished() {
        super.onPageBackgroundTaskFinished();

        adapter.setItems(getController().getAppData().getProducts());
    }
}
