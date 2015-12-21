package com.asseco.sek.nik.assecozadatak;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Patterns;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import com.asseco.sek.nik.assecozadatak.dao.Dao;
import com.asseco.sek.nik.assecozadatak.dao.DaoException;
import com.asseco.sek.nik.assecozadatak.dao.IDao;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Main activity that is launched when application is started.
 * Activity had form for URL input, and button. When button is clicked,
 * Activity fetches content from URL if it is not fetchedd before,
 * calculates SHA-256 hash, and stores it to
 * storage. If first byte of hash id even, hask is stored to database,
 * otherwise it is stored to Shared Preferences. Url, hask and storage are diplayed
 * on ResultActiviy after button is pressed. If content was fetched before, button is disabled for
 * 5 seconds.
 */
public class MainActivity extends AppCompatActivity {

    // Form for URL
    EditText urlInput;
    //Button
    Button button;

    //Progress Dialog that is shown while application waits contetn to be fetched.
    ProgressDialog progress;

    //Connectivity manager for openinig http connections
    ConnectivityManager connMgr;
    //Network Info
    NetworkInfo networkInfo;

    //Class that sends HTTP request in new thread.
    SendRequest sender;

    //DAO
    IDao dao;

    //Values that are shown as result after button is pressed/
    String urlValue;
    String hashValue;
    String storeValue;

    //Timer that enables button 5 seconds after it is disabled.
    Timer timer;
    // Handler that threadsafe enables button.
    Handler handler;


    /**
     * Starts activity. Initiliyes views and onClick hanldler for button.
     *
     * @param savedInstanceState bundle
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        urlInput = (EditText) findViewById(R.id.urlInput);
        button = (Button) findViewById(R.id.button);
        connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        networkInfo = connMgr.getActiveNetworkInfo();

        sender = new SendRequest(connMgr, networkInfo, this);

        dao = new Dao(this);

        timer = new Timer();
        handler = new Handler();

        progress = new ProgressDialog(this);

        progress.setTitle(getString(R.string.connecting));
        progress.setMessage(getString(R.string.wait));

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                InputMethodManager inputManager = (InputMethodManager)
                        getSystemService(Context.INPUT_METHOD_SERVICE);

                //shuts down keyboard after button is pressed
                inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                        InputMethodManager.HIDE_NOT_ALWAYS);

                try {
                    MainActivity.this.buttonClicked();
                } catch (Exception e) {
                    showError(getString(R.string.unknown_error)
                            + ": " + e.getMessage());

                    throw e;
                }

            }
        });
    }

    /**
     * Destroys activity. Closes DAO/
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        dao.close();
    }

    /**
     * Method that is called when button is clicked. If content is already fetched,
     * reads hash from storage and disables button for 5 seconds. If it is not, fetches it from internet and stores it.
     */
    protected void buttonClicked() {

        String url = formatUrl(urlInput.getText().toString());

        if (!checkUrl(url)) {
            showError(getString(R.string.invalid_url));
            return;
        }
        String hash = null;

        try {
            hash = dao.getHash(url);
        } catch (DaoException de) {
            showError(de.getMessage());
            return;
        }

        if (hash == null) {
            if (networkInfo != null && networkInfo.isConnected()) {

                progress.show();

                //sends http request
                sender = new SendRequest(connMgr, networkInfo, this);
                urlValue = url;
                sender.execute(url);
            } else {
                showError(getString(R.string.no_network));
            }
        } else {

            urlValue = url;
            hashValue = hash;
            storeValue = (Utils.firstDigitEven(hash, this) ? getString(R.string.dbStore) : getString(R.string.prefStore));

            disableButton();
            showResult();

        }
    }


    /**
     * Method that formats url. Removes spaces from begin and end of string.
     * If url ends with '/', removes it from end. If protocol is not specified
     * in url, adds 'http://' to begin of url.
     *
     * @param oldUrl old url
     * @return formated url.
     */
    String formatUrl(String oldUrl) {
        if (oldUrl == null) {
            return "";
        }
        String url = oldUrl.trim();

        if (url.endsWith("/")) {
            url = url.substring(0, url.length() - 1);
        }

        String lowerCase = url.toLowerCase();

        if (!lowerCase.startsWith("http://")
                && !lowerCase.startsWith("https://")) {
            url = "http://" + url;
        }

        return url;
    }

    /**
     * Shows error on alert dialog.
     *
     * @param error error description.
     */
    private void showError(String error) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

        alertDialogBuilder.setTitle(getString(R.string.error));
        alertDialogBuilder
                .setMessage(error)
                .setPositiveButton(getString(R.string.ok), null);

        AlertDialog alertDialog = alertDialogBuilder.create();

        alertDialog.show();
    }


    /**
     * Checks if URL is in valid form. Zou should format url
     * with formatUrl() method before used it in this method.
     *
     * @param url URL to check
     * @return true if and only if url is in valid form.
     */
    private boolean checkUrl(String url) {
        if (url == null || url.trim().isEmpty()) {
            return false;
        }


        if (!Patterns.WEB_URL.matcher(url).matches()) {
            return false;
        }
        return true;
    }

    /**
     * Method that is called when download is completed.
     * Disables progress dialog, stores hash to storage,
     * and shows resutl in ResultActivity.
     */
    public void downloadCompleted() {

        progress.dismiss();

        if (sender.hasError()) {
            showError(sender.getErrorMessage());
        } else {
            hashValue = sender.getHash();
            storeValue = (Utils.firstDigitEven(hashValue, this) ?
                    getString(R.string.dbStore) :
                    getString(R.string.prefStore));


            try {
                dao.storeHash(urlValue, hashValue);
            } catch (DaoException de) {
                showError(getString(R.string.dao_exception) + ": " + de.getMessage());
            }

            showResult();
        }

    }


    /**
     * Method that shows result after button is clicked.
     * Stars ResultActivity with intetn filled with url, hash and storage type.
     */
    public void showResult() {

        Intent intent = new Intent(this, ResultActivity.class);

        intent.putExtra("url", urlValue);
        intent.putExtra("hash", hashValue);
        intent.putExtra("storage", storeValue);

        startActivity(intent);
    }

    /**
     * Runnable object that eanbles button.
     */
    final Runnable buttonEnabler = new Runnable() {
        @Override
        public void run() {
            button.setEnabled(true);
        }
    };

    /**
     * Disables button. Start timer taht enables button again after 5 seconds.
     */
    private void disableButton() {
        button.setEnabled(false);

        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                enableButton();
            }
        }, Utils.TIMER_PERIOD, Utils.TIMER_PERIOD);

    }


    /**
     * Enables button with handler. Thread safe mode.
     */
    private void enableButton() {

        timer.cancel();
        handler.post(buttonEnabler);
    }


}
