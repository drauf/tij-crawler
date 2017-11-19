package crawler;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class CustomThreadPool implements ExecutorService {

    private final AtomicBoolean execute;
    private Collection<SimpleThread> threads;
    private final ConcurrentLinkedQueue<Callable<?>> callables;

    private static class ThreadPoolException extends RuntimeException {
        ThreadPoolException(Throwable cause) {
            super(cause);
        }
    }

    private class SimpleThread extends Thread {
        private AtomicBoolean execute;
        private ConcurrentLinkedQueue<Callable<?>> runnables;

        SimpleThread(AtomicBoolean execute, ConcurrentLinkedQueue<Callable<?>> runnables) {
            super();
            this.execute = execute;
            this.runnables = runnables;
        }

        @Override
        public void run() {
            try {
                while (execute.get() || !runnables.isEmpty()) {
                    Callable<?> callable;
                    while ((callable = runnables.poll()) != null) {
                        callable.call();
                    }
                    Thread.sleep(1);
                }
            } catch (Exception e) {
                throw new ThreadPoolException(e);
            }
        }
    }

    public CustomThreadPool(int nThreads) {
        execute = new AtomicBoolean(true);
        callables = new ConcurrentLinkedQueue<>();
        threads = Stream.generate(() -> new SimpleThread(execute, callables)).limit(nThreads).collect(Collectors.toList());
    }

    @Override
    public void shutdown() {
        execute.set(false);
    }

    @Override
    public List<Runnable> shutdownNow() {
        throw new NotImplementedException();
    }

    @Override
    public boolean isShutdown() {
        return execute.get();
    }

    @Override
    public boolean isTerminated() {
        throw new NotImplementedException();
    }

    @Override
    public boolean awaitTermination(long timeout, TimeUnit unit) throws InterruptedException {
        throw new NotImplementedException();
    }

    @Override
    public <T> Future<T> submit(Callable<T> task) {
        throw new NotImplementedException();
    }

    @Override
    public <T> Future<T> submit(Runnable task, T result) {
        throw new NotImplementedException();
    }

    @Override
    public Future<?> submit(Runnable task) {
        callables.add(RunnableAdapter.callable(task));
        return CompletableFuture.completedFuture(null);
    }

    @Override
    public <T> List<Future<T>> invokeAll(Collection<? extends Callable<T>> tasks) throws InterruptedException {
        callables.addAll(tasks);

        while (true) {
            boolean flag = true;
            for (Thread thread : threads) {
                if (thread.isAlive()) {
                    flag = false;
                    break;
                }
            }
            if (flag) {
                return Collections.emptyList();
            }
            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
                throw new ThreadPoolException(e);
            }
        }
    }

    @Override
    public <T> List<Future<T>> invokeAll(Collection<? extends Callable<T>> tasks, long timeout, TimeUnit unit) throws InterruptedException {
        throw new NotImplementedException();
    }

    @Override
    public <T> T invokeAny(Collection<? extends Callable<T>> tasks) throws InterruptedException, ExecutionException {
        throw new NotImplementedException();
    }

    @Override
    public <T> T invokeAny(Collection<? extends Callable<T>> tasks, long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
        throw new NotImplementedException();
    }

    @Override
    public void execute(Runnable command) {
        throw new NotImplementedException();
    }

    private final static class RunnableAdapter {
        static Callable<Void> callable(final Runnable runnable) {
            return () -> {
                try {
                    runnable.run();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
                return null;
            };
        }
    }
}
