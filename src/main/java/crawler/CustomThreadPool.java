package crawler;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class CustomThreadPool implements ExecutorService {

    public CustomThreadPool(int nThreads) {
    }

    @Override
    public void shutdown() {
        throw new NotImplementedException();
    }

    @Override
    public List<Runnable> shutdownNow() {
        throw new NotImplementedException();
    }

    @Override
    public boolean isShutdown() {
        throw new NotImplementedException();
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
        throw new NotImplementedException();
    }

    @Override
    public <T> List<Future<T>> invokeAll(Collection<? extends Callable<T>> tasks) throws InterruptedException {
        throw new NotImplementedException();
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
}
