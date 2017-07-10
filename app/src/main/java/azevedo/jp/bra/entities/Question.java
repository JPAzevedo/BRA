package azevedo.jp.bra.entities;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by joaop on 08/07/2017.
 */

public class Question implements Parcelable{
    private int id;
    private String question;
    private String image_url;
    private String thumb_url;
    private String published_at;
    private List<Choice> choices;

    public Question(int id, String question, String image_url, String thumb_url, String published_at, List<Choice> choices) {
        this.id = id;
        this.question = question;
        this.image_url = image_url;
        this.thumb_url = thumb_url;
        this.published_at = published_at;
        this.choices = choices;
    }

    protected Question(Parcel in) {
        id = in.readInt();
        question = in.readString();
        image_url = in.readString();
        thumb_url = in.readString();
        published_at = in.readString();
        choices = in.createTypedArrayList(Choice.CREATOR);
    }

    public static final Creator<Question> CREATOR = new Creator<Question>() {
        @Override
        public Question createFromParcel(Parcel in) {
            return new Question(in);
        }

        @Override
        public Question[] newArray(int size) {
            return new Question[size];
        }
    };

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    public String getThumb_url() {
        return thumb_url;
    }

    public void setThumb_url(String thumb_url) {
        this.thumb_url = thumb_url;
    }

    public String getPublished_at() {
        return published_at;
    }

    public void setPublished_at(String published_at) {
        this.published_at = published_at;
    }

    public List<Choice> getChoices() {
        return choices;
    }

    public void setChoices(List<Choice> choices) {
        this.choices = choices;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(question);
        dest.writeString(image_url);
        dest.writeString(thumb_url);
        dest.writeString(published_at);
        dest.writeTypedList(choices);
    }

}
