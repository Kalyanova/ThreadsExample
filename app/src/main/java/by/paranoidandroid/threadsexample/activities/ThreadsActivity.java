package by.paranoidandroid.threadsexample.activities;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import by.paranoidandroid.threadsexample.R;
import utils.IAsyncTaskEvents;
import utils.SimpleAsyncTaskImpl;

import static utils.SimpleAsyncTask.Status.FINISHED;
import static utils.SimpleAsyncTask.Status.PENDING;
import static utils.SimpleAsyncTask.Status.RUNNING;

/**
 * Activity that demonstrates advanced work with threads.
 */

public class ThreadsActivity extends Activity implements IAsyncTaskEvents {
    private final String STATE_COUNTER = "COUNTER",
            STATE_RUNNING = "RUNNING",
            STATE_TEXTVIEW = "TEXTVIEW";
    private Button createBtn, startBtn, cancelBtn;
    private TextView textView;
    private View.OnClickListener btnListener;
    private int counter;
    private boolean isRunning;
    ThreadsActivity.CounterAsyncTask counterAsyncTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.actvity_asynctask);

        createBtn = findViewById(R.id.btn_create);
        startBtn = findViewById(R.id.btn_start);
        cancelBtn = findViewById(R.id.btn_cancel);
        textView = findViewById(R.id.text_view_counter);

        if (savedInstanceState != null) {
            textView.setText(savedInstanceState.getString(STATE_TEXTVIEW, ""));
            isRunning = savedInstanceState.getBoolean(STATE_RUNNING);
            if (isRunning) {
                counter = savedInstanceState.getInt(STATE_COUNTER);
                counterAsyncTask = new ThreadsActivity.CounterAsyncTask(ThreadsActivity.this);
                isRunning = true;
                counterAsyncTask.execute(counter);
            }
        }

        btnListener = view -> {
            switch (view.getId()) {
                case R.id.btn_create:
                    if (counterAsyncTask == null
                            || counterAsyncTask.isCancelled()
                            || counterAsyncTask.getStatus() == FINISHED) {
                        counterAsyncTask = new CounterAsyncTask(ThreadsActivity.this);
                    }
                    break;
                case R.id.btn_start:
                    if (counterAsyncTask != null
                            && counterAsyncTask.getStatus() == PENDING) {
                        isRunning = true;
                        counterAsyncTask.execute();
                    }
                    break;
                case R.id.btn_cancel:
                    if (counterAsyncTask != null) {
                        isRunning = false;
                        counterAsyncTask.cancel();
                        counterAsyncTask = null;
                    }
                    break;
            }
        };

        createBtn.setOnClickListener(btnListener);
        startBtn.setOnClickListener(btnListener);
        cancelBtn.setOnClickListener(btnListener);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        if (counterAsyncTask != null && counterAsyncTask.getStatus() == RUNNING) {
            counterAsyncTask.cancel();
            counterAsyncTask = null;
        }
        outState.putInt(STATE_COUNTER, counter);
        outState.putBoolean(STATE_RUNNING, isRunning);
        outState.putString(STATE_TEXTVIEW, textView.getText().toString());

        super.onSaveInstanceState(outState);
    }

    @Override
    public void onProgressUpdate(Integer counter) {
        this.counter = counter;
        textView.setText(String.valueOf(counter));
    }

    @Override
    public void onPostExecute() {
        textView.setText(getString(R.string.done));
        isRunning = true;
    }

    static class CounterAsyncTask extends SimpleAsyncTaskImpl<Integer, Integer, Void> {
        private final static String LOG_TAG = "Own AsyncTask";
        private final static int MAX_COUNT = 10, TIMEOUT = 500;
        private int counter = 1;
        private IAsyncTaskEvents listener;

        CounterAsyncTask(IAsyncTaskEvents events) {
            listener = events;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            listener.onProgressUpdate(values[0]);
        }

        @Override
        protected Void doInBackground(Integer... values) {
            if (values != null && values[0] != null) {
                counter = values[0];
                counter++;
            }
            while (counter <= MAX_COUNT) {
                if (!isCancelled()) {
                    publishProgress(counter++);
                    try {
                        Thread.sleep(TIMEOUT);
                    } catch (Exception ex) {
                        Log.e(LOG_TAG, "Exception: ", ex);
                    }
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void s) {
            listener.onPostExecute();
        }
    }
}
