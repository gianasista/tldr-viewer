package de.gianasista.tldr_viewer.backend;

import android.util.Log;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.TextHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import de.gianasista.tldr_viewer.util.CommandContentDelegate;
import de.gianasista.tldr_viewer.util.MdFileContentParser;
import de.gianasista.tldr_viewer.util.TldrPreferences;

/**
 * Created by vera on 11.04.15.
 */
public class TldrApiClient {

    private static final String TAG = TldrApiClient.class.getName();

    private static final String API_URL = "https://api.github.com/repositories/15019962/contents/pages";
    private static final AsyncHttpClient httpClient = new AsyncHttpClient();

    private static final String RAW_URL = "https://raw.githubusercontent.com/tldr-pages/tldr/master/pages/";
    private static final String LIST_URL = "https://raw.githubusercontent.com/ostera/tldr/feature/index/pages/index.json";

    private static final List<Command> EMPTY_COMMAND_LIST = new ArrayList<Command>(0);

    public static void getPageContent(String commandName, String platform, final CommandContentDelegate delegate) {
        httpClient.get(getContentUrlForCommandName(commandName, platform), new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Log.e(TAG, "Http-Error-Code: "+statusCode, throwable);
                delegate.receiveCommandContent(responseString);
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                delegate.receiveCommandContent(new MdFileContentParser().parseMdFileContent(responseString));
            }
        });
    }

    public static void getPages(final PagesListCallback callback) {
        httpClient.get(LIST_URL, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                callback.receivePages(parseCommandList(response));
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Log.e(TAG, "HttpError: "+statusCode, throwable);
                callback.receivePages(EMPTY_COMMAND_LIST);
            }
        });
    }

    private static List<Command> parseCommandList(JSONObject jsonObject) {
        List<Command> result = new ArrayList<>();

        try {
            JSONArray commandArray = jsonObject.getJSONArray("commands");

            for(int i=0; i<commandArray.length(); i++) {
                JSONObject element = commandArray.getJSONObject(i);
                Command newCommand = new Command(element.getString("name"));
                JSONArray supportedPlatforms = element.getJSONArray("platform");
                for(int j=0; j<supportedPlatforms.length(); j++)
                    newCommand.addPlatform(supportedPlatforms.getString(j));

                result.add(newCommand);
            }
        } catch (JSONException e) {
            Log.e(TAG, e.getMessage(), e);
        }

        return result;
    }

    private static String getContentUrlForCommandName(String commandName, String platform) {
        return "https://raw.githubusercontent.com/tldr-pages/tldr/master/pages/"+ platform+"/"+commandName+".md";
    }

    public interface PagesListCallback {
        void receivePages(List<Command> list);
    }

}
