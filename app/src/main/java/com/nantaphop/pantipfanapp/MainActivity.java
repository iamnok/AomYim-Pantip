package com.nantaphop.pantipfanapp;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.widget.FrameLayout;

import com.google.android.gms.ads.AdRequest;
import com.nantaphop.pantipfanapp.event.OpenChangelogDialog;
import com.nantaphop.pantipfanapp.event.OpenForumEvent;
import com.nantaphop.pantipfanapp.event.OpenForumRearrangeEvent;
import com.nantaphop.pantipfanapp.event.OpenLoginScreenEvent;
import com.nantaphop.pantipfanapp.event.OpenTopicEvent;
import com.nantaphop.pantipfanapp.event.OpenUserEvent;
import com.nantaphop.pantipfanapp.event.SetTitleEvent;
import com.nantaphop.pantipfanapp.event.ToggleDrawerEvent;
import com.nantaphop.pantipfanapp.event.UpdateForumListEvent;
import com.nantaphop.pantipfanapp.fragment.DialogMaterialFragment;
import com.nantaphop.pantipfanapp.fragment.ForumHolderFragment;
import com.nantaphop.pantipfanapp.fragment.ForumHolderFragment_;
import com.nantaphop.pantipfanapp.view.BaseActivity;
import com.squareup.otto.Subscribe;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.App;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.res.StringArrayRes;
import org.androidannotations.annotations.res.StringRes;

@EActivity(R.layout.activity_main)
public class MainActivity extends BaseActivity {

    @App
    BaseApplication app;


    @ViewById
    FrameLayout content_frame;
    @ViewById
    DrawerLayout drawer_layout;

    @StringRes
    String drawer_open;
    @StringRes
    String drawer_close;
    @StringRes
    String topic_sort_type_title;
    @StringArrayRes
    String[] topic_sort_type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState == null) {
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.content_frame, new ForumHolderFragment_(), ForumHolderFragment.TAG);
            fragmentTransaction.setCustomAnimations(
                    R.anim.enter_slide_from_bottom,
                    0,
                    0,
                    R.anim.activity_scale_out_to_bottom_center
            );
            fragmentTransaction.commit();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        System.out.print(item.toString());
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (drawer_layout.isDrawerOpen(Gravity.START))
            drawer_layout.closeDrawers();
        else
            super.onBackPressed();
    }

    @AfterViews
    void init() {
        setDrawerLayout(drawer_layout);
        setDrawerCloseText(drawer_close);
        setDrawerOpenText(drawer_open);

        // Set the drawer toggle as the DrawerListener
        drawer_layout.setDrawerListener(mDrawerToggle);
        AdRequest adRequest = new AdRequest.Builder().build();
    }

    @Override
    public void onCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
        overridePendingTransition(R.anim.activity_enter_slide_from_bottom, R.anim.activity_exit_slide_to_bottom);

    }

    @Override
    protected void onResume() {
        super.onResume();
        app.getEventBus().register(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        app.getEventBus().unregister(this);
    }

    @Subscribe
    public void openTopic(final OpenTopicEvent e) {
        TopicActivity_.intent(this).topic(e.getTopic()).startingLocation(e.getStartingLocation()).start();
        overridePendingTransition(0, 0);
        Log.d("fragment", "open topic");
        drawer_layout.closeDrawers();


    }

    @Subscribe
    public void openForum(OpenForumEvent e) {
        Intent i = new Intent(this, ForumActivity_.class);
        i.putExtra("forumPagerItem", e.forumPagerItem);
        i.putExtra("forumType", e.forumType);
        startActivity(i);
        overrideAnimationBeforeStartActivity();
        Log.d("fragment", "open forum");
        drawer_layout.closeDrawers();
    }


    @Subscribe
    public void openForumRearrange(OpenForumRearrangeEvent e) {
        Log.d("fragment", "rearrange");
        Intent i = new Intent(this, RoomArrangementActivity_.class);
        startActivity(i);
        drawer_layout.closeDrawers();
    }

    @Subscribe
    public void openLogin(OpenLoginScreenEvent e) {
        Intent i = new Intent(this, LoginActivity_.class);
        startActivity(i);
        drawer_layout.closeDrawers();

    }

    @Subscribe
    public void openUser(OpenUserEvent e) {
        UserActivity_.intent(this).userId(e.getUserId()).user(e.getUsername()).avatar(e.getAvatar()).start();
        drawer_layout.closeDrawers();

    }

    private void openFragment(android.support.v4.app.Fragment f, String tag) {
        mDrawerToggle.setDrawerIndicatorEnabled(false);
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.setCustomAnimations(
                R.anim.activity_enter_slide_from_bottom,
                0,
                0,
                R.anim.activity_exit_slide_to_bottom
        );
        fragmentTransaction.add(R.id.content_frame, f, tag);
        fragmentTransaction.addToBackStack(tag);
        fragmentTransaction.commit();
    }

    @Subscribe
    public void updateForumList(UpdateForumListEvent e) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.content_frame, new ForumHolderFragment_(), ForumHolderFragment.TAG);
        fragmentTransaction.setCustomAnimations(R.anim.enter_slide_from_bottom, 0, 0, R.anim.activity_scale_out_to_bottom_center);
        fragmentTransaction.commit();
    }

    @Subscribe
    public void toggleDrawer(ToggleDrawerEvent e) {
        if (drawer_layout.isDrawerOpen(Gravity.START)) {
            drawer_layout.closeDrawers();
        } else {
            drawer_layout.openDrawer(Gravity.START);
        }
    }

    @Subscribe
    public void setTitle(SetTitleEvent e) {
        getSupportActionBar().setTitle(e.title);
    }

    @Subscribe
    public void openChangelogDialog(OpenChangelogDialog e){
        openDialogFragment(new DialogMaterialFragment());
        drawer_layout.closeDrawers();

    }
    private void openDialogFragment(DialogFragment dialogStandardFragment) {
        if (dialogStandardFragment!=null){
            FragmentManager fm = getSupportFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            Fragment prev = fm.findFragmentByTag("changelogdemo_dialog");
            if (prev != null) {
                ft.remove(prev);
            }
            //ft.addToBackStack(null);

            dialogStandardFragment.show(ft,"changelogdemo_dialog");
        }
    }


}
