package azevedo.jp.bra.interfaces;

import java.util.List;

import azevedo.jp.bra.entities.Question;

/**
 * Created by joaop on 08/07/2017.
 */

public interface ActivityMainInterface {
    public void onServerNotOk();
    public void onQuestionListSuccess(final List<Question> questions);
}
