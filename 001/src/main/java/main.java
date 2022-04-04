import java.util.Scanner;

public class main {

	public static void main (String[] args) {
		// TODO Auto-generated method stub
		WeatherApi wapi = new WeatherApi();
		
		//String woeid = wapi.returnTemperatures("london");
		
		//System.out.println("Woeid identificado pelo método MAIN: "+woeid);
		
		Scanner scan = new Scanner(System.in);
		System.out.print("Inform a city name: ");
		String cityName = scan.nextLine();
		scan.close();

		System.out.println(wapi.returnTemperatures(cityName));
		
	}

}
