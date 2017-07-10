package azevedo.jp.bra.webservice;

import azevedo.jp.bra.C;
import azevedo.jp.bra.entities.HTTPResquetHelper;

/**
 * Created by joaop on 07/07/2017.
 */

public class URLManager {

    public static String getURL(C.API_TYPE type, HTTPResquetHelper httpResquetHelper){
        switch (type){
            case QUESTION_API:
                return friendlyUrl(getQuestionListURL(httpResquetHelper.getLimit(),httpResquetHelper.getOffset(),httpResquetHelper.getFilter()));
            case QUESTION_DETAIL_API:
                return friendlyUrl(getQuestionURL(httpResquetHelper.getId()));
            case QUESTION_UPDATE_API:
                return friendlyUrl(getQuestionURL(httpResquetHelper.getId()));
            case HEALTH_API:
                return friendlyUrl(getHealthURL());
            case SHARE_API:
                return friendlyUrl(getShareURL(httpResquetHelper.getUrl(),httpResquetHelper.getEmail()));
            default:
                return friendlyUrl(getQuestionURL());
        }
    }

     private static String getHealthURL(){
        return C.MAIN_URL + C.HEALTH_API;
     }

    private static String getQuestionURL(){
        return C.MAIN_URL + C.QUESTION_API;
    }

    private static String getShareURL(String url, String email){
        return C.MAIN_URL + C.SHARE_API + url + C.API_PARAM_SPLITTER + email;
    }

    private static String getQuestionURL(int id){
        return C.MAIN_URL + C.QUESTION_API + "/" + id;
    }

    private static String getQuestionListURL(int limit, int offset, String filter){
        if(limit==0)
            return  getQuestionListURL(offset,filter);
        if(filter == null)
            filter = "";

        return getQuestionURL() + C.QUESTION_API_LIMIT + limit + C.API_PARAM_SPLITTER + offset + C.API_PARAM_SPLITTER + filter;
    }

    private static String getQuestionListURL(int offset, String filter){
        return getQuestionURL() + C.QUESTION_API_LIMIT + C.API_PARAM_SPLITTER + offset + C.API_PARAM_SPLITTER + filter;
    }

    private static String friendlyUrl(String url){
        url = url.replaceAll(" ", "%20");
        return url;
    }
}
