package by.paranoidandroid.threadsexample.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import by.paranoidandroid.threadsexample.R;

/**
 * Main activity with three buttons.
 */

public class MainActivity extends AppCompatActivity {
    Button asynctaskBtn, loaderBtn, threadsBtn;
    View.OnClickListener btnListener;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        asynctaskBtn = findViewById(R.id.btn_asynctask);
        loaderBtn = findViewById(R.id.btn_loader);
        threadsBtn = findViewById(R.id.btn_threads);

        btnListener = new View.OnClickListener() {
            Intent intent;
            @Override
            public void onClick(View view) {
                switch (view.getId()) {
                    case R.id.btn_asynctask:
                        intent = new Intent(MainActivity.this, AsyncTaskActivity.class);
                        break;
                    case R.id.btn_loader:
                        intent = new Intent(MainActivity.this, LoaderActivity.class);
                        break;
                    case R.id.btn_threads:
                        intent = new Intent(MainActivity.this, ThreadsActivity.class);
                        break;
                }
                startActivity(intent);
            }
        };

        asynctaskBtn.setOnClickListener(btnListener);
        loaderBtn.setOnClickListener(btnListener);
        threadsBtn.setOnClickListener(btnListener);
    }

}
