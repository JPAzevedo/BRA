package azevedo.jp.bra.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import azevedo.jp.bra.R;
import azevedo.jp.bra.adapters.AdapterQuestionList;
import azevedo.jp.bra.entities.Question;
import azevedo.jp.bra.interfaces.OnActionRequired;

/**
 * Created by joaop on 08/07/2017.
 */

public class FragmentQuestionList extends Fragment {

    private View mView;
    private static List<Question> questions;
    private AdapterQuestionList mAdapterQuestionList;
    private RecyclerView mRecyclerView;
    private boolean isLast = false;
    private TextView tvNoRecords;

    // Endless loading
    private int pastVisiblesItems, visibleItemCount, totalItemCount;
    private boolean loading = true;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_questionlist, container, false);
        mAdapterQuestionList = new AdapterQuestionList(getActivity(), questions, isLast);
        mRecyclerView = (RecyclerView) mView.findViewById(R.id.rvQuestionList);
        tvNoRecords = (TextView) mView.findViewById(R.id.tvNoRecords);
        setUpRecyclerView();
        return mView;
    }

    private void setUpRecyclerView(){
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setAdapter(mAdapterQuestionList);
        updateRecyclerView();
        setScrollListener(mLayoutManager);
    }

    private void updateRecyclerView() {
        loading = true;
        if (mRecyclerView != null && tvNoRecords != null) {
            if (questions == null || questions.size() == 0) {
                mRecyclerView.setVisibility(View.GONE);
                tvNoRecords.setVisibility(View.VISIBLE);
            } else {
                mRecyclerView.setVisibility(View.VISIBLE);
                tvNoRecords.setVisibility(View.GONE);
            }
        }
    }

    public void clearData() {
        if(questions!=null) {
            isLast = false;
            questions.clear();
        }
    }

    public void onDataReceived(List<Question> questions, boolean isLast) {
        this.isLast = isLast;
        boolean slideUp = false;
        if(this.questions == null || this.questions.size() == 0) {
            this.questions = questions;
            slideUp = true;
        }else{
            this.questions.addAll(questions);
        }

        updateView();
        if(slideUp && mRecyclerView!=null && getActivity()!=null){
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mRecyclerView.smoothScrollToPosition(0);
                }
            });
        }
    }

    private void updateView() {
        if (mAdapterQuestionList != null)
            mAdapterQuestionList.updateInfo(questions, isLast);

        updateRecyclerView();
    }

    // http://stackoverflow.com/questions/26543131/how-to-implement-endless-list-with-recyclerview
    private void setScrollListener(final LinearLayoutManager mLayoutManager) {
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (!isLast) {
                        if (dy > 0) //check for scroll down
                        {
                            visibleItemCount = mLayoutManager.getChildCount();
                            totalItemCount = mLayoutManager.getItemCount();
                            pastVisiblesItems = mLayoutManager.findFirstVisibleItemPosition();

                            if (loading) {
                                if ((visibleItemCount + pastVisiblesItems) >= totalItemCount) {
                                    loading = false;
                                    ((OnActionRequired) getActivity()).onRequestData(questions.size());
                                }
                            }
                        }
                }
            }
        });
    }

}
