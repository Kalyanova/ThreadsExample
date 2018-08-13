package by.paranoidandroid.threadsexample.utils;

import android.os.Looper;

import org.junit.Test;

import utils.SimpleAsyncTaskImpl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Created by rl98880 on 19/11/2017.
 * Edited by Ksenia on 13/08/2018.
 */
public class SimpleAsyncTaskImplTest {
    int counter = 0;

    @Test
    public void execute() throws Exception {
        new SimpleAsyncTaskImpl() {
            @Override
            protected void onPreExecute() {
                assertTrue(isOnUiThread());
                assertEquals(counter++, 0);
            }

            @Override
            protected Object doInBackground(Object[] obj) {
                assertFalse(isOnUiThread());
                assertEquals(counter++, 1);
                return new Object();
            }

            @Override
            protected void onPostExecute(Object obj) {
                assertTrue(isOnUiThread());
                assertEquals(counter++, 2);
            }
        }.execute();
    }

    public boolean isOnUiThread() {
        return Looper.myLooper() == Looper.getMainLooper();
    }
}