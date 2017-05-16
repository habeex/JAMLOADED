package com.jamloaded.jamloaded;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.io.File;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener{
    private WebView myWebView;
    private ProgressBar progressBar;
    private Handler handler = new Handler();
    final Activity activity = this;


    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        final DownloadManager manager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);

        final File destinationDir = new File(Environment.getExternalStorageDirectory(), getPackageName());
        if (!destinationDir.exists()) {
            destinationDir.mkdir(); // Don't forget to make the directory if it's not there
        }


        myWebView = (WebView) findViewById(R.id.webView);
        myWebView.getSettings().setJavaScriptEnabled(true);
        myWebView.getSettings().setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        myWebView.getSettings().setLoadsImagesAutomatically(true);
        myWebView.getSettings().setBuiltInZoomControls(true);
        myWebView.getSettings().setDomStorageEnabled(true);

        myWebView.setWebChromeClient(new WebChromeClient() {

            public void onProgressChanged(WebView view, int progress)
            {
                activity.setTitle("Loading...");
                activity.setProgress(progress * 100);

                if(progress == 100)
                    activity.setTitle(R.string.app_name);
            }
        });

        myWebView.setWebViewClient(new WebViewClient() {
            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl)
            {
                // Handle the error
                Log.d("WEB_VIEW_TEST", "error code:" + errorCode + " - " + description);
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url)
            {
                // handle different requests for different type of files
                // this example handles downloads requests for .apk and .mp3 files
                // everything else the webview can handle normally
                if (url.endsWith(".apk")) {
                    Uri source = Uri.parse(url);
                    // Make a new request pointing to the .apk url
                    DownloadManager.Request request = new DownloadManager.Request(source);
                    // appears the same in Notification bar while downloading
                    request.setDescription("Description for the DownloadManager Bar");
                    request.setTitle("jamloaded.apk");
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                        request.allowScanningByMediaScanner();
                        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                    }
                    // save the file in the "Downloads" folder of SDCARD
                    request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "SmartPigs.apk");
                    // get download service and enqueue file
                    DownloadManager manager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
                    manager.enqueue(request);
                }
                else if(url.endsWith(".mp3")) {
                    // if the link points to an .mp3 resource do something else
                    Uri source = Uri.parse(url);
                    // Make a new request pointing to the .apk url
                    DownloadManager.Request request = new DownloadManager.Request(source);
                    // appears the same in Notification bar while downloading
                    request.setDescription("Description for the DownloadManager Bar");
                    request.setTitle("jamloaded.com.mp3");
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                        request.allowScanningByMediaScanner();
                        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                    }
                    // save the file in the "Downloads" folder of SDCARD
                    request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "SmartPigs.mp3");
                    // get download service and enqueue file
                    DownloadManager manager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
                    manager.enqueue(request);
                }
                else if(url.endsWith(".mp4")) {
                    // if the link points to an .mp3 resource do something else
                    Uri source = Uri.parse(url);
                    DownloadManager.Request request = new DownloadManager.Request(source);
                    // appears the same in Notification bar while downloading
                    request.setDescription("Description for the DownloadManager Bar");
                    request.setTitle("jamloaded.com.mp4");
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                        request.allowScanningByMediaScanner();
                        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                    }
                    // save the file in the "Downloads" folder of SDCARD
                    request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "SmartPigs.mp4");
                    // get download service and enqueue file
                    DownloadManager manager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
                    manager.enqueue(request);
                }
                // if there is a link to anything else than .apk or .mp3 load the URL in the webview
                else view.loadUrl(url);
                return true;
            }
        });

        myWebView.loadUrl("http://jamloaded.com");
    }



    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START) || myWebView.canGoBack()) {
            drawer.closeDrawer(GravityCompat.START);
            myWebView.goBack();
        } else {
            super.onBackPressed();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
           return true;
        }
        else if (id == R.id.about_app) {
            BlankFragment blankFragment = new BlankFragment();
            FragmentManager manager = getSupportFragmentManager();
            manager.beginTransaction().replace(R.id.
                    Linear_main,
                    blankFragment,
                    blankFragment.getTag()
            ).commit();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.home) {
            myWebView.loadUrl("http://www.jamloaded.com");
        } else if (id == R.id.music) {
            myWebView.loadUrl("http://jamloaded.com/category/music/");
        } else if (id == R.id.video) {
            myWebView.loadUrl("http://jamloaded.com/category/video/");
        } else if (id == R.id.news) {
            myWebView.loadUrl("http://jamloaded.com/category/news/");
        } else if (id == R.id.entertament) {
            myWebView.loadUrl("http://jamloaded.com/category/entertainment/");
        } else if (id == R.id.sports) {
            myWebView.loadUrl("http://jamloaded.com/category/sport/");
        } else if (id == R.id.photo) {
            myWebView.loadUrl("http://jamloaded.com/category/photos/");
        } else if (id == R.id.nav_share) {
            Intent intent = new Intent (Intent.ACTION_VIEW);
            intent.setType("text/plain");
            intent.putExtra(Intent.EXTRA_SUBJECT, "JAMLOADED");
            intent.putExtra(Intent.EXTRA_TEXT, "Download Jamloaded App");
            startActivity(intent);
        } else if (id == R.id.contact) {
            myWebView.loadUrl("http://jamloaded.com/contact/");
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

}

