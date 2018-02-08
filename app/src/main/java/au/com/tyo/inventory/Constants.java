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

package au.com.tyo.inventory;

/**
 * Created by Eric Tang (eric.tang@tyo.com.au) on 2/2/18.
 */

public interface Constants extends au.com.tyo.app.Constants {

    int OPERATION_SCAN_PRODUCT = 0;
    int OPERATION_SCAN_API_KEY = 1;


    String FOLDER_APP = "TyoInventory";
    String FILE_IMPORT = "import.tsv";

    String FOLDER_CACHE_PRODUCTS = "products";
    String FOLDER_CACHE_CATEGORIES = "categories";

    String FOLDER_DOWNLOAD = "Download";

    String DATA_IMPORT_TYPE = "IMPORT_TYPE";
    int IMPORT_TYPE_PRODUCT = 1;
    int IMPORT_TYPE_CATEGORY = 2;

}
