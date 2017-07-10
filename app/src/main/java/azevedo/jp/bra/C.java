package azevedo.jp.bra;

/**
 * Created by joaop on 07/07/2017.
 */

public class C {

    // To debug the app
    public static boolean DEBUG = true;
    public static final String DEBUG_TAG = "BRA_APP_DEBUG";

    // Server Status
    public static final String SERVER_NOT_OK = "NOK";
    public static final String SERVER_OK = "OK";
    public static final String SERVER_STATUS = "status";
    public static final int HTTP_TIMEOUT = 15;

    // Web Service URLs
    public static final String MAIN_URL = "https://private-bbbe9-blissrecruitmentapi.apiary-mock.com";
    public static final String HEALTH_API = "/health";
    public static final String QUESTION_API = "/questions";
    public static final String SHARE_API = "/share?";
    public static final String QUESTION_API_LIMIT = "?";
    public static final String API_PARAM_SPLITTER = "&";
    public static final int REQUEST_LIMIT = 10;

    // API options
    public static enum API_TYPE{
        HEALTH_API,
        QUESTION_API,
        QUESTION_DETAIL_API,
        SHARE_API,
        QUESTION_UPDATE_API
    };

    // Parcelable extra name
    public static final String QUESTION_EXTRA_NAME  = "QUESTION_DATA";

    public static final String BROADCAST_ACTION = "android.net.conn.CONNECTIVITY_CHANGE";


}
