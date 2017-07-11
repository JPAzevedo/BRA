package azevedo.jp.bra.activities;

import android.app.SearchManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import azevedo.jp.bra.C;
import azevedo.jp.bra.R;
import azevedo.jp.bra.activities.controller.ActivityMainController;
import azevedo.jp.bra.entities.Question;
import azevedo.jp.bra.fragments.FragmentLoading;
import azevedo.jp.bra.fragments.FragmentNoInternet;
import azevedo.jp.bra.fragments.FragmentQuestionList;
import azevedo.jp.bra.fragments.FragmentRetry;
import azevedo.jp.bra.interfaces.ActivityMainInterface;
import azevedo.jp.bra.interfaces.OnActionRequired;
import azevedo.jp.bra.util.NetworkUtils;

/**
 * Created by joaop on 08/07/2017.
 */

public class ActivityMain extends AppCompatActivity implements ActivityMainInterface, OnActionRequired {

    private SearchView searchView;
    private MenuItem searchItem;
    private static String filter;
    private FragmentQuestionList fragmentQuestionList;
    private FragmentLoading fragmentLoading;
    private static boolean isFirst = true;
    private static boolean isConnected = false;
    private AtomicInteger offset = new AtomicInteger(0);
    private ActivityMainController mainController;
    private boolean running = false;

    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // do UI updates
            boolean updateUI = false;
            if (!isConnected) {
                updateUI = NetworkUtils.isConnected(context);
            }
            isConnected = NetworkUtils.isConnected(context);
            if (!isConnected) {
                switchFragment(new FragmentNoInternet());
                setMenuVisible(false);
            } else if (updateUI) {
                if (fragmentQuestionList != null) {
                    fragmentQuestionList.clearData();
                }
                getDataFromServer();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar tbMain = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(tbMain);
        getSupportActionBar().setDisplayShowHomeEnabled(false);
        getSupportActionBar().setDisplayUseLogoEnabled(false);
    }

    @Override
    protected void onResume() {
        super.onResume();
        IntentFilter filter = new IntentFilter();
        filter.addAction(C.BROADCAST_ACTION);
        this.registerReceiver(this.broadcastReceiver, filter);
        running = true;
    }

    public void onPause() {
        super.onPause();
        this.unregisterReceiver(this.broadcastReceiver);
        running = false;
    }

    // Search Menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        searchItem = menu.findItem(R.id.search);

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView = (SearchView) searchItem.getActionView();
        searchView.setQueryHint(getString(R.string.search_title));
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setIconifiedByDefault(false);
        searchWhileTyping();
        if (isFirst) {
            setMenuVisible(false);
        }
        return true;
    }

    @Override
    protected void onNewIntent(Intent intent) {
        handleIntent(intent);
    }

    private void handleIntent(Intent intent) {

        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            if (fragmentLoading == null) {
                fragmentLoading = new FragmentLoading();
            }
            if (fragmentQuestionList != null) {
                fragmentQuestionList.clearData();
            }
            if (isConnected) {
                setMenuVisible(false);
                switchFragment(fragmentLoading);
                offset.set(0);
                filter = intent.getStringExtra(SearchManager.QUERY);
                mainController = new ActivityMainController(this);
                mainController.getQuestionList(0, filter);
            }
        }
    }

    private void switchFragment(Fragment frag) {
        if(running) {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.flFragment, frag);
            transaction.commit();
        }
    }

    // Server request and its management
    private void getDataFromServer() {
        fragmentLoading = new FragmentLoading();
        switchFragment(fragmentLoading);
        ActivityMainController mainController = new ActivityMainController(this);
        mainController.startWorking();
    }

    @Override
    public void onServerNotOk() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (isConnected) {
                    FragmentRetry fragmentRetry = new FragmentRetry();
                    switchFragment(fragmentRetry);
                }
            }
        });
    }

    @Override
    public void onQuestionListSuccess(final List<Question> questions) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                boolean isLast = false;
                int offsetValue = offset.get();

                if (fragmentQuestionList == null) {
                    fragmentQuestionList = new FragmentQuestionList();
                }
                if (offsetValue == 0) {
                    fragmentQuestionList.clearData();
                }
                if (questions == null || questions.size() % 10 != 0) {
                    isLast = true;
                }
                fragmentQuestionList.onDataReceived(questions, isLast);
                switchFragment(fragmentQuestionList);
                setMenuVisible(true);
            }
        });
    }


    //Search while typing
    private void searchWhileTyping() {
        searchView.setOnQueryTextFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus)
                    searchView.setQuery(filter, false);
            }
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (!newText.isEmpty() && !newText.equals(filter)) {
                    if (fragmentLoading != null) {
                        switchFragment(fragmentLoading);
                    }
                    filter = newText;
                    onRequestData(0);
                }
                return false;
            }
        });
    }

    private void setMenuVisible(boolean visible) {
        if (searchItem != null) {
            searchItem.setVisible(visible);
            if (visible && isFirst) {
                searchItem.expandActionView();
                isFirst = false;
            } else if (searchView.getVisibility() != View.VISIBLE) {
                searchItem.collapseActionView();
            }
        }
    }

    @Override
    public void onRequestData(int offset) {
        this.offset.set(offset);
        if (mainController == null) {
            mainController = new ActivityMainController(this);
        }
        mainController.getQuestionList(offset, filter);
    }

    @Override
    public void onItemClicked(Question question) {
        Intent mIntent = new Intent(ActivityMain.this, ActivityDetail.class);
        mIntent.putExtra(C.QUESTION_EXTRA_NAME, question);
        startActivity(mIntent);
    }

    @Override
    public void onRetry() {
        getDataFromServer();
    }
}