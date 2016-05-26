package com.chokavo.chosportsman.ui.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.chokavo.chosportsman.R;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NonNls;

/**
 * Created by repitch on 26.05.16.
 */
public class IntroPagerFragment extends BaseFragment {

    private static final int MAX_WIDTH = 768;
    private static final int MAX_HEIGHT = 1024;

    int size = (int) Math.ceil(Math.sqrt(MAX_WIDTH * MAX_HEIGHT));

    @NonNls
    public static final String KEY_POSITION = "KEY_POSITION";

    private int mPosition;
    private TextView mTxtDesc;
    private ImageView mImage;

   /* private static final int INTRO_DESCRIPTIONS[] = {
            R.string.intro_desc_0,
            R.string.intro_desc_1,
            R.string.intro_desc_2
    };*/

    public static final int INTRO_IMAGES[] = {
            R.drawable.intro_0,
            R.drawable.intro_1,
            R.drawable.intro_2,
            R.drawable.intro_3
//            R.drawable.intro_1,
//            R.drawable.intro_2
    };

    public static Fragment newInstance(int position) {
        Bundle args = new Bundle();
        args.putInt(KEY_POSITION, position);
        return newInstance(args);
    }

    public static Fragment newInstance(Bundle args) {
        IntroPagerFragment fragment = new IntroPagerFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (!getArguments().containsKey(KEY_POSITION)) {
            throw new RuntimeException("IntroPagerFragment must contain a \"" + KEY_POSITION + "\" argument!");
        }
        mPosition = getArguments().getInt(KEY_POSITION);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_intro_pager, container, false);
        initViews(view);
        view.setTag(mPosition);
        return view;
    }

    private void initViews(View view) {
        mTxtDesc = (TextView) view.findViewById(R.id.txt_desc);
        mImage = (ImageView) view.findViewById(R.id.image);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

//        mTxtDesc.setText(getString(INTRO_DESCRIPTIONS[mPosition]));

        Picasso.with(view.getContext())
                .load(INTRO_IMAGES[mPosition])
//                .skipMemoryCache()
                .resize(size, size)
                .centerInside()
                .into(mImage);
//        mImage.setImageDrawable(getResources().getDrawable(INTRO_IMAGES[mPosition]));
    }

    @Override
    public String getFragmentTitle() {
        return null;
    }
}
