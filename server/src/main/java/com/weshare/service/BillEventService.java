package com.weshare.service;

import com.weshare.dto.BillEvent;
import org.springframework.context.SmartLifecycle;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

@Service
public class BillEventService implements SmartLifecycle {

    private final List<SseEmitter> emitters = new CopyOnWriteArrayList<>();
    private final ScheduledExecutorService scheduler =
        Executors.newSingleThreadScheduledExecutor(runnable -> {
            Thread thread = new Thread(runnable, "bill-event-heartbeat");
            thread.setDaemon(true);
            return thread;
        });

    private final ExecutorService publisherPool = Executors.newCachedThreadPool();
    private final AtomicBoolean running = new AtomicBoolean(false);

    public BillEventService() {
        scheduler.scheduleAtFixedRate(this::sendHeartbeat, 15, 15, TimeUnit.SECONDS);
    }

    public SseEmitter subscribe() {
        SseEmitter emitter = new SseEmitter(0L);
        emitters.add(emitter);
        emitter.onCompletion(() -> emitters.remove(emitter));
        emitter.onTimeout(() -> emitters.remove(emitter));
        return emitter;
    }

    public void publish(BillEvent event) {
        publisherPool.submit(() ->
            emitters.removeIf(emitter -> {
                try {
                    emitter.send(SseEmitter.event()
                        .name("bill-event")
                        .reconnectTime(3000)
                        .data(event));
                    return false;
                }
                catch (Exception e) {
                    return true;
                }
            })
        );
    }

    private void sendHeartbeat() {
        emitters.removeIf(emitter -> {
            try {
                emitter.send(SseEmitter.event()
                    .name("heartbeat")
                    .reconnectTime(3000)
                    .data("ping"));
                return false;
            }
            catch (Exception e) {
                return true;
            }
        });
    }

    @Override
    public void start() {
        running.set(true);
    }

    @Override
    public void stop() {
        emitters.forEach(SseEmitter::complete);
        emitters.clear();
        scheduler.shutdown();
        publisherPool.shutdown();
        running.set(false);
    }

    @Override
    public boolean isRunning() {
        return running.get();
    }

}