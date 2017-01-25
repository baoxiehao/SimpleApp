package com.yekong.droid.simpleapp.util;

/**
 * Created by baoxiehao on 17/1/26.
 */

public class CrashHandler implements Thread.UncaughtExceptionHandler {

    private static final class Holder {
        private static final CrashHandler INSTANCE = new CrashHandler();
    }

    private CrashHandler() {

    }

    public static CrashHandler getInstance() {
        return Holder.INSTANCE;
    }

    private Thread.UncaughtExceptionHandler mDefaultHandler;

    public void init() {
        mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();
        Thread.setDefaultUncaughtExceptionHandler(this);
    }

    @Override
    public void uncaughtException(Thread thread, Throwable throwable) {
        if (handleException(throwable)) {
            Logger.e("ignore throwable", throwable);
        } else if (mDefaultHandler != null) {
            mDefaultHandler.uncaughtException(thread, throwable);
        }
    }

    private boolean handleException(Throwable ex) {
        Toaster.quick("Handle exception: " + ex);
        return true;
    }
}
