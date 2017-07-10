package azevedo.jp.bra.interfaces;

import azevedo.jp.bra.C;

/**
 * Created by joaop on 07/07/2017.
 */

public interface OnHTTPRequestResponse {
    public void onSuccess(final String json, final C.API_TYPE type);
    public void onError(final String error);
}
