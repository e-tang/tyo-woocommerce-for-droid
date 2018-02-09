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
import android.text.TextUtils;
import android.util.Base64;
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
import au.com.tyo.utils.RegexUtils;
import au.com.tyo.utils.StringUtils;
import au.com.tyo.woocommerce.WooCommerceApi;
import au.com.tyo.woocommerce.WooCommerceJson;

import static au.com.tyo.app.Constants.MESSAGE_CUSTOM_ONE;

/**
 * Created by Eric Tang (eric.tang@tyo.com.au) on 13/12/17.
 */

public class AppData extends CommonAppData implements ProductContainer {

    private static final String TAG = "AppData";

    public static final String PRODUCTS_JSON_CACHE = "products.json";
    private static final String PASSWORD = "T0fawefwwhlhisIsafwVeryLogh";

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
    private Map<Integer, Product> productMapById;
    private Map<String, Product> productMapByName;

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

    private Resources resources;

    private Controller controller;

    public static class DataUpdate {}

    public static class DataUpdateProduct extends DataUpdate {}

    public AppData(Controller controller, Context context) {
        super(context);

        this.controller = controller;

        this.api = new WooCommerceApi(context);
        this.parser = new WooCommerceJson();

//        productMapById = new HashMap<>();
//        categoryMap = new HashMap<>();

        setCacheManager(new CommonCache(context, new String[] {Constants.FOLDER_CACHE_CATEGORIES, Constants.FOLDER_CACHE_PRODUCTS}));
        barcodeImageCache = new CommonCache(context, "barcode");

        setResources(new Resources(context));
    }

    public Resources getResources() {
        return resources;
    }

    public void setResources(Resources resources) {
        this.resources = resources;
    }

    public CommonCache getBarcodeImageCache() {
        return barcodeImageCache;
    }

    public List<Product> getProducts() {
        return products;
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
        return getCacheDirectory() + File.separator + Constants.FOLDER_CACHE_PRODUCTS;
    }

    private String getCategoryCacheDir(){
        return getCacheDirectory() + File.separator + Constants.FOLDER_CACHE_CATEGORIES;
    }

    public void load(ErrorChecker checker) {
        categories = loadCategories(checker, 1);

        if (null != categories) {
            categoryMap = new HashMap<>();
            for (int i = 0; i < categories.size(); ++i) {
                Category category = categories.get(i);
                category.setIndex(i);

                categoryMap.put(category.getName().toLowerCase(), category);
            }

            products = loadProducts(checker);

            if (null != products) {
                productMapById = new HashMap<Integer, Product>();
                productMapByName = new HashMap<>();
                for (int i = 0; i < products.size(); ++i) {
                    Product product = (Product) products.get(i);
                    product.setIndex(i);

                    productMapById.put(product.getId(), product);

                    try {
                        productMapByName.put((String) product.getUniqueCode(), product);
                    }
                    catch (Exception ex) {
                        Log.e(TAG, "The product code is not set properly", ex);
                    }
                }
            }
        }
    }

    public List loadCategories(ErrorChecker checker, int page) {
        return load(checker, getCategoryCacheDir(), getApi().getProductCategoriesApiUrlWithPageNumber(page), categoryType, categoriesType);
    }

    public List loadProducts(ErrorChecker checker) {
        return load(checker, getProductCacheDir(), getApi().getProductsApiUrl(), productType, productsType);
    }

    private List load(ErrorChecker checker, String cacheDirectory, String url, Type itemType, Type mapType) {
        List list = null;
        // Map map = new HashMap();
        WildcardFileStack fileStack = null;
        try {
            fileStack = new WildcardFileStack(new File(cacheDirectory));
            fileStack.listFiles();
        } catch (Exception e) {
            Log.e(TAG, "error in reading cache files from " + cacheDirectory, e);
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
                    saveCache(cacheDirectory, product);
                }
            }
            catch (Exception ex) {
                Map mapErr;
                try {
                    mapErr = WooCommerceJson.getGson().fromJson(json, HashMap.class);
                    if (mapErr.containsKey("data")) {
                        Map smap = (Map) mapErr.get("data");
                        if (smap.containsKey("status")) {
                            int status = (int) ((double) smap.get("status"));
                            if (status == 401) {
                                getApi().getAuthentication().clearSecret();
                                checker.onLoadDataFailedBecauseOfUnauthorization();
                                return null;
                            }
                        }
                    }
                    checker.onLoadDataFailedGeneral(mapErr);
                    return null;
                }
                catch (Exception ex2) {
                    String msg = "unable pass the server response";
                    Log.e(TAG, msg, ex2);
                    throw new IllegalStateException("");
                }
            }
        }

        Log.d(TAG, "productListItems loaded: total " + list.size());
        return list; //new Object[] {list, map};
    }

    private void saveProductCache(Product product) {
        saveCache(getProductCacheDir(), product);
    }

    private void saveCache(String path, GeneralItem product) {
        String productJson = WooCommerceJson.getGson().toJson(product);
        try {
            getCacheManager().writeText(path + File.separator + product.getId() + ".json", productJson);
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

            if (!productMapById.containsKey(newProductPtr.getId())) {
                newProductPtr.setIndex(products.size());
                products.add(newProductPtr);
            }
            else
                products.set(newProductPtr.getIndex(), newProductPtr);

            productMapById.put(newProductPtr.getId(), newProductPtr);
            saveProductCache(newProductPtr);

            controller.sendMessage(MESSAGE_CUSTOM_ONE);
        }
    }

    public Product updateProduct(Product product, String result) {
        Product newProductPtr;
        try {
            newProductPtr = WooCommerceJson.getGson().fromJson(result, productType);
            newProductPtr.setIndex(product.getIndex());
            updateProduct(newProductPtr);
        }
        catch (Exception ex) {
            newProductPtr = null;
        }

        // if (null != newProductPtr && newProductPtr.getStock() != product.getStock()) {
        // }
        return newProductPtr;
    }

    @Override
    public Product getProductById(int id) {
        return productMapById.get(id);
    }

    public Product getProduct(int position) {
        return products.get(position);
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
        if (hasProduct(product))
            return null;

        String json = product.toJson();
        String result = getApi().createProduct(json);
        Product newProduct = updateProduct(product, result);
        return newProduct;
    }

    public String getCacheDirectory() {
        return getCacheManager().getCacheDir().getAbsolutePath();
    }

    public void importProducts(List<List> table) {

        // temporary solution
        // orders

        for (List row : table) {
            String oem = "";
            String code = "";
            String spec = "";
            String image = "";
            String category = "";
            String name = "";
            String brand = "";
            String series = "";
            String model = "";
            String year = "";
            int quantity = 0;
            String costPerUnit = "";
            String location = "";
            String salePrice = "";

            for (int i = 0; i < row.size(); ++i) {
                String colStr = (String) row.get(i);

                if (colStr == null)
                    continue;
                // for auto
                //
                switch (i) {
                    case 1:
                        oem = colStr;
                        break;
                    case 0:
                        code = colStr;
                        break;
                    case 2:
                        spec = colStr;
                        break;
                    case 3:
                        image = colStr;
                        break;
                    case 4:
                        category = colStr.toLowerCase();
                        break;
                    case 5:
                        name = colStr;
                        break;
                    case 6:
                        brand = colStr;
                        break;
                    case 7:
                        series = colStr;
                        break;
                    case 8:
                        year = colStr;
                        break;
                    case 9:
                        model = colStr;
                        break;
                    case 10:
                        try {
                            quantity = Integer.parseInt(colStr);
                        }
                        catch (Exception e) {}

                        break;
                    case 11:
                        costPerUnit = colStr;
                        break;
                    case 12:
                        break;
                    case 13:
                        location = colStr;
                        break;
                    case 14:
                        salePrice = colStr;
                        break;
                    case 15:
                        break;
                }

            }

            if (TextUtils.isEmpty(oem) && TextUtils.isEmpty(code))
                continue;

            if (!RegexUtils.containsNumber(oem) && !RegexUtils.containsNumber(code))
                continue;

            Product product = new Product();
            product.setName(StringUtils.join(" ", oem, code, name, series, model, year));
            product.setDescription(String.format(resources.getTemplateDescription(),
                    oem.length() > 0 ? oem : (code.length() > 0 ? code : "N/A"),
                    brand));

            product.setInStock(quantity > 0);
            product.setStock(quantity);
            product.setManageStock(true);
            product.setProductTypeSimple();
            product.setPrice(salePrice);

            if (!TextUtils.isEmpty(oem))
                product.setAttribute("MPN", oem);

            if (!TextUtils.isEmpty(brand))
                product.setAttribute("Brand", brand);

            if (!TextUtils.isEmpty(code))
                product.setAttribute("Code", code);

            // TODO
            // create image prefix in preferences
            product.setImage("http://fred-auto-parts-store1.appspot.com.storage.googleapis.com/" + code + ".jpg");

            Category cat = findCategory(category);

            if (cat != null)
                product.setCategory(cat.getId());

            importProduct(product);
        }
    }

    public Category findCategory(String category) {
        return categoryMap.get(category);
    }

    private String encryptCost(String cost) {
        byte[] source = cost.getBytes();
       byte[] sb = new byte[source.length];
        byte[] password = PASSWORD.getBytes();

        for (int i = 0; i < source.length; ++i) {
            sb[i] = (byte) (password[i] + source[i]);
        }

        String target = Base64.encodeToString(sb, Base64.DEFAULT);
        return target;
    }

    private String decryptCost(String s) {
        byte[] source = Base64.decode(s, Base64.DEFAULT);
        byte[] sb = new byte[source.length];
        byte[] password = PASSWORD.getBytes();
        for (int i = 0; i < source.length; ++i) {
            sb[i] = (byte) (source[i] - password[i]);
        }
        return new String(sb);
    }

    public void importCategories(String data) {
        List<String> cats = new ArrayList<String>();
        cats = WooCommerceJson.getGson().fromJson(data, cats.getClass());

        importCategories(cats);
    }

    public boolean hasProduct(Product product) {
//        for (Product p : products) {
//            if (p.getName().equals(product.getName()))
//                return true;
//        }
//        return false;
        String code = product.getUniqueCode();
        if (null == code)
            return false;
        return productMapByName.containsKey(code);
    }

}
