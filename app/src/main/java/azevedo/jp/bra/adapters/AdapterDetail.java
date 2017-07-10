package azevedo.jp.bra.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

import azevedo.jp.bra.R;
import azevedo.jp.bra.entities.Choice;
import azevedo.jp.bra.interfaces.OnChoiceClicked;

/**
 * Created by joaop on 10/07/2017.
 */

public class AdapterDetail extends RecyclerView.Adapter<AdapterDetail.MyViewHolder>{
    private List<Choice> choice;
    private Context context;
    public AdapterDetail(Context context, List<Choice> choice){
        this.choice = choice;
        this.context = context;
    }

    public void dataHasChanged(List<Choice> choice){
        this.choice = choice;
        notifyDataSetChanged();
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View mView = layoutInflater.inflate(R.layout.adapter_detail, parent, false);
        return new MyViewHolder(mView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        holder.tvLanguage.setText(choice.get(position).getChoice());
        holder.tvVotes.setText(Integer.toString(choice.get(position).getVotes()));
        holder.rlVotesMaster.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(context instanceof OnChoiceClicked){
                    ((OnChoiceClicked) context).onItemClicked(position);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return choice.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder{
        private TextView tvVotes;
        private TextView tvLanguage;
        private RelativeLayout rlVotesMaster;

        public MyViewHolder(View itemView) {
            super(itemView);
            tvVotes = (TextView) itemView.findViewById(R.id.tvVotes);
            tvLanguage = (TextView) itemView.findViewById(R.id.tvLanguage);
            rlVotesMaster = (RelativeLayout) itemView.findViewById(R.id.rlVotesMaster);
        }
    }
}
