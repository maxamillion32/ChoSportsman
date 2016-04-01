package com.chokavo.chosportsman.ui.fragments.helloscreen;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.chokavo.chosportsman.R;
import com.chokavo.chosportsman.ui.fragments.BaseFragment;
import com.vk.sdk.VKScope;
import com.vk.sdk.VKSdk;

/**
 * Created by ilyapyavkin on 01.04.16.
 */
public class VKAuthFragment extends BaseFragment {
    @Override
    public String getFragmentTitle() {
        return null;
    }

    Button mSignVK;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_vk_auth, container, false);
        initViews(rootView);
        return rootView;
    }

    private void initViews(View rootView) {
        mSignVK = (Button) rootView.findViewById(R.id.sign_vk_btn);
        mSignVK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                VKSdk.login(getActivity(), VKScope.AUDIO, VKScope.FRIENDS);
            }
        });
    }

}
