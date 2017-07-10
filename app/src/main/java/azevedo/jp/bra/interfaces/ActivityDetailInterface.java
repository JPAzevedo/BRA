package azevedo.jp.bra.interfaces;

import azevedo.jp.bra.entities.Question;

/**
 * Created by joaop on 10/07/2017.
 */

public interface ActivityDetailInterface {
    public void onShare(boolean success);
    public void onVote(Question questionClone);
    public void onError(String error);
}
