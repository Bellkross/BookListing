package ua.bellkross.android.booklisting;


import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;

public class QueryUtils {

    private static final String LOG_TAG = "QueryUtils";

    private QueryUtils() {
    }

    public static ArrayList<Book> fetchBooksData(String requestUrl){
        URL url = stringToUrl(requestUrl);

        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Error closing input stream", e);
        }

        ArrayList<Book> books = fromJsonToBooks(jsonResponse);

        return books;
    }

    public static ArrayList<Book> fetchBooksData(URL requestUrl){
        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(requestUrl);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Error closing input stream", e);
        }

        ArrayList<Book> books = fromJsonToBooks(jsonResponse);

        return books;
    }

    private static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";
        if (url == null) {
            return jsonResponse;
        }

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000 /* milliseconds */);
            urlConnection.setConnectTimeout(15000 /* milliseconds */);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            if (urlConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = streamToString(inputStream);
            } else {
                Log.e(LOG_TAG, "Error response code: " + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem retrieving the earthquake JSON results.", e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    public static ArrayList<Book> fromJsonToBooks(String booksJson){
        if (booksJson.isEmpty()) {
            return null;
        }

        ArrayList<Book> books = new ArrayList<>();

        try {
            JSONObject jsonResponse = new JSONObject(booksJson);
            JSONArray items = jsonResponse.getJSONArray("items");
            JSONObject book;
            JSONObject volumeInfo;
            JSONArray jsonAuthors;
            String author;

            int pageCount;
            long publishedDate;
            String title;
            String url;
            String authors = "";

            for (int i = 0; i < items.length(); ++i) {
                book = items.getJSONObject(i);
                volumeInfo = (JSONObject) book.get("volumeInfo");

                pageCount = volumeInfo.getInt("pageCount");
                publishedDate = volumeInfo.getLong("publishedDate");
                title = volumeInfo.getString("title");
                jsonAuthors = volumeInfo.getJSONArray("authors");
                for (int j = 0; j < jsonAuthors.length(); ) {
                    author = (String) jsonAuthors.get(i);
                    authors += (++j) < jsonAuthors.length() ? author + ',' : author;
                }
                url = volumeInfo.getString("canonicalVolumeLink");

                books.add(new Book(title, authors, publishedDate, pageCount, url));
            }

        } catch (JSONException e) {
            Log.e(LOG_TAG, "Problem parsing the earthquake JSON results", e);
        }

        return books;
    }

    private static String streamToString(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }

    private static URL stringToUrl(String stringUrl) {
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Error with creating URL ", e);
        }
        return url;
    }

}
