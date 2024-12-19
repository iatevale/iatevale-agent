package org.iatevale.util.timer;

import java.time.Duration;
import java.time.Instant;

public class ElapsedTimer {

    private Duration duration;
    private Instant finish;

    public ElapsedTimer(Duration duration) {
        this.duration = duration;
    }

    public void start() {
        setFinish(Instant.now().plus(duration));
    }

    public void setNow() {
        setFinish(Instant.now());
    }

    public boolean isSet() {
        if (Instant.now().isAfter(getFinish())) {
            return false;
        } else {
            return true;
        }
    }

    public Instant getFinishDate() {
        return finish;
    }

    private Instant getFinish() {
        synchronized (this) {
            return this.finish;
        }
    }

    private void setFinish(Instant finish) {
        synchronized (this) {
            this.finish = finish;
        }
    }
}