package utils;

import android.os.Handler;
import android.os.Looper;

/**
 * Own implementation of AsyncTask with java threads
 * and communication to the UI thread's’ Handler.
 */

public class SimpleAsyncTaskImpl<Params, Progress, Result> extends SimpleAsyncTask<Params, Progress, Result> {
    private Handler handler;
    private Params[] params;

    protected SimpleAsyncTaskImpl() {
        status = Status.PENDING;
        handler = new Handler(Looper.getMainLooper());
    }

    @Override
    protected void onPreExecute() {}

    @Override
    protected Result doInBackground(Params[] params) {
        return null;
    }

    @Override
    protected void onPostExecute(Result o) {}

    @Override
    public void execute() {
        status = Status.RUNNING;

        handler.post(this::onPreExecute);

        new Thread(() -> {
            if (isCancelled) {
                handler.post(this::onCancelled);
            } else {
                Result result = doInBackground(params);
                handler.post(() -> {
                    if (isCancelled) {
                        onCancelled();
                    } else {
                        onPostExecute(result);
                    }
                });
            }
            status = Status.FINISHED;
        }).start();
    }

    public void execute(Params... values) {
        params = values;
        execute();
    }

    @Override
    protected void onProgressUpdate(Progress... values) { }

    /**
     * Invoking this method will cause subsequent calls to {@link #isCancelled()} to return true.
     * After invoking this method, {@link #onCancelled()}, instead of
     * {@link #onPostExecute(Object)} will be invoked after {@link #doInBackground(Object[])}
     * returns.
     * IMPORTANT TO NOTE:
     * <b>To ensure that a task is cancelled as quickly as possible, you should always
     * check the return value of {@link #isCancelled()} periodically from
     * {@link #doInBackground(Object[])}, if possible (inside a loop for instance.)</b>
     *
     */
    @Override
    public void cancel() {
        isCancelled = true;
    }

    protected void onCancelled() {}
}
