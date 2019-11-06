package com.example.PetBoard;


import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;



/**
 * A simple {@link Fragment} subclass.
 */
public class ButtonFragment extends Fragment implements View.OnClickListener{
    //I made the buttons their own fragment so I wouldn't have to depend on any other fragment and could move them around better

    private static final String TAG = "Button Fragment";

    public interface ButtonClickedListener{
        void buttonClicked(View button); //sends to main where it uses a switch statement to read which button was pressed
    }

    private ButtonClickedListener mButtonClickedListener;

    ImageButton mAddButton;
    ImageButton mEditButton;
    ImageButton mDeleteButton;


    public static ButtonFragment newInstance() {
        final ButtonFragment fragment = new ButtonFragment();
        return fragment;
    }

    @Override
    public void onAttach(@NonNull Context context){
        Log.d(TAG, "onAttach");

        super.onAttach(context);

        if(context instanceof ButtonClickedListener){
            mButtonClickedListener = (ButtonClickedListener) context;
            Log.d(TAG, "On attach configured listener " + mButtonClickedListener);
        } else {
            throw new RuntimeException(context.toString() + "must implement ButtonClickedListener");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_button, container, false);

        mAddButton = view.findViewById(R.id.add_button);
        mAddButton.setOnClickListener(this);

        mDeleteButton = view.findViewById(R.id.delete_button);
        mDeleteButton.setOnClickListener(this);

        mEditButton = view.findViewById(R.id.edit_button);
        mEditButton.setOnClickListener(this);

        return view;
    }
    @Override
    public void onClick(View button){
        mButtonClickedListener.buttonClicked(button);
    } //read above note
}
