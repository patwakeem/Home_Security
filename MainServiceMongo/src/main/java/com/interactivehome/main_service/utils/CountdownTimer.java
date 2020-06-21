package com.interactivehome.main_service.utils;

import org.json.JSONException;

import java.util.Timer;
import java.util.TimerTask;

public class CountdownTimer extends Timer
{
    private enum timer_state {
        started,
        expired,
        stopped
    }

    private timer_state timerState;

    class TimerExpired extends TimerTask {
        public void run() {
            try {
                verificationTimerExpired();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public void verificationTimerStart(int seconds) {
        schedule(new TimerExpired(),seconds * 1000);
        timerState = timer_state.started;
    }

    public void verificationTimerStop() {
        if(timerState == timer_state.started) {
            this.cancel();
            timerState = timer_state.stopped;
        }
    }

    private void verificationTimerExpired() throws JSONException {
        this.cancel();
        timerState = timer_state.expired;
    }
}
