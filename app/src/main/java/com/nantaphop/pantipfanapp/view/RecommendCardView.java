package com.nantaphop.pantipfanapp.view;

import android.content.Context;
import android.os.Build;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.nantaphop.pantipfanapp.BaseApplication;
import com.nantaphop.pantipfanapp.R;
import com.nantaphop.pantipfanapp.event.OpenTopicEvent;
import com.nantaphop.pantipfanapp.response.Topic;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.App;
import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.res.ColorRes;

/**
 * Created by nantaphop on 28-Oct-14.
 */
@EViewGroup(R.layout.card_recommend)
public class RecommendCardView extends LinearLayout {
    @App
    BaseApplication app;
    @ViewById
    LinearLayout root;
    @ViewById
    CardView card;

    @ViewById
    TextView recommend1;
    @ViewById
    TextView recommend2;
    @ViewById
    TextView recommend3;
    @ViewById
    ProgressBar progressBar;

    @ColorRes(R.color.base_color_bright)
    int rippleColor;
    private Context context;


    public RecommendCardView(Context context) {
        super(context);
        this.context = context;
    }

    @AfterViews
    void init(){
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            card.setElevation(context.getResources().getDimensionPixelSize(R.dimen.card_topic_elevation));
        }
    }

    public void addItem(final String title, final String url) {
        progressBar.setVisibility(GONE);
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        TextView text = null;
        if(recommend1.getText().length() == 0){
            text = recommend1;
        }else if(recommend2.getText().length() == 0){
            text = recommend2;
        }else if(recommend3.getText().length() == 0){
            text = recommend3;
        }
        if (text != null) {
//            RippleDrawable.createRipple(text, rippleColor);
            text.setText(title);
            final Topic topic = new Topic();
            topic.setTitle(title);
            try {
                topic.setId(Integer.parseInt(url.split("/")[4]));
            } catch (Exception e) {
                e.printStackTrace();
                Log.e("recommend", "Exception on set Recommend Item > "+title+""+url);
            }
            text.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (topic.getId()>0) {
                        app.fireEvent(new OpenTopicEvent(topic));
                    }
                }
            });
        }
    }

}
