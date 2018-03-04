package com.makaroffandrey.stackoverflowusersexample.ui;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.backends.pipeline.PipelineDraweeControllerBuilder;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;
import com.makaroffandrey.stackoverflowusersexample.R;
import com.makaroffandrey.stackoverflowusersexample.repo.User;

import java.util.List;

public class UsersRecyclerViewAdapter
        extends RecyclerView.Adapter<UsersRecyclerViewAdapter.ViewHolder> {

    private final PipelineDraweeControllerBuilder controllerBuilder =
            Fresco.newDraweeControllerBuilder();
    private final UserListActivity parentActivity;
    private final List<User> values;
    private final boolean twoPane;
    private final View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            User item = (User) view.getTag();
            if (twoPane) {
                Bundle arguments = new Bundle();
                arguments.putSerializable(UserDetailFragment.ARG_USER, item);
                UserDetailFragment fragment = new UserDetailFragment();
                fragment.setArguments(arguments);
                parentActivity.getSupportFragmentManager().beginTransaction()
                        .replace(R.id.user_detail_container, fragment)
                        .commit();
            } else {
                Context context = view.getContext();
                Intent intent = new Intent(context, UserDetailActivity.class);
                intent.putExtra(UserDetailFragment.ARG_USER, item);

                context.startActivity(intent);
            }
        }
    };

    UsersRecyclerViewAdapter(UserListActivity parent,
                             List<User> items,
                             boolean twoPane) {
        values = items;
        parentActivity = parent;
        this.twoPane = twoPane;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.user_list_content, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        if (values.get(position).profile_image != null) {
            final ImageRequest imageRequest = ImageRequestBuilder.newBuilderWithSource(Uri.parse
                    (values.get(position).profile_image)).build();
            holder.draweeView.setController(controllerBuilder.reset().setOldController(holder
                    .draweeView.getController()).setImageRequest(imageRequest).build());
        } else {
            holder.draweeView.setController(null);
        }
        holder.idView.setText(values.get(position).display_name);
        holder.contentView.setText(String.valueOf(values.get(position).reputation));
        holder.itemView.setTag(values.get(position));
        holder.itemView.setOnClickListener(mOnClickListener);
    }

    @Override
    public int getItemCount() {
        return values.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        final TextView idView;
        final TextView contentView;
        final SimpleDraweeView draweeView;

        ViewHolder(View view) {
            super(view);
            idView = view.findViewById(R.id.id_text);
            contentView = view.findViewById(R.id.content);
            draweeView = view.findViewById(R.id.profile_image);
        }
    }
}
