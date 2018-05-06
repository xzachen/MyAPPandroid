package io.github.zeleven.mua.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import butterknife.ButterKnife;
import io.github.zeleven.mua.Model.Thing;
import io.github.zeleven.mua.R;

//笔记的文章适配器写在这！！！
public class ThingRecyclerAdapter extends BaseRecyclerAdapter<Thing> {

    @Override
    public ThingHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);
        return new ThingHolder(view);
    }
//绑定了列表中的子视图R.layout.list_item解析并且绑定视图。

    public class ThingHolder extends BaseRecyclerAdapter<Thing>.ViewHolder {

        TextView titleTextView;
        TextView contentTextView;

        public ThingHolder(View itemView) {
            super(itemView);
            titleTextView=itemView.findViewById(R.id.title);
            contentTextView=itemView.findViewById(R.id.content);
        }

        public void populate(Thing item) {
            titleTextView.setText(item.text);
        }
    }
}
