package azevedo.jp.bra.activities.controller;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import azevedo.jp.bra.C;
import azevedo.jp.bra.activities.ActivityMain;
import azevedo.jp.bra.entities.HTTPResquetHelper;
import azevedo.jp.bra.entities.Question;
import azevedo.jp.bra.entities.Status;
import azevedo.jp.bra.interfaces.OnHTTPRequestResponse;
import azevedo.jp.bra.webservice.WebRequester;

/**
 * Created by joaop on 08/07/2017.
 */

public class ActivityMainController implements OnHTTPRequestResponse{
    private ActivityMain listener;
    public ActivityMainController(ActivityMain listener){
        this.listener = listener;
    }

    public void startWorking(){
        getServerStatus();
    }

    private void getServerStatus(){
        WebRequester mRequest = new WebRequester(this,listener);
        mRequest.getData(C.API_TYPE.HEALTH_API,null);
    }

    public void getQuestionList(int offset){
        WebRequester mRequest = new WebRequester(this,listener);
        HTTPResquetHelper.HTTPResquetHelperBuilder builder = new HTTPResquetHelper.HTTPResquetHelperBuilder();
        builder.setLimit(C.REQUEST_LIMIT);
        builder.setOffset(offset);
        mRequest.getData(C.API_TYPE.QUESTION_API,builder.build());
    }

    public void getQuestionList(int offset, String filter){
        WebRequester mRequest = new WebRequester(this,listener);
        HTTPResquetHelper.HTTPResquetHelperBuilder builder = new HTTPResquetHelper.HTTPResquetHelperBuilder();
        builder.setLimit(C.REQUEST_LIMIT);
        builder.setOffset(offset);
        builder.setFilter(filter);
        mRequest.getData(C.API_TYPE.QUESTION_API,builder.build());
    }

    @Override
    public void onSuccess(String json, C.API_TYPE type) {
        Gson mGsonObject = new Gson();
        if(type == C.API_TYPE.HEALTH_API){
            if(json == null || json.isEmpty())
                manageStatus(null);

            Status status =  mGsonObject.fromJson(json,Status.class);
            manageStatus(status.getStatus());
        }
        else if(type == C.API_TYPE.QUESTION_API){
            if(json == null || json.isEmpty())
                updateActivity(null);

            List<Question> questions = new ArrayList<Question>();
            Type listType = new TypeToken<ArrayList<Question>>(){}.getType();
            questions = mGsonObject.fromJson(json, listType);
            updateActivity(questions);
        }

    }

    @Override
    public void onError(String error) {
        listener.onServerNotOk();
    }

    private void manageStatus(final String status){
        if((status != null) && status.equals(C.SERVER_OK)){
            getQuestionList(0);
        }
        else{
            listener.onServerNotOk();
         }
    }

    private void updateActivity(List<Question> questions){
        listener.onQuestionListSuccess(questions);
    }
}