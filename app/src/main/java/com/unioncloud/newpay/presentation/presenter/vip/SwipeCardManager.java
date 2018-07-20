package com.unioncloud.newpay.presentation.presenter.vip;

import android.util.Log;
import android.widget.Toast;

import com.pax.baselib.card.MagCard;
import com.pax.baselib.card.MagException;
import com.pax.baselib.card.TrackData;
import com.raizlabs.coreutils.events.Event;
import com.raizlabs.coreutils.functions.Delegate;

/**
 * Created by cwj on 16/8/17.
 */
public class SwipeCardManager {

    private static SwipeCardManager instance;
    private Event<TrackData> dateChanged = new Event<>();
    private MagReaderThread magReaderThread;


    public static SwipeCardManager getInstance() {
        if (instance == null) {
            instance = new SwipeCardManager();
        }
        return instance;
    }

    private SwipeCardManager() {

    }

    public Event<TrackData> getDateChangedEvent() {
        return dateChanged;
    }

    public void addSwipeListener(Delegate<TrackData> listener) {
        getDateChangedEvent().addListener(listener);
        start();
    }

    public void removeSwipeListener(Delegate<TrackData> listener) {
        getDateChangedEvent().removeListener(listener);
        stop();
    }

    private void start() {
//        if(magReaderThread != null) {
//            magReaderThread.stopMagReader();
//            magReaderThread = null;
//        }
        if (magReaderThread != null && magReaderThread.isRunning()) {
            return;
        }
        magReaderThread = new MagReaderThread("reader--");
        magReaderThread.start();
    }

    private void stop() {
        if(magReaderThread != null) {
            magReaderThread.stopMagReader();
            magReaderThread = null;
        }
    }

    private class MagReaderThread extends Thread {

        private boolean running;

        public MagReaderThread(String threadName) {
            super(threadName);
            this.running = true;
        }

        public void stopMagReader() {
            running = false;
        }

        public boolean isRunning() {
            return running;
        }

        @Override
        public void run() {
//            先reset磁卡阅读器
            try{
                MagCard magCard = MagCard.getInstance();

                magCard.getInstance().reset();
            }catch (MagException e){
//                Toast.makeText()
                Log.i("----reset error----",e.toString());
            }
            while (running) {
                try {
                    TrackData trackData = MagCard.getInstance().read();
                    if (trackData != null) {
                        dateChanged.raiseEvent(trackData);
                    }
                } catch (MagException e) {
                    e.printStackTrace();
                }
                try {
                    Thread.sleep(300);
                } catch (Exception e) {
                }
            }
        }
    }
}
