package com.mjacksi.rojprints.SimpleAlbum;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.mjacksi.rojprints.R;
import com.mjacksi.rojprints.RealmObjects.ImageRealm;
import com.mjacksi.rojprints.RealmObjects.Project;

import org.apache.http.util.EncodingUtils;

import io.realm.Realm;
import io.realm.RealmList;

public class PreviewActivity extends AppCompatActivity {


    private static final String TAG = PreviewActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preview);
        toolbarSetup("Preview");


        Realm realm = Realm.getDefaultInstance();
        String id = getIntent().getExtras().getString("album_id");
        Project project = realm.where(Project.class).equalTo("id", id).findFirst();
        sendPost(project);
    }

    private void toolbarSetup(String title) {
        Toolbar toolbar = findViewById(R.id.order_toolbar);
        toolbar.setTitle(title);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void sendPost(Project project) {

        WebView webView = findViewById(R.id.web_view);
        webView.getSettings().setLoadsImagesAutomatically(true);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        webView.setVisibility(View.VISIBLE);
        RealmList<ImageRealm> images = project.getImages();

        String postData = "covercover=" + images.first().getUrl()
                + "&npages=" + images.size() + "&isrec=";
        if (project.isSquare()) {
            postData += "1";
        } else {
            postData += "1.33";

        }
        for (int i = 1; i < images.size(); i++) {
            int ii = i - 1;
            postData += "&page" + ii + "=" + images.get(i).getUrl();
        }
        webView.postUrl("http://rojprint.com/admin/preview/2018/preview/turnJs/samples/myalbums/index.php", postData.getBytes());

        Log.d(TAG, "sendPost: " + postData);
    }
}
