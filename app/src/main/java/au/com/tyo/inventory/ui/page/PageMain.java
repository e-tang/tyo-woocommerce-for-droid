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
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Toast;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;

import au.com.tyo.android.adapter.ListViewItemAdapter;
import au.com.tyo.app.ui.page.PageCommonList;
import au.com.tyo.inventory.BuildConfig;
import au.com.tyo.inventory.Constants;
import au.com.tyo.inventory.Controller;
import au.com.tyo.inventory.DataLoader;
import au.com.tyo.inventory.R;
import au.com.tyo.inventory.model.ProductForm;
import au.com.tyo.inventory.ui.widget.ProductListItemFactory;

/**
 * Created by Eric Tang (eric.tang@tyo.com.au) on 27/11/17.
 */

public class PageMain extends PageCommonList<Controller> implements AdapterView.OnItemLongClickListener, AdapterView.OnItemClickListener, Observer, DataLoader {

    private static final String TAG = "PageMain";

    private ListViewItemAdapter adapter;
    private Button stockInButton;

    /**
     * @param controller
     * @param activity
     */
    public PageMain(Controller controller, Activity activity) {
        super(controller, activity);

        // show it after the search functions are implemented
        // setToShowSearchView(true);
        setSubpage(false);

        controller.getAppData().addObserver(this);
    }

    @Override
    protected void createAdapter() {
        super.createAdapter();

        adapter = getListAdapter();
        adapter.setItemFactory(new ProductListItemFactory(getActivity()));
    }

    @Override
    public void setupComponents() {
        super.setupComponents();

        // showSearchBar();
        // showSuggestionView();
        getListView().setOnItemLongClickListener(this);
    }

    @Override
    public void bindData(Intent intent) {
        super.bindData(intent);
    }

    @Override
    public void onActivityStart() {
        super.onActivityStart();

        try {
            ViewConfiguration config = ViewConfiguration.get(getActivity());
            Field menuKeyField = ViewConfiguration.class.getDeclaredField("sHasPermanentMenuKey");
            if (menuKeyField != null) {
                menuKeyField.setAccessible(true);
                menuKeyField.setBoolean(config, false);
            }
        } catch (Exception ex) {
            // Ignore
            Log.e(TAG, "setting menu");
        }

        if (checkUserLoginStatus()) {
            if (getController().getAppData().getProductList() == null)
                startBackgroundTask();
            else
                showProductList();
        }
    }

    private boolean checkUserLoginStatus() {
        if (!getController().hasUserLoggedIn()) {
            getController().getUi().gotoLoginPage();

            finish();
            return false;
        }
        return true;
    }

    @Override
    public void showProgressBar() {
        showProgressBar("loading products");
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

        getController().getAppData().loadProducts(this);

    }

    @Override
    protected void onPageBackgroundTaskFinished() {
        super.onPageBackgroundTaskFinished();

        showProductList();
    }

    private void showProductList() {
        List<ProductForm> productList = getController().getAppData().getProductList();

        adapter.clear();

        adapter.setItems(productList);
        adapter.notifyDataSetChanged();
    }

    @Override
    public AdapterView.OnItemClickListener getOnItemClickListener() {
        return this;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        ProductForm item = (ProductForm) adapter.get(position);
        getController().getUi().gotoProductDetailsPage(item);
    }

    @Override
    public boolean onCreateOptionsMenu(MenuInflater menuInflater, Menu menu) {
//        menuInflater.inflate(R.menu.scan_only, menu);
//        menuInflater.inflate(R.menu.reload_only, menu);
        getActivity().getMenuInflater().inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menuInflater, menu);
    }

    @Override
    protected void createMenu(MenuInflater menuInflater, Menu menu) {
        // super.createMenu(menuInflater, menu);
    }

    @Override
    protected boolean onMenuCreated(Menu menu) {
        super.onMenuCreated(menu);
        return true;
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == R.id.menuItemReload) {
            getController().getAppData().getCacheManager().clear();
            startBackgroundTask();
            return true;
        }
        else if (itemId == R.id.menuItemScan) {
            getController().getUi().gotoBarcodeScannerPage(Constants.OPERATION_SCAN_PRODUCT);
            return true;
        }
        return super.onMenuItemClick(item);
    }

    @Override
    protected void setupPageOverlay(View pageOverlay) {
        LayoutInflater inflater = LayoutInflater.from(getActivity());
        View actionsView = inflater.inflate(R.layout.actions, (ViewGroup) pageOverlay, false);
        ((ViewGroup) pageOverlay).addView(actionsView);

        stockInButton = actionsView.findViewById(R.id.btn_stock_in);
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        final ProductForm listItem = (ProductForm) adapter.get(position);

        showPageOverlay();

        stockInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hidePageOverlay();
                getController().getUi().gotoProductStockInPage(listItem.getProduct());
            }
        });

        return true;
    }

    @Override
    public void onFinish() {
        super.onFinish();
    }

    @Override
    public void finish() {
        super.finish();
    }

    @Override
    public void finishCompletely() {
        super.finishCompletely();
    }

    @Override
    public boolean onDestroy() {
        if (getActivity().isFinishing())
            Log.d(TAG, "System is low on space");
        Log.d(TAG, "Main page just got destroyed");
        return super.onDestroy();
    }

    @Override
    public void update(Observable o, Object arg) {
        adapter.notifyDataSetChanged();
    }


    @Override
    public void onLoadDataFailedGeneral(Map map) {
        String message;
        if (map.containsKey("message"))
            message = (String) map.get("message");
        else
            message = "Something wrong with your key permission, please check with your site's administrator";

        Toast.makeText(getActivity(), message, Toast.LENGTH_LONG);
    }

    @Override
    public void onLoadDataFailedBecauseOfUnauthorization() {
        checkUserLoginStatus();
    }
}
