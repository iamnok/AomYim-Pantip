package com.nantaphop.pantipfanapp.utils;

import android.content.Context;
import android.widget.ArrayAdapter;
import com.r0adkll.postoffice.PostOffice;
import com.r0adkll.postoffice.model.Delivery;
import com.r0adkll.postoffice.model.Design;
import com.r0adkll.postoffice.styles.ListStyle;

/**
 * Created by nantaphop on 16-Oct-14.
 */
public class PostOfficeHelper {

    public static Delivery newSimpleListMailCancelable(Context ctx, CharSequence title, Design design, CharSequence[] contents, ListStyle.OnItemAcceptedListener<CharSequence> listener){
        ArrayAdapter<CharSequence> adapterHolo = new ArrayAdapter(ctx, android.R.layout.simple_list_item_1, contents);
        ArrayAdapter<CharSequence> adapterMtrl = new ArrayAdapter(ctx, design.isLight() ? com.r0adkll.postoffice.R.layout.simple_listitem_mtrl_light : com.r0adkll.postoffice.R.layout.simple_listitem_mtrl_dark, contents);

        return PostOffice.newMail(ctx)
                .setTitle(title)
                .setDesign(design)
                .setCanceledOnTouchOutside(true)
                .setCancelable(true)
                .setStyle(new ListStyle.Builder(ctx)
                                  .setDividerHeight(design.isMaterial() ? 0 : 2)
                                  .setOnItemAcceptedListener(listener)
                                  .build(design.isMaterial() ? adapterMtrl : adapterHolo))
                .build();
    }
}