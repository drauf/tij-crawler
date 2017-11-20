package crawler.threadpool;

import java.util.Collection;
import java.util.concurrent.Callable;

public interface ThreadPool {
    void shutdown();
    void submit(Runnable task);
    <T> void invokeAll(Collection<? extends Callable<T>> tasks) throws InterruptedException;
    <T> void invokeAllAndAwait(Collection<? extends Callable<T>> tasks) throws InterruptedException;
}
