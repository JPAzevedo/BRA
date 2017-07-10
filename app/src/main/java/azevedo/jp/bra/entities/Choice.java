package azevedo.jp.bra.entities;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by joaop on 08/07/2017.
 */

public class Choice implements Parcelable{
    private String choice;
    private int votes;

    public Choice(String choice, int votes) {
        this.choice = choice;
        this.votes = votes;
    }

    protected Choice(Parcel in) {
        choice = in.readString();
        votes = in.readInt();
    }

    public static final Creator<Choice> CREATOR = new Creator<Choice>() {
        @Override
        public Choice createFromParcel(Parcel in) {
            return new Choice(in);
        }

        @Override
        public Choice[] newArray(int size) {
            return new Choice[size];
        }
    };

    public String getChoice() {
        return choice;
    }

    public void setChoice(String choice) {
        this.choice = choice;
    }

    public int getVotes() {
        return votes;
    }

    public void setVotes(int votes) {
        this.votes = votes;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(choice);
        dest.writeInt(votes);
    }

}
