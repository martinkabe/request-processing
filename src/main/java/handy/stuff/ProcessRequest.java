package handy.stuff;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Base64;
import java.util.HashMap;
import java.util.Set;
import java.util.TreeSet;

public class ProcessRequest {

    private HashMap<String, Integer> hashMap = new HashMap<>();

    public void getRequest() {
        String urlLink = "https://voting-stats.herokuapp.com/api/v1.0/votes";
        String user = "Martin";
        String pwd = "nitram21459";

        try {
            URL url = new URL (urlLink);
            String encoding = Base64.getEncoder().encodeToString((user + ":" + pwd).getBytes());

            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setDoOutput(true);
            connection.setRequestProperty  ("Authorization", "Basic " + encoding);
            InputStream content = (InputStream)connection.getInputStream();
            BufferedReader in   =
                    new BufferedReader (new InputStreamReader(content));
            String line;
            while ((line = in.readLine()) != null) {
                JSONArray jsonarray = new JSONArray(line);
                for (int i = 0; i < jsonarray.length(); i++) {
                    JSONObject jsonobject = jsonarray.getJSONObject(i);
                    String choice = jsonobject.getString("choice");
                    if (hashMap.containsKey(choice)) {
                        int count = hashMap.get(choice);
                        hashMap.put(choice, count+1);
                    } else {
                        hashMap.put(choice, 1);
                    }
                }
            }
            displayData(hashMap);
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    private void displayData(HashMap<String, Integer> map) {
        Set<String> keys = map.keySet(); // list of unique words because it's a Set
        TreeSet<String> sortedKeys = new TreeSet<>(keys); // ascending order of words
        int countStudents = map.values().stream().mapToInt(Integer::intValue).sum();

        System.out.println();
        System.out.println("----------------------------------");
        System.out.println("#Students voted: " + countStudents);
        System.out.println("----------------------------------");
        for (String str: sortedKeys) {
            int val = map.get(str);
            double perc = val / (double)countStudents * 100;
            System.out.println(str + " --> " +
                    map.get(str) + " (" +
                    String.format("%.2f", perc) +
                    "%)");
        }
    }
}
