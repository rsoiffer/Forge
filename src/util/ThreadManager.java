package util;

import engine.Core;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public abstract class ThreadManager {

    private static final Queue<Runnable> toRun = new ConcurrentLinkedQueue();

    static {
        Core.update.onEvent(() -> {
            while (!toRun.isEmpty()) {
                toRun.poll().run();
            }
        });
    }

    public static void onMainThread(Runnable r) {
        toRun.add(r);
    }
}
