package azevedo.jp.bra.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import azevedo.jp.bra.R;
import azevedo.jp.bra.interfaces.OnActionRequired;

/**
 * Created by joaop on 08/07/2017.
 */

public class FragmentRetry extends Fragment  {
    private ImageView ivRetry;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View mView = inflater.inflate(R.layout.fragment_retry,container,false);
        ivRetry = (ImageView) mView.findViewById(R.id.ivRetry);
        ivRetry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(getActivity() instanceof OnActionRequired){
                    ((OnActionRequired) getActivity()).onRetry();
                }
            }
        });
        return mView;
    }


}
