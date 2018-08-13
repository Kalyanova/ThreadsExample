package utils;

/**
 * Interface that helps to communicate between the task (the worker thread)
 * and the Activity (UI thread).
 */

public interface IAsyncTaskEvents {
    void onProgressUpdate(Integer counter);
    void onPostExecute();
}
