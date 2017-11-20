package crawler.threadpool;

import java.util.Collection;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class CustomThreadPool implements ThreadPool {

    private final AtomicBoolean isShutdown = new AtomicBoolean(false);
    private final Collection<SimpleThread> threads;
    private final BlockingQueue<Callable<?>> callables;

    private static class ThreadPoolException extends RuntimeException {
        ThreadPoolException(Throwable cause) {
            super(cause);
        }
    }

    private static class SimpleThread extends Thread {
        private final BlockingQueue<Callable<?>> callables;
        private final long timeout;
        private final TimeUnit unit;

        SimpleThread(BlockingQueue<Callable<?>> callables, long timeout, TimeUnit unit) {
            super();
            this.callables = callables;
            this.timeout = timeout;
            this.unit = unit;
        }

        @Override
        public void run() {
            try {
                Callable<?> callable;
                while ((callable = callables.poll(timeout, unit)) != null) {
                    call(callable);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        private void call(Callable<?> callable) {
            try {
                callable.call();
            } catch (Exception e) {
                throw new ThreadPoolException(e);
            }
        }
    }

    public CustomThreadPool(int nThreads) {
        callables = new LinkedBlockingQueue<>();
        threads = Stream.generate(() -> new SimpleThread(callables, 250, TimeUnit.MILLISECONDS))
                .limit(nThreads).collect(Collectors.toList());
        threads.forEach(Thread::start);
    }

    @Override
    public void shutdown() {
        isShutdown.set(true);
    }

    @Override
    public void submit(Runnable task) {
        callables.add(RunnableAdapter.callable(task));
    }

    @Override
    public <T> void invokeAll(Collection<? extends Callable<T>> tasks) throws InterruptedException {
        tasks.forEach(this::submit);
    }

    @Override
    public <T> void invokeAllAndAwait(Collection<? extends Callable<T>> tasks) throws InterruptedException {
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
                return;
            }
            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
                throw new ThreadPoolException(e);
            }
        }
    }

    private <T> Future<T> submit(Callable<T> task) {
        callables.add(task);
        return CompletableFuture.completedFuture(null);
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
