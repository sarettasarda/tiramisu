package com.saracraba.tiramisu;

import android.os.AsyncTask;
import android.text.TextUtils;
import android.util.Log;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;

/**
 * Created by scraba on 11/26/16.
 */

public class DownloadHtmlPage {
    private final static String TAG = DownloadHtmlPage.class.getName();
    private final static String AGENT = "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/535.21 " +
            "(KHTML, like Gecko) Chrome/19.0.1042.0 Safari/535.21";
    private final static int TIMEOUT = 10000;
    private final static int RESPONSE_SUCCESS = 200;

    private final static int DOWNLOAD_SUCCESS = 100;
    private final static int DOWNLOAD_FAIL = 101;

    private String mUrl;
    private DownloadTaskResponse mResponse;
    private DownloadTask mDownloadTask;


    private DownloadHtmlPage() {
    }

    public DownloadHtmlPage(String url, DownloadTaskResponse response) {
        if (TextUtils.isEmpty(url)) {
            throw new IllegalArgumentException("URL cannot be null or empty");
        }

        mUrl = url;
        mResponse = response;

        if (mResponse != null) {
            mDownloadTask = new DownloadTask(mUrl, mResponse);
            mDownloadTask.execute();
        }
    }

    public interface DownloadTaskResponse {
        void onResult(Document result);
    }

    private final class DownloadTask extends AsyncTask<Void, Void, Integer> {
        private String mUrl;
        private DownloadTaskResponse mResponse;
        private Document mDocument;

        public DownloadTask(String url, DownloadTaskResponse response) {
            mUrl = url;
            mResponse = response;
        }

        @Override
        protected Integer doInBackground(Void... params) {
            try {
                Connection connection = Jsoup.connect(mUrl)
                        .userAgent(AGENT)
                        .timeout(TIMEOUT);

                if (connection.execute().statusCode() == RESPONSE_SUCCESS) {
                    mDocument = connection.get();
                } else {
                    return DOWNLOAD_FAIL;
                }

            } catch (IOException e) {
                e.printStackTrace();
                return DOWNLOAD_FAIL;
            }

            return DOWNLOAD_SUCCESS;
        }

        @Override
        protected void onPostExecute(Integer response) {

            if (response == DOWNLOAD_SUCCESS) {
                mResponse.onResult(mDocument);
                return;
            }

            // TODO manage error case
            Log.e(TAG, "Download failed");
        }
    }
}
