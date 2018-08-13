package utils;

/**
 * Abstract class that is interface for own implementation of AsyncTask with java threads
 * and communication to the UI thread's’ Handler.
 */

public abstract class SimpleAsyncTask<Params, Progress, Result> {
    public enum Status {
        FINISHED,
        PENDING,
        RUNNING
    }

    volatile Status status;
    volatile boolean isCancelled = false;

    public final Status getStatus() {
        return status;
    }

    protected abstract void onPreExecute();

    protected abstract Result doInBackground(Params[] params);

    protected abstract void onPostExecute(Result result);

    protected abstract void execute();

    protected final void publishProgress(Progress... values) {
        onProgressUpdate(values);
    }

    protected abstract void onProgressUpdate(Progress... values);

    protected abstract void cancel();

    protected abstract void onCancelled();

    public final boolean isCancelled() {
        return isCancelled;
    }
}
