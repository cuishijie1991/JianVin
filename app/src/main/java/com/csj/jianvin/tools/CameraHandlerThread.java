package com.csj.jianvin.tools;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.util.Log;

/**
 * Created by cuishijie on 2018/1/7.
 */

public class CameraHandlerThread extends HandlerThread implements Handler.Callback {
    private Handler mHandler;
    public static final int PROCESS_FRAME = 100;
    private Tesseract mTesseract;

    public CameraHandlerThread(String name) {
        super(name);
        start();
        mHandler = new Handler(getLooper(), this);
    }

    public void setTesseract(Tesseract tesseract, int width, int height, int bpp, int bpl) {
        mTesseract = tesseract;
        this.width = width;
        this.height = height;
        this.bpp = bpp;
        this.bpl = bpl;
    }

    int width, height, bpp, bpl;

    @Override
    public boolean handleMessage(Message msg) {
        if (msg.what == PROCESS_FRAME) {
            byte[] data = (byte[]) msg.obj;
            String result = mTesseract.extractText(data, width, height, bpp, bpl);
            if ("empty result".equals(result)) {
                Log.d("Tesseract", "empty result");
            } else {
                Log.e("Tesseract", result);
            }
        }
        return false;
    }

    public Handler getHandler() {
        return mHandler;
    }
}
