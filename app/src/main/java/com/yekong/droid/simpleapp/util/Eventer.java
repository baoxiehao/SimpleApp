package com.yekong.droid.simpleapp.util;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by baoxiehao on 16/12/1.
 */

public class Eventer {

    public static class TopEvent {
        public static void send() {
            EventBus.getDefault().post(new TopEvent());
        }
    }

    public static class PositionEvent {
        public int pos;

        public PositionEvent(int pos) {
            this.pos = pos;
        }

        public static void send(int pos) {
            EventBus.getDefault().post(new PositionEvent(pos));
        }
    }
}
