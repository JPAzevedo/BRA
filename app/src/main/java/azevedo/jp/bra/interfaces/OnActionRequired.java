package azevedo.jp.bra.interfaces;

import azevedo.jp.bra.entities.Question;

/**
 * Created by joaop on 08/07/2017.
 */

public interface OnActionRequired {
    public void onRequestData(int offset);
    public void onItemClicked(Question question);
    public void onRetry();
}
