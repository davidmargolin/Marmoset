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
import android.widget.Toast;

import com.sequencing.androidoauth.core.ISQAuthCallback;
import com.sequencing.androidoauth.core.OAuth2Parameters;
import com.sequencing.androidoauth.core.SQUIoAuthHandler;
import com.sequencing.appchains.AppChains;
import com.sequencing.appchains.DefaultAppChainsImpl;
import com.sequencing.fileselector.FileEntity;
import com.sequencing.fileselector.core.ISQFileCallback;
import com.sequencing.fileselector.core.SQUIFileSelectHandler;
import com.sequencing.oauth.config.AuthenticationParameters;
import com.sequencing.oauth.core.Token;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;
import com.squareup.picasso.Picasso;

import org.androidannotations.annotations.App;

import java.io.IOException;

import de.hdodenhof.circleimageview.CircleImageView;


public class ProfileFragment extends Fragment implements ISQAuthCallback {

    // TODO: Rename and change types of parameters
    private String name;
    private String image_url;
    private String about_me;
    private String email;
    SQUIoAuthHandler ioAuthHandler;
    private OnProfileFragmentInteractionListener mListener;

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
        Log.e("yay token1!!", token.getAccessToken());
        SQUIFileSelectHandler fileSelectHandler = new SQUIFileSelectHandler(this.getActivity());
        fileSelectHandler.selectFile(OAuth2Parameters.getInstance().getOauth(), new ISQFileCallback() {
            @Override
            public void onFileSelected(FileEntity entity, Activity activity) {
                MediaType JSON
                        = MediaType.parse("application/json; charset=utf-8");

                OkHttpClient client = new OkHttpClient();
                RequestBody body = RequestBody.create(JSON, "");
                Request request = new Request.Builder()
                        .url("https://appchainskms.azurewebsites.net/api/HttpTriggerPython31?code=SxLl3btzGiE/4JJEwbIkXSzsCYhLUEVMyu6BUxJSFDiYc5ywFgLvOQ==")
                        .post(body)
                        .build();
                try {
                    Response response = client.newCall(request).execute();
                    if (response.body().string() == "Success") {
                        Toast.makeText(getActivity(), "Success!.", Toast.LENGTH_LONG).show();
                    }else{
                        Toast.makeText(getActivity(),"Error. Please try again.", Toast.LENGTH_LONG).show();
                    }
                }catch (IOException e){
                    Toast.makeText(getActivity(),"Error. Please try again.", Toast.LENGTH_LONG).show();
                }
//                Log.e("selected file: ", entity.toString());
//                com.iter.marmoset.AppChains chains = new com.iter.marmoset.AppChains(token.getAccessToken(), "api.sequencing.com");
//                com.iter.marmoset.AppChains.Report result = chains.getReport("StartApp", "Chain8006", entity.getId());
//                //chains.getReportBatch()
//
//                if (!result.isSucceeded())
//                    System.out.println("Request has failed");
//                else
//                    System.out.println("Request has succeeded");
//
//                for (com.iter.marmoset.AppChains.Result r : result.getResults())
//                {
//                    com.iter.marmoset.AppChains.ResultType type = r.getValue().getType();
//
//                    if (type == com.iter.marmoset.AppChains.ResultType.TEXT)
//                    {
//                        com.iter.marmoset.AppChains.TextResultValue v = (com.iter.marmoset.AppChains.TextResultValue) r.getValue();
//                        Log.e(r.getName(), v.getData());
//                    }
//                }

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
