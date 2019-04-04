package game;

public class Log {

	public static String UI = "UI";

	public static void log(String type, String message) {
		if(type.equals(UI)) {
			System.out.println(message);
		}
	}
	
}
