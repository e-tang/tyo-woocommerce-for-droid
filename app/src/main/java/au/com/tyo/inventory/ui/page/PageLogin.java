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
import android.view.View;
import android.widget.Button;

import au.com.tyo.inventory.Constants;
import au.com.tyo.inventory.Controller;
import au.com.tyo.inventory.R;

/**
 * Created by Eric Tang (eric.tang@tyo.com.au) on 27/11/17.
 */

public class PageLogin extends PageEmailPassword {

    /**
     * @param controller
     * @param activity
     */
    public PageLogin(Controller controller, Activity activity) {
        super(controller, activity);

        setContentViewResId(R.layout.login);
    }

    @Override
    public void setupComponents() {
        super.setupComponents();

        Button button = (Button) findViewById(R.id.btn_scan);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getController().getUi().gotoBarcodeScannerPage(Constants.OPERATION_SCAN_API_KEY);
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();

        if (getController().getAppData().getApi().getAuthentication().hasConsumerKeyPair()) {
            getController().startMainActivity();
            finish();
        }
    }
}
