package azevedo.jp.bra.util;

import android.content.Context;

/**
 * Created by joaop on 08/07/2017.
 */

public class Utils {
    private final static int DEFAULT_DIMEN = 120;
    private final static int DEFAULT_DIMEN_BIG_WIDTH = 600;
    private final static int DEFAULT_DIMEN_BIG_HEIGHT = 400;

    public static String getImageUrlDimension(String url, Context context){
        int density = (int) context.getResources().getDisplayMetrics().density;
        return url.replace(Integer.toString(DEFAULT_DIMEN),Integer.toString(DEFAULT_DIMEN*density));
    }

    public static int getScreenWidth(Context context){
        int width = (int) context.getResources().getDisplayMetrics().widthPixels;
        return width;
    }

    public static float getAspectRatio(){
        return DEFAULT_DIMEN_BIG_HEIGHT/DEFAULT_DIMEN_BIG_WIDTH;
    }

    public static String getFriendlyDate(String date){
        return date.substring(0,10);
    }

    public static  boolean isEmailValid(String email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }
}
