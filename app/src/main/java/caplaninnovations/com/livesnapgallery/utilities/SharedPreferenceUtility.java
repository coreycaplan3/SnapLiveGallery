package caplaninnovations.com.livesnapgallery.utilities;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;

import caplaninnovations.com.livesnapgallery.activities.BaseActivity;

/**
 * Purpose of Class: To easily save simple things to shared preferences
 */
public class SharedPreferenceUtility {

    private static final String KEY_TASK_DURATION = "TASK_DURATION";

    public static void saveSlideShowTaskDuration(long durationInMillis) {
        BaseActivity.getGlobalContext()
                .getSharedPreferences(KEY_TASK_DURATION, Context.MODE_PRIVATE)
                .edit()
                .putLong(KEY_TASK_DURATION, durationInMillis)
                .apply();
    }

    public static long getSlideShowTaskDuration() {
        return BaseActivity.getGlobalContext()
                .getSharedPreferences(KEY_TASK_DURATION, Context.MODE_PRIVATE)
                .getLong(KEY_TASK_DURATION, 5000L);
    }

}

