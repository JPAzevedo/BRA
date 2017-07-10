package azevedo.jp.bra.activities.controller;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import azevedo.jp.bra.C;
import azevedo.jp.bra.activities.ActivityDetail;
import azevedo.jp.bra.entities.Choice;
import azevedo.jp.bra.entities.HTTPResquetHelper;
import azevedo.jp.bra.entities.Question;
import azevedo.jp.bra.entities.Status;
import azevedo.jp.bra.interfaces.ActivityDetailInterface;
import azevedo.jp.bra.interfaces.OnHTTPRequestResponse;
import azevedo.jp.bra.webservice.URLManager;
import azevedo.jp.bra.webservice.WebRequester;

/**
 * Created by joaop on 10/07/2017.
 */

public class ActivityDetailController implements OnHTTPRequestResponse{
    private ActivityDetail context;
    private ActivityDetailInterface listener;
    private Question questionClone;

    public ActivityDetailController(ActivityDetail context, ActivityDetailInterface listener){
        this.context = context;
        this.listener = listener;
    }

    public String getQuestionURL(int id){
        HTTPResquetHelper.HTTPResquetHelperBuilder mBuilder = new HTTPResquetHelper.HTTPResquetHelperBuilder().setId(id);
        return URLManager.getURL(C.API_TYPE.QUESTION_DETAIL_API,mBuilder.build());
    }

    public void shareRequest(String email,String url){
        HTTPResquetHelper.HTTPResquetHelperBuilder mBuilder = new HTTPResquetHelper.HTTPResquetHelperBuilder().setEmail(email).setUrl(url);
        WebRequester mWebRequester = new WebRequester(this,context);
        mWebRequester.postData(C.API_TYPE.SHARE_API,mBuilder.build());
    }

    public void updateRequest(Question question, int position){
        HTTPResquetHelper.HTTPResquetHelperBuilder mBuilder = new HTTPResquetHelper.HTTPResquetHelperBuilder().setId(question.getId());
        WebRequester mWebRequester = new WebRequester(this,context);
        questionClone = getUpdateQuestion(question,position);
        String json = questionToJson(questionClone);
        mWebRequester.putData(C.API_TYPE.QUESTION_UPDATE_API,mBuilder.build(),json);
    }

    private Question getUpdateQuestion(Question question, int position) {
        if(question!=null && question.getChoices() != null && question.getChoices().size() >= position) {
            questionClone = cloneQuestionObject(question);
            Choice choice = questionClone.getChoices().get(position);
            choice.setVotes(choice.getVotes() + 1);
            questionClone.getChoices().remove(position);
            questionClone.getChoices().add(position, choice);
        }
        return questionClone;
    }

    private Question cloneQuestionObject(Question question){
        List<Choice> choices = new ArrayList<>();
        for(Choice c: question.getChoices()){
            choices.add(new Choice(c.getChoice(),c.getVotes()));
        }
        return new Question(question.getId(),question.getQuestion(),question.getImage_url(),question.getThumb_url(),question.getPublished_at(),choices);
    }

    private String questionToJson(Question question){
        Gson mGson = new Gson();
        return  mGson.toJson(question);
    }

    @Override
    public void onSuccess(String json, C.API_TYPE type) {
        Gson mGsonObject = new Gson();
        if(type == C.API_TYPE.SHARE_API){
            boolean success = false;
            if(json == null || json.isEmpty())
                listener.onShare(success);

            Status status =  mGsonObject.fromJson(json,Status.class);
            if(status.getStatus().equals(C.SERVER_OK)){
                success = true;
            }
            listener.onShare(success);
        }
        else if(type == C.API_TYPE.QUESTION_UPDATE_API){
            listener.onVote(questionClone);
        }
    }

    @Override
    public void onError(String error) {
        listener.onError(error);
    }
}
