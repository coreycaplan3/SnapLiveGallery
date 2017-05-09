package caplaninnovations.com.livesnapgallery.networking;

import android.view.View;

import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;

import org.json.JSONArray;
import org.json.JSONException;

import caplaninnovations.com.livesnapgallery.R;
import caplaninnovations.com.livesnapgallery.activities.BaseActivity;
import caplaninnovations.com.livesnapgallery.models.Snap;
import caplaninnovations.com.livesnapgallery.utilities.JsonExtractor;
import caplaninnovations.com.livesnapgallery.utilities.UiUtility;
import io.realm.Realm;

/**
 * Created by Corey Caplan on 5/8/17.
 * Project: SnapLiveGallery
 * <p></p>
 * Purpose of Class:
 */
public class FeedNetwork {

    private static final String TAG = FeedNetwork.class.getSimpleName();

    private static final String BASE_URL = "http://f584a53b.ngrok.io";

    public FeedNetwork() {
    }

    public void getPhotosFromNetwork(Listener<JSONArray> listener, ErrorListener errorListener) {
        JsonArrayRequest request = new JsonArrayRequest(BASE_URL + "/get-snaps", listener, errorListener);
        BaseActivity.getRequestQueue().add(request);
    }

    public void free() {
        BaseActivity.getRequestQueue().cancelAll(TAG);
    }

}
