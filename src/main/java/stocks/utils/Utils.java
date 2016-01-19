package stocks.utils;

/**
 * Created by allemaos on 18/01/16.
 */
public class Utils {

    public static double roundDecimal(double d){
        return Math.round(d * 100.0) / 100.0;
    }
}
