package ro.teamnet.bootstrap.async;

import java.util.concurrent.Callable;
import java.util.concurrent.Future;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.core.task.AsyncTaskExecutor;

/**
 * Asynchronous task executor for exception handling.
 */
public class ExceptionHandlingAsyncTaskExecutor implements AsyncTaskExecutor,
        InitializingBean, DisposableBean {

    private final Logger log = LoggerFactory.getLogger(ExceptionHandlingAsyncTaskExecutor.class);

    private final AsyncTaskExecutor executor;

    /**
     * Constructor with executor parameter.
     * @param executor - executor used for exception handling
     */
    public ExceptionHandlingAsyncTaskExecutor(AsyncTaskExecutor executor) {
        this.executor = executor;
    }

    /**
     * This method will execute a task, using the executor set in constructor.
     * @param task the {@code Runnable} to execute
     */
    @Override
    public void execute(Runnable task) {
        executor.execute(task);
    }

    /**
     * This method will execute a task, using the executor set in constructor.
     * @param task the {@code Runnable} to execute
     * @param startTimeout the time duration (milliseconds) within which the task is
     * supposed to start. This is intended as a hint to the executor, allowing for
     * preferred handling of immediate tasks. Typical values are {@link #TIMEOUT_IMMEDIATE}
     * or {@link #TIMEOUT_INDEFINITE} (the default as used by {@link #execute(Runnable)}).
     */
    @Override
    public void execute(Runnable task, long startTimeout) {
        executor.execute(createWrappedRunnable(task), startTimeout);
    }

    private <T> Callable<T> createCallable(final Callable<T> task) {
        return new Callable<T>() {

            @Override
            public T call() throws Exception {
                try {
                    return task.call();
                } catch (Exception e) {
                    handle(e);
                    throw e;
                }
            }
        };
    }

    private Runnable createWrappedRunnable(final Runnable task) {
        return new Runnable() {

            @Override
            public void run() {
                try {
                    task.run();
                } catch (Exception e) {
                    handle(e);
                }
            }
        };
    }

    protected void handle(Exception e) {
        log.error("Caught async exception", e);
    }

    /**
     * Submit a Runnable task for execution, receiving a Future representing that task.
     * The Future will return a {@code null} result upon completion.
     * @param task the {@code Runnable} to execute (never {@code null})
     * @return a Future representing pending completion of the task
     */
    @Override
    public Future<?> submit(Runnable task) {
        return executor.submit(createWrappedRunnable(task));
    }

    /**
     * Submit a Callable task for execution, receiving a Future representing that task.
     * The Future will return the Callable's result upon completion.
     * @param task the {@code Callable} to execute (never {@code null})
     * @return a Future representing pending completion of the task
     */
    @Override
    public <T> Future<T> submit(Callable<T> task) {
        return executor.submit(createCallable(task));
    }

    /**
     * Invoked by a BeanFactory on destruction of a singleton.
     */
    @Override
    public void destroy() throws Exception {
        if (executor instanceof DisposableBean) {
            DisposableBean bean = (DisposableBean) executor;
            bean.destroy();
        }
    }

    /**
     * Invoked by a BeanFactory after it has set all bean properties supplied
     * (and satisfied BeanFactoryAware and ApplicationContextAware).
     * <p>This method allows the bean instance to perform initialization only
     * possible when all bean properties have been set and to throw an
     * exception in the event of misconfiguration.
     * @throws Exception in the event of misconfiguration (such
     * as failure to set an essential property) or if initialization fails.
     */
    @Override
    public void afterPropertiesSet() throws Exception {
        if (executor instanceof InitializingBean) {
            InitializingBean bean = (InitializingBean) executor;
            bean.afterPropertiesSet();
        }
    }
}
