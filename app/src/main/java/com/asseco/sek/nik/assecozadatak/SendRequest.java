package com.asseco.sek.nik.assecozadatak;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.MessageDigest;

import static com.asseco.sek.nik.assecozadatak.Utils.*;

/**
 * AsyncTask that fetches content from the web, and calulates sha-256 hash of that content.
 * Input for task i String with URL, and output is array of bytes (sha=256).
 * <p/>
 * Created by sekul on 21.12.2015..
 */
public class SendRequest extends AsyncTask<String, Void, byte[]> {

    private int status = 0;
    private boolean error = false;
    private String errorMessage;

    private MainActivity activity;

    ConnectivityManager connMgr;
    NetworkInfo networkInfo;

    MessageDigest digester;

    String hash;

    /**
     * Constructor.
     *
     * @param connMgr     connection manager
     * @param networkInfo network info
     * @param activity    actitity that uses this AsyncTask
     */
    public SendRequest(ConnectivityManager connMgr, NetworkInfo networkInfo, MainActivity activity) {
        this.connMgr = connMgr;
        this.networkInfo = networkInfo;
        this.activity = activity;

        try {
            digester = MessageDigest.getInstance(HASH_ALGORITHM);
        } catch (Exception NoSuchAlgorithmException) {
        }
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        status = 0;
        error = false;
        hash = "";
        digester.reset();
    }

    @Override
    protected byte[] doInBackground(String... params) {
        return downloadUrl(params[0]);
    }

    @Override
    protected void onPostExecute(byte[] hash) {
        super.onPostExecute(hash);

        this.hash = byteArrayToHexString(hash);
        activity.downloadCompleted();
    }


    /**
     * Donwnoads content from internet and calulates hash of that content.
     *
     * @param myurl url for web content
     * @return sha256 hash of content
     */
    private byte[] downloadUrl(String myurl) {
        InputStream is = null;

        try {
            URL url = new URL(myurl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(10000 /* milliseconds */);
            conn.setConnectTimeout(15000 /* milliseconds */);
            conn.setRequestMethod("GET");
            conn.setDoInput(true);
            // Starts the query
            conn.connect();

            int response = conn.getResponseCode();
            is = conn.getInputStream();

            byte[] buffer = new byte[BUFFER_SIZE];

            while (true) {
                int count = is.read(buffer);

                if (count == -1) break;

                digester.update(buffer, 0, count);
            }

        } catch (MalformedURLException e1) {
            error = true;
            errorMessage = activity.getString(R.string.invalid_url);
        } catch (IOException e2) {
            error = true;
            errorMessage = activity.getString(R.string.invalid_url);
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException ignorable) {
                }
            }
        }

        return digester.digest();
    }


    /**
     * Convertsbyte array to String in hexadecimal notation.
     *
     * @param array array of bytes.
     * @return dex representation of bytes.
     */
    private static String byteArrayToHexString(byte[] array) {
        StringBuffer hexString = new StringBuffer();
        for (byte b : array) {
            int intVal = b & 0xff;
            if (intVal < 0x10)
                hexString.append("0");
            hexString.append(Integer.toHexString(intVal));
        }
        return hexString.toString();
    }


    /**
     * Returns calulated hash.
     *
     * @return hash
     */
    public String getHash() {
        return hash;
    }


    /**
     * Checks if error occured on task execution.
     *
     * @return true if and only ir error occured on task execution.
     */
    public boolean hasError() {
        return error;
    }


    /**
     * Returns HTTP response status.
     *
     * @return HTTP response status.
     */
    public int getResponseStatus() {
        return status;
    }

    /**
     * Returns error description after task execution.
     *
     * @return error description. null if error did not occured.
     */
    public String getErrorMessage() {
        return errorMessage;
    }
}
