package azevedo.jp.bra.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

import azevedo.jp.bra.R;
import azevedo.jp.bra.entities.Question;
import azevedo.jp.bra.interfaces.OnActionRequired;
import azevedo.jp.bra.util.Utils;
import it.sephiroth.android.library.picasso.Picasso;

/**
 * Created by joaop on 08/07/2017.
 */

public class AdapterQuestionList extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    private Context context;
    private List<Question> questions;
    private boolean isLast;
    private final int LOADING_VIEW = 0;

    public AdapterQuestionList(Context context, List<Question> questions, boolean isLast){
        this.context = context;
        this.questions = questions;
        this.isLast = isLast;
    }

    public void updateInfo(List<Question> questions,boolean isLast){
        this.questions = questions;
        this.isLast = isLast;
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        int res = R.layout.adapter_questionlist;
        View mView = layoutInflater.inflate(res, parent, false);
        if(viewType== LOADING_VIEW) {
            res = R.layout.adapter_listloading;
            mView = layoutInflater.inflate(res, parent, false);
            return new LoadingViewHolder(mView);
        }
        else{
            return new ListViewHolder(mView);
        }

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if(holder instanceof ListViewHolder){
            ListViewHolder mHolder = (ListViewHolder) holder;
            mHolder.tvQuestionTitle.setText(questions.get(position).getQuestion());
            mHolder.tvPostedAt.setText(Utils.getFriendlyDate(questions.get(position).getPublished_at()));
            Picasso.with(context)
                    .load(Utils.getImageUrlDimension(questions.get(position).getThumb_url(),context))
                    .placeholder(R.drawable.placeholder)
                    .into(mHolder.ivList);

            ((ListViewHolder) holder).rlQuestionList.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(context instanceof OnActionRequired){
                        ((OnActionRequired)context).onItemClicked(questions.get(position));
                    }
                }
            });
        }
    }

    @Override
    public int getItemViewType(int position) {
        if(!isLast && position == questions.size()){
            return LOADING_VIEW;
        }
        return 1;
    }

    @Override
    public int getItemCount() {
        if(questions!=null && !isLast)
            return questions.size()+1;
        else if(questions!=null && isLast)
            return questions.size();
        return 0;
    }

    class ListViewHolder extends RecyclerView.ViewHolder{

        private TextView tvQuestionTitle;
        private ImageView ivList;
        private TextView tvPostedAt;
        private RelativeLayout rlQuestionList;

        public ListViewHolder(View itemView) {
            super(itemView);
            tvQuestionTitle = (TextView) itemView.findViewById(R.id.tvQuestionTitle);
            tvPostedAt = (TextView) itemView.findViewById(R.id.tvPostedAt);
            ivList = (ImageView) itemView.findViewById(R.id.ivList);
            rlQuestionList = (RelativeLayout) itemView.findViewById(R.id.rlQuestionList);
        }
    }

    class LoadingViewHolder extends RecyclerView.ViewHolder{

        public LoadingViewHolder(View itemView) {
            super(itemView);
        }
    }
}
