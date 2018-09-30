package com.example.android.quakereport;

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
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Helper methods related to requesting and receiving earthquake data from USGS.
 */
public final class QueryUtils {

    /** Tag for the log messages */
    private static final String LOG_TAG = QueryUtils.class.getSimpleName();

    /**
     * Create a private constructor because no one should ever create a {@link QueryUtils} object.
     * This class is only meant to hold static variables and methods, which can be accessed
     * directly from the class name QueryUtils (and an object instance of QueryUtils is not needed).
     */
    private QueryUtils() {
    }

    /**
     * Query the USGS dataset and return a list of {@link PlaceHolder} objects.
     */
    public static List<PlaceHolder> fetchEarthquakeData(String requestUrl) {
        // Create URL object
        URL url = createUrl(requestUrl);
        // Perform HTTP request to the URL and receive a JSON response back
        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem making the HTTP request.", e);
        }
        // Extract relevant fields from the JSON response and create a list of {@link Earthquake}s
        List<PlaceHolder> earthquakes = extractFeatureFromJson(jsonResponse);
        // Return the list of {@link Earthquake}s
        return earthquakes;
    }
    /**
     * Returns new URL object from the given string URL.
     */
    private static URL createUrl(String stringUrl) {
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Problem building the URL ", e);
        }
        return url;
    }
    /**
     * Make an HTTP request to the given URL and return a String as the response.
     */
    private static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";
        // If the URL is null, then return early.
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
            // If the request was successful (response code 200),
            // then read the input stream and parse the response.
            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
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
                // Closing the input stream could throw an IOException, which is why
                // the makeHttpRequest(URL url) method signature specifies than an IOException
                // could be thrown.
                inputStream.close();
            }
        }
        return jsonResponse;
    }
    /**
     * Convert the {@link InputStream} into a String which contains the
     * whole JSON response from the server.
     */
    private static String readFromStream(InputStream inputStream) throws IOException {
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


    /**
             * Return a list of {@link PlaceHolder} objects that has been built up from
             * parsing the given JSON response.
             */
    private static List<PlaceHolder> extractFeatureFromJson(String earthquakeJSON) {
        // If the JSON string is empty or null, then return early.
        if (TextUtils.isEmpty(earthquakeJSON)) {
            return null;
        }

        String locationOffset;
        String primLocation;
        int color = 0;

        // Create an empty ArrayList that we can start adding earthquakes to
        List<PlaceHolder> earthquakes = new ArrayList<>();

        // Try to parse the JSON response string. If there's a problem with the way the JSON. If there's a problem with the way the JSON
        // is formatted, a JSONException exception object will be thrown.
        // Catch the exception so the app doesn't crash, and print the error message to the logs.
        try {
            JSONObject jsonObject = new JSONObject(earthquakeJSON);
            JSONArray jsonArray = jsonObject.optJSONArray("features");
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject feature = jsonArray.getJSONObject(i);
                JSONObject properties = feature.getJSONObject("properties");

                Double mag = properties.getDouble("mag");

                String url=properties.getString("url");

                String place = properties.getString("place");
                if (place.contains("of")) {
                    String[] separated = place.split("of");
                    locationOffset = separated[0];
                    primLocation = separated[1].trim();
                } else {
                    locationOffset = "Near of";
                    primLocation = place;
                }


                long timeInMilliseconds = properties.getLong("time");

                Date dateObject = new Date(timeInMilliseconds);
                SimpleDateFormat dateFormatter = new SimpleDateFormat("MMM dd, yyyy" + "\nhh:mm");
                String dateToDisplay = dateFormatter.format(dateObject);


                NumberFormat numberFormat = new DecimalFormat("##.#");
                String magni=numberFormat.format(mag);

                String timee = String.valueOf(dateToDisplay);

                if (mag < 2.0){
                    color = R.color.magnitude1;
                } else if (mag < 3.0 & mag >= 2.0) {
                    color = R.color.magnitude2;
                }else if (mag < 4.0 & mag >= 3.0) {
                    color = R.color.magnitude3;
                }else if (mag < 5.0 & mag >= 4.0) {
                    color = R.color.magnitude4;
                }else if (mag < 6.0 & mag >= 5.0) {
                    color = R.color.magnitude5;
                }else if (mag < 7.0 & mag >= 6.0) {
                    color = R.color.magnitude6;
                }else if (mag < 8.0 & mag >= 7.0) {
                    color = R.color.magnitude7;
                }else if (mag < 9.0 & mag >= 8.0) {
                    color = R.color.magnitude8;
                }else if (mag < 10.0 & mag >= 9.0) {
                    color = R.color.magnitude9;
                }else if (mag >=10) {
                    color = R.color.magnitude10plus;
                }



                earthquakes.add(new PlaceHolder(locationOffset, primLocation, mag, timee,color,url));
            }

            // TODO: Parse the response given by the SAMPLE_JSON_RESPONSE string and
            // build up a list of Earthquake objects with the corresponding data.

        } catch (JSONException e) {
            // If an error is thrown when executing any of the above statements in the "try" block,
            // catch the exception here, so the app doesn't crash. Print a log message
            // with the message from the exception.
            Log.e("QueryUtils", "Problem parsing the earthquake JSON results", e);
        }

        // Return the list of earthquakes
        return earthquakes;
    }

}