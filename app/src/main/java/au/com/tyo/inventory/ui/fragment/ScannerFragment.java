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

package au.com.tyo.inventory.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.zxing.Result;

import au.com.tyo.app.CommonFragment;
import au.com.tyo.inventory.Controller;
import me.dm7.barcodescanner.zxing.ZXingScannerView;

/**
 * Created by Eric Tang (eric.tang@tyo.com.au) on 29/12/17.
 */

public class ScannerFragment extends CommonFragment<Controller> implements ZXingScannerView.ResultHandler {

    private ZXingScannerView mScannerView;

    private ZXingScannerView.ResultHandler resultHandler;

    public void setResultHandler(ZXingScannerView.ResultHandler resultHandler) {
        this.resultHandler = resultHandler;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mScannerView = new ZXingScannerView(getActivity());
        return mScannerView;
    }

    @Override
    public void onResume() {
        super.onResume();
        mScannerView.setResultHandler(resultHandler);
        mScannerView.startCamera();
    }

    @Override
    public void handleResult(Result rawResult) {
//        Toast.makeText(getActivity(), "Contents = " + rawResult.getText() +
//                ", Format = " + rawResult.getBarcodeFormat().getName(), Toast.LENGTH_SHORT).show();
//        // Note:
//        // * Wait 2 seconds to resume the preview.
//        // * On older devices continuously stopping and resuming camera preview can result in freezing the app.
//        // * I don't know why this is the case but I don't have the time to figure out.
//        Handler handler = new Handler();
//        handler.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                mScannerView.resumeCameraPreview(ScannerFragment.this);
//            }
//        }, 2000);

    }

    @Override
    public void onPause() {
        super.onPause();
        mScannerView.stopCamera();
    }

}
