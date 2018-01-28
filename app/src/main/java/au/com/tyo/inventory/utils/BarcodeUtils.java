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

package au.com.tyo.inventory.utils;

import android.graphics.Bitmap;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;

import java.util.EnumMap;
import java.util.Map;

/**
 * Created by Eric Tang (eric.tang@tyo.com.au) on 29/12/17.
 */

public class BarcodeUtils {

    private static final int WHITE = 0xFFFFFFFF;
    private static final int BLACK = 0xFF000000;

    public static Bitmap toQRCodeBitmap(String content, int imgWidth, int imgHeight) throws WriterException {
        return encodeAsBitmap(content, BarcodeFormat.QR_CODE, imgWidth, imgHeight);
    }

    public static Bitmap encodeAsBitmap(String content, BarcodeFormat format, int imgWidth, int imgHeight) throws WriterException {
        String contentsToEncode = content;
        if (contentsToEncode == null) {
            return null;
        }
        Map<EncodeHintType, Object> hints = null;
        String encoding = "UTF-8";
        if (encoding != null) {
            hints = new EnumMap<EncodeHintType, Object>(EncodeHintType.class);
            hints.put(EncodeHintType.CHARACTER_SET, encoding);
        }
        MultiFormatWriter writer = new MultiFormatWriter();
        BitMatrix result;
        try {
            result = writer.encode(contentsToEncode, format, imgWidth, imgHeight, hints);
        } catch (IllegalArgumentException iae) {
            // Unsupported format
            return null;
        }
        int width = result.getWidth();
        int height = result.getHeight();
        int[] pixels = new int[width * height];
        for (int y = 0; y < height; y++) {
            int offset = y * width;
            for (int x = 0; x < width; x++) {
                pixels[offset + x] = result.get(x, y) ? BLACK : WHITE;
            }
        }

        Bitmap bitmap = Bitmap.createBitmap(width, height,
                Bitmap.Config.ARGB_8888);
        bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
        return bitmap;
    }

    /**
     * The following code is from Let'sRefactor@StackOverflow
     *
     * https://stackoverflow.com/questions/35104305/how-to-generate-qr-code-with-logo-inside-it
     *
     *
     * However BufferedImage class is not part of Android SDK
     *
        public BufferedImage getQRCodeWithOverlay(BufferedImage qrcode) {
            BufferedImage scaledOverlay = scaleOverlay(qrcode);

            Integer deltaHeight = qrcode.getHeight() - scaledOverlay.getHeight();
            Integer deltaWidth  = qrcode.getWidth()  - scaledOverlay.getWidth();

            BufferedImage combined = new BufferedImage(qrcode.getWidth(), qrcode.getHeight(), BufferedImage.TYPE_INT_ARGB);
            Graphics2D g2 = (Graphics2D)combined.getGraphics();
            g2.drawImage(qrcode, 0, 0, null);
            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, overlayTransparency));
            g2.drawImage(scaledOverlay, Math.round(deltaWidth/2), Math.round(deltaHeight/2), null);
            return combined;
        }

        private BufferedImage scaleOverlay(BufferedImage qrcode) {
            Integer scaledWidth = Math.round(qrcode.getWidth() * overlayToQRCodeRatio);
            Integer scaledHeight = Math.round(qrcode.getHeight() * overlayToQRCodeRatio);

            BufferedImage imageBuff = new BufferedImage(scaledWidth, scaledHeight, BufferedImage.TYPE_INT_ARGB);
            Graphics g = imageBuff.createGraphics();
            g.drawImage(overlay.getScaledInstance(scaledWidth, scaledHeight, BufferedImage.SCALE_SMOOTH), 0, 0, new Color(0,0,0), null);
            g.dispose();
            return imageBuff;
        }

     */
    /// TODO
}
