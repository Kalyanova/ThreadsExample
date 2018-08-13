package utils;

import android.content.Context;
import android.util.Log;

import androidx.loader.content.AsyncTaskLoader;

public class CounterLoader extends AsyncTaskLoader<Integer> {
    private final static String LOG_TAG = "CounterLoader";
    private final static int MAX_COUNT = 10, TIMEOUT = 500;
    private int counter = 0;

    public CounterLoader(Context context) {
        super(context);
    }

    @Override
    protected void onStartLoading() {
        super.onStartLoading();
        if (counter != 0) {
            deliverResult(counter);
        } else if (isStarted()) {
            forceLoad();
        }
    }

    @Override
    public Integer loadInBackground() {
        counter++;
        while (counter <= MAX_COUNT) {
            if (!isLoadInBackgroundCanceled()) {
                Log.e(LOG_TAG, "Counter: " + counter);
                counter++;
                try {
                    Thread.sleep(TIMEOUT);
                } catch (Exception ex) {
                    Log.e(LOG_TAG, "Exception: ", ex);
                }
            }
        }
        return counter;
    }

    @Override
    public void deliverResult(Integer data) {
        counter = data;
        super.deliverResult(data);
    }

    @Override
    protected void onStopLoading() {
        super.onStopLoading();
    }
}
