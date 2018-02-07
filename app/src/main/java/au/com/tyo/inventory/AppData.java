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

package au.com.tyo.inventory;

import android.content.Context;
import android.util.Log;

import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import au.com.tyo.android.CommonCache;
import au.com.tyo.app.CommonAppData;
import au.com.tyo.inventory.model.Category;
import au.com.tyo.inventory.model.GeneralItem;
import au.com.tyo.inventory.model.Product;
import au.com.tyo.inventory.model.ProductContainer;
import au.com.tyo.inventory.model.ProductForm;
import au.com.tyo.inventory.model.ProductFormMetaData;
import au.com.tyo.inventory.model.ProductStockInMetaData;
import au.com.tyo.io.WildcardFileStack;
import au.com.tyo.woocommerce.WooCommerceApi;
import au.com.tyo.woocommerce.WooCommerceJson;

/**
 * Created by Eric Tang (eric.tang@tyo.com.au) on 13/12/17.
 */

public class AppData extends CommonAppData implements ProductContainer {

    private static final String TAG = "AppData";

    public static final String PRODUCTS_JSON_CACHE = "products.json";

    //
    // API
    //
    private WooCommerceApi api;

    //
    // PARSER
    //
    private WooCommerceJson parser;

    //
    // MODELS
    //
    /**
     * Product list from json
     */
    private List<Product> products;

    /**
     * Product list for listview
     */
    private Map<Integer, Product> productMap;

    /**
     * Product list from json
     */
    private List<Category> categories;

    /**
     * Product list for listview
     */
    private Map<String, Category> categoryMap;

    private static ProductFormMetaData productFormMetaData;

    private static Type productsType = new TypeToken<List<Product>>(){}.getType();
    private static Type productType = new TypeToken<Product>(){}.getType();
    private static Type productStockInMetaDataType = new TypeToken<ProductStockInMetaData>(){}.getType();

    private static Type categoriesType = new TypeToken<List<Category>>(){}.getType();
    private static Type categoryType = new TypeToken<Category>(){}.getType();

    private static ProductStockInMetaData productStockInMetaData;

    private CommonCache barcodeImageCache;

    public AppData(Context context) {
        super(context);

        this.api = new WooCommerceApi(context);
        this.parser = new WooCommerceJson();

//        productMap = new HashMap<>();
//        categoryMap = new HashMap<>();

        setCacheManager(new CommonCache(context, "products"));
        barcodeImageCache = new CommonCache(context, "barcode");
    }

    public CommonCache getBarcodeImageCache() {
        return barcodeImageCache;
    }

    public List<ProductForm> getProductList() {
        if (null == products || products.size() == 0)
            return null;
        return createProductList();
    }

    public WooCommerceApi getApi() {
        return api;
    }

    private String getProductCacheDir(){
        return getCacheDirectory() + File.separator + "products";
    }

    private String getCategoryCacheDir(){
        return getCacheDirectory() + File.separator + "categories";
    }

    public void load(ErrorChecker checker) {
        Object[] objects;
        objects = loadCategories(checker);

        if (null != objects) {
            categories = (List<Category>) objects[0];
            categoryMap = (Map<String, Category>) objects[1];

            objects = loadProducts(checker);

            if (null != objects) {
                products = (List<Product>) objects[0];
                productMap = (Map<Integer, Product>) objects[1];
            }
        }
    }

    public Object[] loadCategories(ErrorChecker checker) {
        return load(checker, getCategoryCacheDir(), getApi().getProductCategoriesApiUrl(), categoryType, categoriesType);
    }

    public Object[] loadProducts(ErrorChecker checker) {
        return load(checker, getCategoryCacheDir(), getApi().getProductsApiUrl(), productType, productsType);
    }

    private Object[] load(ErrorChecker checker, String cacheDirectory, String url, Type itemType, Type mapType) {
        List list = null;
        Map map = new HashMap();
        WildcardFileStack fileStack = null;
        try {
            fileStack = new WildcardFileStack(new File(cacheDirectory));
            fileStack.listFiles();
        } catch (Exception e) {

        }
        if (fileStack != null && fileStack.size() > 0) {
            list = new ArrayList();
            File file = fileStack.next();
            while (null != file) {
                try {
                    String productJson = getCacheManager().readText(file);
                    GeneralItem product = WooCommerceJson.getGson().fromJson(productJson, itemType);
                    list.add(product);
                    file = fileStack.next();
                }
                catch (Exception ex) {
                    // if any errors we clear the cache
                    list.clear();
                    getCacheManager().clear();
                    break;
                }
            }
        }

        String json = null;
        if (null == list || list.size() == 0) {
            try {
                json = getApi().get(url);
                list = WooCommerceJson.getGson().fromJson(json, mapType);

                for (int i = 0; i < list.size(); ++i) {
                    GeneralItem product = (GeneralItem) list.get(i);
                    saveCache(product);
                }
            }
            catch (Exception ex) {
                Map mapErr;
                try {
                    mapErr = WooCommerceJson.getGson().fromJson(json, HashMap.class);
                    if (mapErr.containsKey("data")) {
                        Map smap = (Map) map.get("data");
                        if (smap.containsKey("status")) {
                            int status = (int) ((double) smap.get("status"));
                            if (status == 401) {
                                getApi().getAuthentication().clearSecret();
                                checker.onLoadDataFailedBecauseOfUnauthorization();
                                return null;
                            }
                        }
                    }
                    checker.onLoadDataFailedGeneral(map);
                    return null;
                }
                catch (Exception ex2) {
                    String msg = "unable pass the server response";
                    Log.e(TAG, msg, ex2);
                    throw new IllegalStateException("");
                }
            }
        }

        for (int i = 0; i < list.size(); ++i) {
            GeneralItem product = (GeneralItem) list.get(i);
            product.setIndex(i);

            map.put(product.getId(), product);
        }

        Log.d(TAG, "productListItems loaded: total " + list.size());
        return new Object[] {list, map};
    }

    public void loadProducts1(ErrorChecker checker) {

        String json;

        WildcardFileStack fileStack = null;
        try {
            fileStack = new WildcardFileStack(getCacheManager().getCacheDir());
            fileStack.listFiles();
        } catch (Exception e) {

        }
        if (fileStack != null && fileStack.size() > 0) {
            products = new ArrayList();
            File file = fileStack.next();
            while (null != file) {
                try {
                    String productJson = getCacheManager().readText(file);
                    Product product = WooCommerceJson.getGson().fromJson(productJson, productType);
                    products.add(product);
                    file = fileStack.next();
                }
                catch (Exception ex) {
                    // if any errors we clear the cache
                    products.clear();
                    getCacheManager().clear();
                    break;
                }
            }
        }

        if (null == products || products.size() == 0) {
            json = api.getProductsJsonString();
            try {
                products = WooCommerceJson.getGson().fromJson(json, productsType);

                for (int i = 0; i < products.size(); ++i) {
                    Product product = products.get(i);
                    saveProductCache(product);
                }
            }
            catch (Exception ex) {
                Map map;
                try {
                    map = WooCommerceJson.getGson().fromJson(json, HashMap.class);
                    if (map.containsKey("data")) {
                        Map smap = (Map) map.get("data");
                        if (smap.containsKey("status")) {
                            int status = (int) ((double) smap.get("status"));
                            if (status == 401) {
                                getApi().getAuthentication().clearSecret();
                                checker.onLoadDataFailedBecauseOfUnauthorization();
                                return;
                            }
                        }
                    }
                    checker.onLoadDataFailedGeneral(map);
                    return;
                }
                catch (Exception ex2) {
                    String msg = "unable pass the server response";
                    Log.e(TAG, msg, ex2);
                    throw new IllegalStateException("");
                }
            }
        }

        for (int i = 0; i < products.size(); ++i) {
            Product product = products.get(i);
            product.setIndex(i);

            productMap.put(product.getId(), product);
        }

        Log.d(TAG, "productListItems loaded: total " + products.size());
    }

    private void saveProductCache(Product product) {
        String productJson = WooCommerceJson.getGson().toJson(product);
        try {
            getCacheManager().writeText("" + product.getId() + ".json", productJson);
        } catch (Exception e) {
            Log.e(TAG, au.com.tyo.utils.StringUtils.exceptionStackTraceToString(e));
        }
    }

    private void saveCache(GeneralItem product) {
        String productJson = WooCommerceJson.getGson().toJson(product);
        try {
            getCacheManager().writeText("" + product.getId() + ".json", productJson);
        } catch (Exception e) {
            Log.e(TAG, au.com.tyo.utils.StringUtils.exceptionStackTraceToString(e));
        }
    }

    public List<ProductForm> createProductList() {
        List<ProductForm> productListItems;
        productListItems = new ArrayList<>();
        for (Product product : products)
            productListItems.add(new ProductForm(this, product.getId()));
        return productListItems;
    }

    public void load() {
        loadProductFormMetaData();
        loadProductStockInFormMetaData();
    }

    private void loadProductStockInFormMetaData() {
        String json = assetToString("product-stock-in-form.json");
        productStockInMetaData = WooCommerceJson.getGson().fromJson(json, productStockInMetaDataType);
    }

    private void loadProductFormMetaData() {
        Type dataType = new TypeToken<ProductFormMetaData>(){}.getType();
        String json = assetToString("product-form.json");
        productFormMetaData = WooCommerceJson.getGson().fromJson(json, dataType);
    }

    public static ProductFormMetaData getProductFormMetaData() {
        return productFormMetaData;
    }

    public static ProductStockInMetaData getProductStockInMetaData() {
        return productStockInMetaData;
    }

    public Product lookupProductById(int id) {
        for (int i = 0; i < products.size(); ++i) {
            Product product = products.get(i);

            if (product.getId() == id) {
                return product;
            }
        }
        return null;
    }

    public void updateProductStock(Product product, int stock) {
        int oldStock = product.getStock();
        if (oldStock < 0)
            oldStock = 0;

        String result = api.updateProductStock(product.getId(), oldStock + stock);
        updateProduct(product, result);
    }

    private void updateProduct(Product newProductPtr) {
        if (null != newProductPtr) {
            productMap.put(newProductPtr.getId(), newProductPtr);
            saveProductCache(newProductPtr);
        }
    }

    public Product updateProduct(Product product, String result) {
        Product newProductPtr;
        try {
            newProductPtr = WooCommerceJson.getGson().fromJson(result, productType);
        }
        catch (Exception ex) {
            newProductPtr = product;
        }

        // if (null != newProductPtr && newProductPtr.getStock() != product.getStock()) {
            updateProduct(newProductPtr);
        // }
        return newProductPtr;
    }

    @Override
    public Product getProduct(int id) {
        return productMap.get(id);
    }


    public void importCategories(List<String> cats) {
        for (String cat : cats)
            importCategory(cat);
    }

    public void importCategory(String cat) {
        String json = getApi().createProductCategory(cat);
        Log.i(TAG, json);
    }

    public Product importProduct(Product product) {
        String json = product.toString();
        String result = getApi().createProduct(json);
        Product newProduct = updateProduct(product, result);
        return newProduct;
    }

    public String getCacheDirectory() {
        return getCacheManager().getCacheDir().getAbsolutePath();
    }
}
