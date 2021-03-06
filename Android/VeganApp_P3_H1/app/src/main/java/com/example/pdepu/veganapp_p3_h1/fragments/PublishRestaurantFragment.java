package com.example.pdepu.veganapp_p3_h1.fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.example.pdepu.veganapp_p3_h1.R;
import com.example.pdepu.veganapp_p3_h1.activities.MainActivity;
import com.example.pdepu.veganapp_p3_h1.models.Challenge;
import com.example.pdepu.veganapp_p3_h1.models.User;
import com.example.pdepu.veganapp_p3_h1.network.Service;
import com.example.pdepu.veganapp_p3_h1.network.ServicesInitializer;
import com.example.pdepu.veganapp_p3_h1.views.UriHandler;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.app.Activity.RESULT_OK;

/**
 * Created by pdepu on 12/08/2017.
 */

public class PublishRestaurantFragment extends Fragment {
    private String restaurantName;
    private String restaurantPoints;
    private static final int PICK_IMAGE = 0;

    @BindView(R.id.restaurantNamePublish)
    TextView restaurantNamePublishTextView;

    @BindView(R.id.restaurantPointsPublish)
    TextView restaurantPointsPublishTextView;

    @BindView(R.id.restaurantImage)
    ImageView restaurantImageView;

    @BindView(R.id.publishRestaurantButton)
    Button publishRestaurantButton;

    @BindView(R.id.addPictureLayoutPublish)
    RelativeLayout addPictureLayoutPublish;

    @BindView(R.id.publishLayout)
    LinearLayout publishLayout;

    @BindView(R.id.progress)
    ProgressBar progress;



    private UploadImageTask uploadImageTask;
    private Map response;
    private Challenge challenge;

    private Service service;
    private File file;
    private String uri;
    private String imageUrl;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            restaurantName = getArguments().getString("restaurantName");
            restaurantPoints = getArguments().getString("restaurantPoints");
        }
        service = new ServicesInitializer().initializeService();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_publish_restaurant, container, false);
        ButterKnife.bind(this, rootView);
        updateView();
        return rootView;
    }

    @OnClick({R.id.addPictureLayoutPublish, R.id.restaurantImageButton})
    public void onClick() {
        pickImage();
    }


    @OnClick(R.id.publishRestaurantButton)
    public void onClickPublish() {
        if (file != null && uri != null)
            uploadImage();
        else {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setMessage("Image is required")
                    .setTitle("Required");
            AlertDialog dialog = builder.create();
            dialog.show();
        }

    }

    private void pickImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select picture"), PICK_IMAGE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            String path = UriHandler.getPath(getContext(), data.getData());
            file = new File(path);
            uri = path;
            Picasso.with(getActivity()).load(file).fit().into(restaurantImageView);
        }
    }


    private void uploadImage() {
        uploadImageTask = new UploadImageTask(file.getAbsolutePath());
        uploadImageTask.execute((Void) null);
    }


    private void updateView() {
        restaurantNamePublishTextView.setText("You went to " + restaurantName);
        restaurantPointsPublishTextView.setText("+" + restaurantPoints + " points" + "\n");
    }

    private void callApi() {
        MainActivity activity = (MainActivity) getActivity();
        Call<User> challengeCall = service.postChallenge(activity.token.getUserid(), challenge);
        challengeCall.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful()) {
                    startFeedFragment();
                    Log.i("SUCCESS", "challenge posted to user");
                    progress.setVisibility(View.GONE);
                }
                progress.setVisibility(View.GONE);

            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                progress.setVisibility(View.GONE);
                Log.i("FAILURE", t.toString());
            }
        });
    }

    private void clearSharedPreferences() {
        SharedPreferences prefs = getActivity().getSharedPreferences("prefs", Activity.MODE_PRIVATE);
        prefs.edit().remove("chooseRestaurantsPreferences").apply();

    }

    private void startFeedFragment() {
        clearSharedPreferences();
        progress.setVisibility(View.GONE);
        FeedFragment fragment = new FeedFragment();
        getFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment).addToBackStack(null).commit();
    }

    public class UploadImageTask extends AsyncTask<Void, Void, Boolean> {

        private final String filePath;

        UploadImageTask(String filePath) {
            this.filePath = filePath;
        }

        @Override
        protected Boolean doInBackground(Void... params) {

            Map config = new HashMap();
            config.put("cloud_name", "douskchks");
            config.put("api_key", "552883666323728");
            config.put("api_secret", "RhDl-TvAXIiaPkeBWOHY8OcCwr8");
            final Cloudinary cloudinary = new Cloudinary(config);
            try {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progress.setVisibility(View.VISIBLE);
                        publishLayout.setVisibility(View.GONE);
                    }
                });
                response = cloudinary.uploader().upload(file.getAbsolutePath(), ObjectUtils.emptyMap());
                if (response != null) {
                    imageUrl = response.get("url").toString();
                    return true;
                } else
                    return false;
            } catch (Exception e) {
                return false;
            }

        }

        @Override
        protected void onPostExecute(final Boolean success) {
            if (success) {
                MainActivity activity = (MainActivity) getActivity();
                challenge = new Challenge("Restaurant", restaurantName, imageUrl, Calendar.getInstance().getTime(), 0, Integer.parseInt(restaurantPoints), true, activity.user.getFullName());
                callApi();
            } else {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progress.setVisibility(View.GONE);
                        publishLayout.setVisibility(View.VISIBLE);
                    }
                });
            }
        }

        @Override
        protected void onCancelled() {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    progress.setVisibility(View.GONE);
                    publishLayout.setVisibility(View.VISIBLE);
                }
            });
            uploadImageTask = null;
        }


    }
}
