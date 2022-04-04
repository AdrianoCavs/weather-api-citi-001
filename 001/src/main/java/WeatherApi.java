import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.Scanner;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class WeatherApi {
	
	public String returnTemperatures(String cityName) {
		
		//https://www.metaweather.com/api/location/${woeid}/
		//https://www.metaweather.com/api/location/search/?query=${city}
		String temperatures="N/A";
		String woeid="City woeid not identified, problem ocurred";
		try {
			URL searchWoeidUrl = new URL ("https://www.metaweather.com/api/location/search/?query="+cityName);
			HttpURLConnection conn1 = (HttpURLConnection) searchWoeidUrl.openConnection();
			conn1.setRequestMethod("GET");
			conn1.connect();
			
			int responseCode = conn1.getResponseCode();
			
			//code 200, the page loaded successfully
			if (responseCode != 200) {
				throw new RuntimeException("HttpRespondeCode: " + responseCode);
			}else {
				StringBuilder informationString = new StringBuilder();
				Scanner scanner = new Scanner(searchWoeidUrl.openStream());
				
				while(scanner.hasNext()) {
					informationString.append(scanner.nextLine());
				}
				scanner.close();
				
				//used for checking the results, comment later
				//System.out.println(informationString);
				if (informationString.length() > 2) {
					
					JSONParser parseWoeid = new JSONParser();
					JSONArray jsonArrayWoeid = (JSONArray) parseWoeid.parse(String.valueOf(informationString));
					JSONObject jsonObjectWoeid = (JSONObject) jsonArrayWoeid.get(0);
					
					//used for checking the results, comment later
					//System.out.println("woeid identificado interno na classe: "+jsonObjectWoeid.get("woeid"));
					
					woeid=jsonObjectWoeid.get("woeid").toString();
					cityName=jsonObjectWoeid.get("title").toString();
					
				}else return "Invalid / Not recognized city name";
				
			}
			
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		
		try {
			URL searchTempsUrl = new URL ("https://www.metaweather.com/api/location/"+woeid+"/");
			
			HttpURLConnection conn2 = (HttpURLConnection) searchTempsUrl.openConnection();
			conn2.setRequestMethod("GET");
			conn2.connect();
			
			int responseCode = conn2.getResponseCode();
			
			//code 200, the page loaded successfully
			if (responseCode != 200) {
				throw new RuntimeException("HttpRespondeCode: " + responseCode);
			}else {
				StringBuilder informationString = new StringBuilder();
				Scanner scanner = new Scanner(searchTempsUrl.openStream());
				
				while(scanner.hasNext()) {
					informationString.append(scanner.nextLine());
				}
				scanner.close();
				//used for checking the results, comment later
				//System.out.println(informationString);
				
				JSONParser parseTemps = new JSONParser();
				JSONObject jsonObjectWeather = (JSONObject) parseTemps.parse(String.valueOf(informationString));
				
				JSONArray jsonArrayTemps = (JSONArray) jsonObjectWeather.get("consolidated_weather");
								
				JSONObject jsonObjectTemps = (JSONObject) jsonArrayTemps.get(0);
				
				double celcius= (Double) jsonObjectTemps.get("the_temp");
				double fahrenheit = convertCelciusToFahrenheit(celcius);
				
				DecimalFormat df = new DecimalFormat("#.##");
		       
				//used for checking the results, comment later
				temperatures = "The identified city is "+cityName+". Temperature now is: "+ df.format(celcius) +" ºC / "+df.format(fahrenheit)+"ºF";
				//System.out.println(temperatures);
				
			}
			
			
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return temperatures;
		//return temperatures;
	}
	
	
	public double convertCelciusToFahrenheit (double celcius) {
		double fahrenheit = celcius * 1.8 + 32;
		
		return fahrenheit;
	}

}
