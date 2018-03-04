package com.makaroffandrey.stackoverflowusersexample.ui;

import android.app.Activity;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.makaroffandrey.stackoverflowusersexample.R;
import com.makaroffandrey.stackoverflowusersexample.repo.User;

import java.text.DateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * A fragment representing a single User detail screen.
 * This fragment is either contained in a {@link UserListActivity}
 * in two-pane mode (on tablets) or a {@link UserDetailActivity}
 * on handsets.
 */
public class UserDetailFragment extends Fragment {
    /**
     * The fragment argument representing the item ID that this fragment
     * represents.
     */
    public static final String ARG_USER = "user";

    /**
     * The dummy content this fragment is presenting.
     */
    private User user;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public UserDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments().containsKey(ARG_USER)) {
            // Load the dummy content specified by the fragment
            // arguments. In a real-world scenario, use a Loader
            // to load content from a content provider.
            user = (User) getArguments().getSerializable(ARG_USER);

            Activity activity = this.getActivity();
            CollapsingToolbarLayout appBarLayout = activity.findViewById(R.id.toolbar_layout);
            if (appBarLayout != null) {
                appBarLayout.setTitle(user.display_name);
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.user_detail, container, false);

        // Show the dummy content as text in a TextView.
        if (user != null) {
            ((SimpleDraweeView) rootView.findViewById(R.id.profile_image)).setImageURI(user
                    .profile_image);
            ((TextView) rootView.findViewById(R.id.user_name)).setText(user.display_name);
            ((TextView) rootView.findViewById(R.id.user_id)).setText(String.valueOf(user
                    .account_id));
            ((TextView) rootView.findViewById(R.id.user_reputation)).setText(String.valueOf(user
                    .reputation));
            ((TextView) rootView.findViewById(R.id.user_location)).setText(String.valueOf(user
                    .location));
            ((TextView) rootView.findViewById(R.id.user_creation)).setText(DateFormat
                    .getDateInstance(DateFormat.DEFAULT, Locale.getDefault()).format(new Date(user
                            .creation_date * 1000)));
        }

        return rootView;
    }
}
