package utils;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class HttpUtils {

	public static JsonObject callURL(String myURL) {
		//System.out.println("Requeted URL:" + myURL);
		StringBuilder sb = new StringBuilder();
		URLConnection urlConn = null;
		InputStreamReader in = null;
		try {
			URL url = new URL(myURL);
			urlConn = url.openConnection();
			if (urlConn != null)
				urlConn.setReadTimeout(60 * 1000);
			if (urlConn != null && urlConn.getInputStream() != null) {
				in = new InputStreamReader(urlConn.getInputStream(),
						Charset.defaultCharset());
				BufferedReader bufferedReader = new BufferedReader(in);
				if (bufferedReader != null) {
					int cp;
					while ((cp = bufferedReader.read()) != -1) {
						sb.append((char) cp);
					}
					bufferedReader.close();
				}
			}
			in.close();
		} catch (Exception e) {
			throw new RuntimeException("Exception while calling URL:" + myURL,
					e);
		}
		
		//Gson gson = new Gson();
		JsonParser parser = new JsonParser();
		JsonObject o = parser.parse(sb.toString()).getAsJsonObject();
		// JsonNode jsonObj = ;
		//ObjectNode result = Json.newObject();
		//result.putArray(sb.toString().replace("\n", ""));
		return o;
	}

	public static void main(String[] args) {
		String url = "https://www.googleapis.com/oauth2/v3/tokeninfo?id_token=XYZ123";
		System.out.println("\nOutput: \n" + HttpUtils.callURL(url));
	}

}
