package by.paranoidandroid.threadsexample.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.Loader;
import by.paranoidandroid.threadsexample.R;
import utils.CounterLoader;

/**
 * Activity that demonstrates work with Loader.
 */

public class LoaderActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Integer> {
    final int COUNTER_LOADER_ID = 0, MAX_COUNT = 10;
    Button startBtn, cancelBtn;
    TextView textView;
    View.OnClickListener btnListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loader);

        startBtn = findViewById(R.id.btn_start);
        cancelBtn = findViewById(R.id.btn_cancel);
        textView = findViewById(R.id.text_view_counter);

        if (savedInstanceState != null) {
            if (LoaderManager.getInstance(LoaderActivity.this).getLoader(COUNTER_LOADER_ID) != null) {
                // Prepare the loader.  Either re-connect with an existing one, or start a new one.
                LoaderManager.getInstance(LoaderActivity.this)
                        .initLoader(COUNTER_LOADER_ID, null, LoaderActivity.this);
            }
        }

        btnListener = view -> {
            switch (view.getId()) {
                case R.id.btn_start:
                    // Prepare the loader.  Either re-connect with an existing one, or start a new one.
                    LoaderManager.getInstance(LoaderActivity.this)
                            .initLoader(COUNTER_LOADER_ID, null, LoaderActivity.this);
                    break;
                case R.id.btn_cancel:
                    LoaderManager.getInstance(LoaderActivity.this)
                            .destroyLoader(COUNTER_LOADER_ID);
                    break;
            }
        };

        startBtn.setOnClickListener(btnListener);
        cancelBtn.setOnClickListener(btnListener);
    }


    @Override
    public Loader<Integer> onCreateLoader(int id, Bundle args) {
        textView.setText("");
        return new CounterLoader(this);
    }

    @Override
    public void onLoadFinished(Loader<Integer> loader, Integer data) {
        if (data > MAX_COUNT) {
            textView.setText(getString(R.string.done));
        }
    }

    @Override
    public void onLoaderReset(Loader<Integer> loader) {}
}
