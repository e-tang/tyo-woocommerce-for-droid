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

package au.com.tyo.inventory.model;

import java.util.Map;

import au.com.tyo.inventory.AppData;

/**
 * Created by Eric Tang (eric.tang@tyo.com.au) on 2/1/18.
 */

public class ProductStockInForm extends ProductParcel {

//    public ProductStockIn() {
//        put("name", null);
//        put("currentStock", 0);
//        put("newStock", 0);
//    }

//    @Key
//    public String name;
//
//    @Key
//    public String currentStock;
//
//    @Key
    public int newStock;

    public ProductStockInForm(ProductContainer productContainer, int productId) {
        super(productContainer, productId);
    }

    public int getNewStock() {
        return newStock;
    }

    public void setNewStock(int newStock) {
        this.newStock = newStock;
    }

    //
//    public int getNewStockCount() {
//        return Integer.parseInt(newStock);
//    }

    @Override
    public Map getFormMetaDataMap() {
        return AppData.getProductStockInMetaData();
    }

    @Override
    public void put(String key, Object value) {
        if (key.equals("newStock"))
            setNewStock(Integer.parseInt(value.toString()));
        else
            super.put(key, value);
    }
}
