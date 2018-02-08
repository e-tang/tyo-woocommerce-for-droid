/*
 * Copyright (c) 2018 TYONLINE TECHNOLOGY PTY. LTD. (TYO Lab)
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
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import au.com.tyo.inventory.Constants;
import au.com.tyo.inventory.Controller;
import au.com.tyo.inventory.R;
import au.com.tyo.inventory.model.Product;
import au.com.tyo.io.IO;
import au.com.tyo.json.android.pages.PageForm;
import au.com.tyo.utils.SpreadSheet;
import au.com.tyo.woocommerce.WooCommerceJson;

/**
 * Created by Eric Tang (eric.tang@tyo.com.au) on 4/2/18.
 */

public class PageImport extends PageForm<Controller> {

    private static final String TAG = "PageImport";

    private Object data;
    private Uri uri;
    private String content;
    private int importType;

    /**
     * @param controller
     * @param activity
     */
    public PageImport(Controller controller, Activity activity) {
        super(controller, activity);

        setContentViewResId(R.layout.page_import);
        setFormContainerId(R.id.form_container);
    }

    protected void createMenu(MenuInflater menuInflater, Menu menu) {
        menuInflater.inflate(R.menu.edit_orders, menu);
    }

    @Override
    public void bindData(Intent intent) {
        super.bindData(intent);

        importType = intent.getIntExtra(Constants.DATA_IMPORT_TYPE, Constants.IMPORT_TYPE_PRODUCT);

        if (null == data)
            data = uri = intent.getData();
    }

    private void uriToData() {
        if (null != uri) {
            InputStream inputStream = null;
            try {
                inputStream = getActivity().getContentResolver().openInputStream(uri);

                content = new String(IO.inputStreamToBytes(inputStream));

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (null != inputStream)
                    try {
                        inputStream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
            }
        }
    }

    @Override
    public void bindData() {
        super.bindData();

        if (getController().getParcel() != null && getController().getParcel() instanceof List) {
            data = getController().getParcel();
        }
    }

    @Override
    public void onActivityStart() {
        super.onActivityStart();

        if (data instanceof List) {
            List list;
            list = (List) data;

            if (list.size() > 0) {
                if (list.size() == 1)
                    uri = Uri.fromFile(new File((String) list.get(0)));
                else
                    getController().getUi().pickFromList(list, getActivity().getResources().getString(R.string.pick_file_to_import));
            }
        }
    }

    @Override
    public boolean onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK && null != data) {
            Object obj = getActivityResult(data);
            try {
                uri = Uri.fromFile(new File((String) obj));
            }
            catch (Exception ex) {
                Log.d(TAG, "Getting result from pick error", ex);
            }
            return true;
        }
        return super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onFormCheckFailed() {

    }

    @Override
    protected void saveFormData(Object form) {

    }

    @Override
    public void onFormClick(Context context, String key, String text) {

    }

    @Override
    public void setupComponents() {
        super.setupComponents();

        Button button = (Button) findViewById(R.id.btn_import);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (content != null)
                    uriToData();

                if (data == null) {
                    Toast.makeText(getActivity(), "Invalid content", Toast.LENGTH_LONG);
                    return;
                }

                startBackgroundTask(new Runnable() {
                    @Override
                    public void run() {
                        if (importType == Constants.IMPORT_TYPE_CATEGORY)
                            importCategories(content);
                        else
                            importProducts(content);
                    }
                });
                finish();
            }
        });
    }

    private void importProducts(String content) {
        SpreadSheet spreadSheet = new SpreadSheet(SpreadSheet.SPREAD_SHEET_TYPE_TSV);

        spreadSheet.setSimpleTable(true);
        spreadSheet.setIgnoreEmptyCell(true);

        spreadSheet.createTable(content);

        List<List> table = spreadSheet.getTable();

        // temporary solution
        // orders

        for (List row : table)
            for (Object obj : row) {
                String colStr = (String) obj;

                Product product = new Product();
                getController().getAppData().importProduct(product);
            }
    }

    private void importCategories(String data) {

        List<String> cats = new ArrayList<String>();
        cats = WooCommerceJson.getGson().fromJson(data, cats.getClass());

        getController().getAppData().importCategories(cats);

    }

    @Override
    public void onFieldClick(View v) {

    }

    @Override
    public void onFieldValueClear(String key) {

    }

}
