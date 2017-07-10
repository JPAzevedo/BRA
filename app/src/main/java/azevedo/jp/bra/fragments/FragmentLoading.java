package azevedo.jp.bra.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import azevedo.jp.bra.R;

/**
 * Created by joaop on 08/07/2017.
 */

public class FragmentLoading extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View mView = inflater.inflate(R.layout.fragment_loading,container,false);
        return mView;
    }
}
