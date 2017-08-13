package com.example.pdepu.veganapp_p3_h1.fragments;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.pdepu.veganapp_p3_h1.R;
import com.example.pdepu.veganapp_p3_h1.activities.MainActivity;
import com.example.pdepu.veganapp_p3_h1.models.Token;
import com.example.pdepu.veganapp_p3_h1.models.User;
import com.example.pdepu.veganapp_p3_h1.network.Service;
import com.example.pdepu.veganapp_p3_h1.network.ServicesInitializer;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by pdepu on 8/08/2017.
 */

public class ProfileFragment extends Fragment {
    private Service service;
    private Token token;
    private User user;

    @BindView(R.id.imageViewUserProfile)
    ImageView imageViewUserProfile;

    @BindView(R.id.usernameProfile)
    TextView textViewUsernameProfile;

    @BindView(R.id.followerAmountProfile)
    TextView textViewFollowerAmountProfile;

    @BindView(R.id.veganPointsProfile)
    TextView textViewVeganPointsProfile;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setHasOptionsMenu(true);
        service = new ServicesInitializer().initializeService();
        if (getArguments() != null) {
            token = new Gson().fromJson(getArguments().getString("tokenString"), Token.class);
            callApi();
        }else{
            SharedPreferences prefs = getActivity().getSharedPreferences("prefs", Activity.MODE_PRIVATE);
            if(prefs.getString("tokenStringPreferences",null) !=  null) {
                token = new Gson().fromJson(getArguments().getString("tokenString"), Token.class);
                callApi();
            }
        }



    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_profile, container, false);
        ButterKnife.bind(this, rootView);
        return rootView;

    }

//    @Override
//    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
//        super.onCreateOptionsMenu(menu,inflater);
//        inflater.inflate(R.menu.main, menu);
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        int id = item.getItemId();
//
//        if (id == R.id.action_settings) {
//            startEditFragment();
//            return true;
//        }
//
//        return super.onOptionsItemSelected(item);
//    }

    private void startEditFragment() {
        EditProfileFragment fragment = new EditProfileFragment();
        Bundle extras = new Bundle();
        extras.putString("tokenString", new Gson().toJson(token));
        fragment.setArguments(extras);
        this.getFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .addToBackStack(null)
                .commit();
    }


    private void callApi() {
        Call<User> userCall = service.getUserById(token.getUserid());
        userCall.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful()) {
                    user = response.body();
                    user.setToken(token);
                    updateView(user);
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Log.i("failure", t.toString());
            }
        });

    }

    private void updateView(User user) {
        if (user.getImage() != null && !user.getImage().isEmpty())
            Picasso.with(imageViewUserProfile.getContext()).load(user.getImage()).fit().into(imageViewUserProfile);
        textViewUsernameProfile.setText(user.getFullName());
        textViewFollowerAmountProfile.setText(String.valueOf(user.getFollowingUsers().length) + " followers");
        textViewVeganPointsProfile.setText(String.valueOf(user.getTotalVeganScore()) + " vegan score");
        MainActivity activity = (MainActivity) getActivity();
        activity.updateView(user);


    }


}
