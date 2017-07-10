package azevedo.jp.bra.activities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import azevedo.jp.bra.C;
import azevedo.jp.bra.R;
import azevedo.jp.bra.activities.controller.ActivityDetailController;
import azevedo.jp.bra.adapters.AdapterDetail;
import azevedo.jp.bra.entities.Question;
import azevedo.jp.bra.interfaces.ActivityDetailInterface;
import azevedo.jp.bra.interfaces.OnChoiceClicked;
import azevedo.jp.bra.util.NetworkUtils;
import azevedo.jp.bra.util.Utils;
import it.sephiroth.android.library.picasso.Picasso;

/**
 * Created by joaop on 07/07/2017.
 */

public class ActivityDetail extends AppCompatActivity implements ActivityDetailInterface, OnChoiceClicked {
    private TextView tvDetail;
    private ImageView ivDetail;
    private RecyclerView rvDetail;
    private boolean isConnected = true;
    private MenuItem shareItem;
    private Question question;
    private AdapterDetail adapter;

    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            isConnected = NetworkUtils.isConnected(context);
            if (isConnected && shareItem != null) {
                shareItem.setVisible(true);
            } else if (shareItem != null) {
                shareItem.setVisible(false);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        tvDetail = (TextView) findViewById(R.id.tvDetail);
        ivDetail = (ImageView) findViewById(R.id.ivDetail);
        rvDetail = (RecyclerView) findViewById(R.id.rlDetail);
        Toolbar tbMain = (Toolbar) findViewById(R.id.tbDetail);
        setSupportActionBar(tbMain);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        manageData();
    }

    private void setUpRecyclerView(Question question) {
        adapter = new AdapterDetail(this, question.getChoices());
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        rvDetail.setLayoutManager(mLayoutManager);
        rvDetail.setItemAnimator(new DefaultItemAnimator());
        rvDetail.setAdapter(adapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        IntentFilter filter = new IntentFilter();
        filter.addAction(C.BROADCAST_ACTION);
        this.registerReceiver(this.broadcastReceiver, filter);
    }

    public void onStop() {
        super.onStop();
        this.unregisterReceiver(this.broadcastReceiver);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_detail, menu);
        shareItem = menu.findItem(R.id.share);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            case R.id.share:
                shareAlertDialog();
                return true;
            default:
                return true;
        }

    }

    private void manageData() {
        question = getIntent().getExtras().getParcelable(C.QUESTION_EXTRA_NAME);
        if (question != null) {
            setUpRecyclerView(question);
            String url = question.getImage_url();
            Picasso.with(this)
                    .load(Utils.getImageUrlDimension(url, this))
                    .placeholder(R.drawable.placeholder)
                    .resize(Utils.getScreenWidth(this), (int) (Utils.getScreenWidth(this) * Utils.getAspectRatio())) // resizes the image to these dimensions (in pixel)
                    .into(ivDetail);
            tvDetail.setText(question.getQuestion());
        } else {
            tvDetail.setText("extra is not working");
        }
    }

    @Override
    public void onShare(final boolean success) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (success) {
                    Toast.makeText(ActivityDetail.this, getString(R.string.share_success), Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(ActivityDetail.this, getString(R.string.detail_error), Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    @Override
    public void onVote(final Question questionClone) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(questionClone!=null) {
                    adapter.dataHasChanged(questionClone.getChoices());
                    Toast.makeText(ActivityDetail.this, getString(R.string.detail_votesucess), Toast.LENGTH_LONG).show();
                }
                else{
                    onError(null);
                }
            }
        });
    }

    @Override
    public void onError(String error) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(ActivityDetail.this, getString(R.string.detail_error), Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void onItemClicked(int position) {
        areYouSure(position);
    }

    //Share alert dialog
    private void shareAlertDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.share_email));

        final EditText etEmail = new EditText(this);
        etEmail.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
        etEmail.setSingleLine(true);
        builder.setView(etEmail);

        builder.setPositiveButton(getText(R.string.share_okbutton), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {

                if (Utils.isEmailValid(etEmail.getText().toString())) {
                    ActivityDetailController mController = new ActivityDetailController(ActivityDetail.this, ActivityDetail.this);
                    mController.shareRequest(mController.getQuestionURL(question.getId()), etEmail.getText().toString());
                } else {
                    Toast.makeText(ActivityDetail.this, getString(R.string.share_invalidemail), Toast.LENGTH_LONG).show();
                }

                dialog.dismiss();
            }
        });

        builder.setNegativeButton(getText(R.string.share_cancelbutton),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }

    //Share alert dialog
    private void areYouSure(final int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.detail_votetitle));
        builder.setMessage(getString(R.string.detail_votemessage)+ " "  +question.getChoices().get(position).getChoice() +" ?");
        builder.setPositiveButton(getText(R.string.vote_yes), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                ActivityDetailController mController = new ActivityDetailController(ActivityDetail.this,ActivityDetail.this);
                mController.updateRequest(question,position);
            }
        });

        builder.setNegativeButton(getText(R.string.vote_no),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }
}
