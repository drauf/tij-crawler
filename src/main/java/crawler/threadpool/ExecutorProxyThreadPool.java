package crawler.threadpool;

import java.util.Collection;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ExecutorProxyThreadPool implements ThreadPool {

    private final ExecutorService executorService;

    public ExecutorProxyThreadPool(int numberOfThreads) {
        executorService = Executors.newFixedThreadPool(numberOfThreads);
    }

    @Override
    public void shutdown() {
        executorService.shutdown();
    }

    @Override
    public void submit(Runnable task) {
        executorService.submit(task);
    }

    @Override
    public <T> void invokeAll(Collection<? extends Callable<T>> tasks) throws InterruptedException {
        executorService.invokeAll(tasks);
    }

    @Override
    public <T> void invokeAllAndAwait(Collection<? extends Callable<T>> tasks) throws InterruptedException {
        executorService.invokeAll(tasks);
    }
}
