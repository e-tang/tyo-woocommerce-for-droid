package au.com.tyo.woocommerce;

import android.content.Context;
import android.util.Log;

import au.com.tyo.android.utils.Base64;
import au.com.tyo.app.api.CommonApiJson;

/**
 * Created by Eric Tang (eric.tang@tyo.com.au) on 27/11/17.
 */

public class WooCommerceApi extends CommonApiJson {

    private static final String TAG = WooCommerceApi.class.getSimpleName();

    private WooCommerceAuthentication authentication;

    private String productsPath;

    private String basicAuthorizationString;

    private String authorizationParameters;

    public WooCommerceApi(Context context) {
        super(context.getResources().getString(R.string.common_api_protocol),
                context.getResources().getString(R.string.common_api_host),
                context.getResources().getString(R.string.common_api_path));

        productsPath = context.getResources().getString(R.string.woocommerce_rest_api_path_products);

        setAuthentication(new WooCommerceAuthentication(context));

        // setParser(new WooCommerceJson());

        if (getAuthentication().hasConsumerKeyPair())
            createAuthorizationStrings();
    }

    private void createAuthorizationStrings() {
        String key = authentication.getConsumerKey();
        String secret = authentication.getConsumerKeySecret();
        basicAuthorizationString = key + ":" + secret;
        Log.d(TAG, "Basic " + Base64.encode(basicAuthorizationString.getBytes()));
        authorizationParameters = String.format("consumer_key=%s&consumer_secret=%s", key, secret);
    }

    public WooCommerceAuthentication getAuthentication() {
        return authentication;
    }

    public void setAuthentication(WooCommerceAuthentication authentication) {
        this.authentication = authentication;
    }

    public String getProductsApiUrl() {
        return getBaseUrl() + productsPath;
    }

    public String getProductsJsonString() {
        String json;

        json = loadUrl(getProductsApiUrl() + "?" + authorizationParameters);

        return json;
    }

    public void createCurlCommands() {
        createCurlCommandListAllProducts();
    }

    private void createCurlCommandListAllProducts() {
        Log.d(TAG, "curl " + getProductsApiUrl() + " -u " + basicAuthorizationString);
        Log.d(TAG, "curl " + getProductsApiUrl() + "?" + authorizationParameters);
    }
}
