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

package au.com.tyo.inventory.model;

import com.google.api.client.json.GenericJson;
import com.google.gson.internal.LinkedTreeMap;

import java.util.List;
import java.util.Map;

import au.com.tyo.inventory.AppData;
import au.com.tyo.json.FormItem;
import au.com.tyo.json.JsonForm;
import au.com.tyo.json.android.utils.FormHelper;

/**
 * Created by Eric Tang (eric.tang@tyo.com.au) on 13/12/17.
 */

public class Product extends GenericJson implements FormItem {

    private String imageUrl;

    public String getImageUrl() {
        if (imageUrl == null) {
            List object = (List) get("images");
            if (null != object && object.size() > 0) {
                LinkedTreeMap imageJson = (LinkedTreeMap) object.get(0);
                imageUrl = (String) imageJson.get("src");
            }
        }
        return imageUrl;
    }

    @Override
    public JsonForm toJsonForm() {
        return FormHelper.createForm(this);
    }

    @Override
    public Map getFormKeyValueMap() {
        return this;
    }

    @Override
    public Map getFormMetaDataMap() {
        return AppData.getProductFormMetaData();
    }

    @Override
    public Object getValue(String key) {
        if (key.equals("images"))
            return getImageUrl();
        return get(key);
    }
}
