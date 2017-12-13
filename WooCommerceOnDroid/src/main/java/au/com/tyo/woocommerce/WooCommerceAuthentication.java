package au.com.tyo.woocommerce;

import android.content.Context;

import au.com.tyo.services.sn.Message;
import au.com.tyo.services.sn.SNBase;

/**
 * Created by Eric Tang (eric.tang@tyo.com.au) on 28/11/17.
 */

public class WooCommerceAuthentication extends SNBase {

    private String userId = "wc";

    public WooCommerceAuthentication(Context context) {
        if (WooCommerceSecrets.CONSUMER_KEY == null || WooCommerceSecrets.CONSUMER_SECRET == null) {
            consumerKey = context.getResources().getString(R.string.oauth_key);
            consumerKeySecret = context.getResources().getString(R.string.oauth_secret);
        }
        else {
            consumerKey = WooCommerceSecrets.CONSUMER_KEY;
            consumerKeySecret = WooCommerceSecrets.CONSUMER_SECRET;
        }
    }

    @Override
    public String getUserAlias() {
        return null;
    }

    @Override
    public String getUserName() {
        return null;
    }

    @Override
    public String getUserAvatarUrl() {
        return null;
    }

    @Override
    public void retrieveAccessToken() throws Exception {

    }

    private void saveSecrets() {
        secretOAuth.getId().setToken(String.valueOf(userId));

        secretOAuth.getToken().setToken(consumerKey);
        secretOAuth.getToken().setSecret(consumerKeySecret);

        secrets.save(secretOAuth.getId());
        secrets.save(secretOAuth.getToken());
    }

    @Override
    public boolean isAuthenticated() {
        return false;
    }

    @Override
    public void authenticate() throws Exception {

    }

    @Override
    public void postStatus(Message msg) throws Exception {

    }

    @Override
    public void addPeopleInNetwork() throws Exception {

    }

    @Override
    public void createInstance() {

    }
}
