package me.scolastico.runner.manager.etc;

import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import java.util.ArrayList;
import javax.naming.ConfigurationException;
import me.scolastico.runner.manager.dataholders.RunnerConfiguration;
import me.scolastico.runner.manager.dataholders.RunnerData;
import me.scolastico.tools.handler.ErrorHandler;
import org.json.JSONArray;
import org.json.JSONObject;

public class GithubAPI {

  private static final OkHttpClient client = new OkHttpClient();

  public static RunnerData[] getRunners(RunnerConfiguration config) {
    try {
      Request request = new Request.Builder()
          .header("Authorization", "token " + config.getGhToken())
          .header("Accept", "application/vnd.github.v3+json")
          .url("https://api.github.com/orgs/" + config.getOrg() + "/actions/runners")
          .build();
      Response response = client.newCall(request).execute();
      if (response.code() == 200) {
        ArrayList<RunnerData> data = new ArrayList<>();
        JSONArray array = new JSONObject(response.body().string()).getJSONArray("runners");
        for (int i = 0; i < array.length(); i++) {
          JSONObject object = array.getJSONObject(i);
          ArrayList<String> tags = new ArrayList<>();
          JSONArray tagsArray = object.getJSONArray("labels");
          for (int j = 0; j < tagsArray.length(); j++) {
            JSONObject o = tagsArray.getJSONObject(j);
            tags.add(o.getString("name"));
          }
          data.add(new RunnerData(
              object.getLong("id"),
              object.getString("name"),
              object.getString("os"),
              object.getString("status"),
              object.getBoolean("busy"),
              tags.toArray(new String[0])
          ));
        }
        return data.toArray(new RunnerData[0]);
      } else {
        throw new ConfigurationException("api access error; in most cases a configuration problem");
      }
    } catch (Exception e) {
      ErrorHandler.handle(e);
    }
    return null;
  }

  public static boolean deleteRunner(RunnerConfiguration config, Long id) {
    try {
      Request request = new Request.Builder()
          .header("Authorization", "token " + config.getGhToken())
          .header("Accept", "application/vnd.github.v3+json")
          .url("https://api.github.com/orgs/" + config.getOrg() + "/actions/runners/" + id)
          .delete()
          .build();
      Response response = client.newCall(request).execute();
      return response.code() == 204;
    } catch (Exception e) {
      ErrorHandler.handle(e);
    }
    return false;
  }

}
