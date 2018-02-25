package com.iter.marmoset;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.github.kittinunf.fuel.Fuel;
import com.github.kittinunf.fuel.core.FuelError;
import com.github.kittinunf.fuel.core.Handler;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.sequencing.androidoauth.core.ISQAuthCallback;
import com.sequencing.androidoauth.core.OAuth2Parameters;
import com.sequencing.androidoauth.core.SQUIoAuthHandler;
import com.sequencing.fileselector.FileEntity;
import com.sequencing.fileselector.core.ISQFileCallback;
import com.sequencing.fileselector.core.SQUIFileSelectHandler;
import com.sequencing.oauth.config.AuthenticationParameters;
import com.sequencing.oauth.core.Token;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;


public class ProfileFragment extends Fragment implements ISQAuthCallback {
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    // TODO: Rename and change types of parameters
    private String name;
    private String image_url;
    private String about_me;
    private String email;
    SQUIoAuthHandler ioAuthHandler;
    private OnProfileFragmentInteractionListener mListener;
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    public ProfileFragment() {
        // Required empty public constructor
    }

    public static ProfileFragment newInstance(String name, String image_url, String about_me, String email) {
        ProfileFragment fragment = new ProfileFragment();
        Bundle args = new Bundle();
        args.putString("name", name);
        args.putString("image_url", image_url);
        args.putString("about_me", about_me);
        args.putString("email", email);

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            name = getArguments().getString("name");
            image_url = getArguments().getString("image_url");
            about_me = getArguments().getString("about_me");
            email = getArguments().getString("email");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        CircleImageView profPic = view.findViewById(R.id.prof_pic);
        EditText profName = view.findViewById(R.id.prof_name);
        Button sequenceConnect = view.findViewById(R.id.sequence_connect);
        TextView emailView = view.findViewById(R.id.email);
        EditText aboutView = view.findViewById(R.id.about_me);
        aboutView.setText(about_me);
        emailView.setText(email);
        AuthenticationParameters parameters = new AuthenticationParameters.ConfigurationBuilder()
                .withRedirectUri("wmw://login/Default/Authcallback")
                .withScope("SCOPES")
                .withResponseType("code")
                .withClientId("Marmoset")
                .withClientSecret("nfrrgi4LmKnBbSS2E_b-CJYJgLgs9xe_7rSAepMq66a0ArNbpnBZkoTjX5-Qe-tXewjvBcOhQHUltWZ8n4S5Tw")
                .build();
        ioAuthHandler = new SQUIoAuthHandler(getActivity());
        ioAuthHandler.authenticate(sequenceConnect, this, parameters);
        Picasso.with(getActivity()).load(image_url).placeholder(getResources()
                .getDrawable(R.drawable.ic_account_circle_black_24dp))
                .error(getResources().getDrawable(R.drawable.ic_account_circle_black_24dp))
                .into(profPic);

        profName.setText(name);
        TextView switcher = view.findViewById(R.id.switcher);
        switcher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Map<String, Object> data = new HashMap<>();
                data.put("is_biz", true);

                db.collection("Users").document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                        .set(data, SetOptions.merge());
                ((MainActivity)getActivity()).switcher();
            }
        });
        return view;
    }

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnProfileFragmentInteractionListener) {
            mListener = (OnProfileFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onAuthentication(final Token token){
        SQUIFileSelectHandler fileSelectHandler = new SQUIFileSelectHandler(this.getActivity());
        fileSelectHandler.selectFile(OAuth2Parameters.getInstance().getOauth(), new ISQFileCallback() {
            @Override
            public void onFileSelected(FileEntity entity, Activity activity) {
                Fuel.post("https://appchainskms.azurewebsites.net/api/HttpTriggerPython31?code=SxLl3btzGiE/4JJEwbIkXSzsCYhLUEVMyu6BUxJSFDiYc5ywFgLvOQ==").body(("{ \"token\" : \""+token.getAccessToken()+"\", \"file\" : \""+entity.getId()+"\" }").getBytes()).responseString(new Handler<String>() {
                    @Override
                    public void success(com.github.kittinunf.fuel.core.Request request, com.github.kittinunf.fuel.core.Response response, String s) {
                        db.collection("Genes").document(user.getUid()).set(new Genetics(s));
                    }

                    @Override
                    public void failure(com.github.kittinunf.fuel.core.Request request, com.github.kittinunf.fuel.core.Response response, FuelError fuelError) {

                    }
                });
            }
        }, null);
    }

    @Override
    public void onFailedAuthentication(Exception e){
        Log.e("exception", e.toString());
    }
    public interface OnProfileFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
