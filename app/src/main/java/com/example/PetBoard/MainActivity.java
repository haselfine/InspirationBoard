package com.example.PetBoard;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.View;

import com.example.PetBoard.db.Pet;

public class MainActivity extends AppCompatActivity implements
        PetListFragment.PetClickedListener,
        ButtonFragment.ButtonClickedListener,
        EditPetFragment.SaveChangesListener{


    private static final String TAG = "MAIN_ACTIVITY";

    private static final String TAG_LIST = "PetListFragment";
    private static final String TAG_BUTTON = "ButtonFragment";
    private static final String TAG_DETAIL = "DetailFragment";
    private static final String TAG_EDIT = "EditFragment";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        PetListFragment petListFragment = PetListFragment.newInstance();
        ButtonFragment buttonFragment = ButtonFragment.newInstance();
        PetDetailFragment petDetailFragment = PetDetailFragment.newInstance();

        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();

        ft.add(R.id.pet_detail_container, petDetailFragment, TAG_DETAIL);
        ft.add(R.id.pet_list_container, petListFragment, TAG_LIST);
        ft.add(R.id.button_container, buttonFragment, TAG_BUTTON);

        ft.commit();

    }

    @Override
    public void buttonClicked(View button){
        int buttonID = button.getId();
        boolean edit;
        switch (buttonID){
            case R.id.add_button:
                edit = false;
                callEditFragment(edit);
                break;
            case R.id.delete_button:
                //TODO add delete functionality
            case R.id.edit_button:
                edit = true;
                callEditFragment(edit);
                break;
        }
    }

    private void callEditFragment(boolean edit) {
        EditPetFragment editPetFragment = EditPetFragment.newInstance(edit);

        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();

        ft.replace(android.R.id.content, editPetFragment,TAG_EDIT);

        ft.addToBackStack(TAG_EDIT);

        ft.commit();
    }

    @Override
    public void petClicked (Pet pet){
        FragmentManager fm = getSupportFragmentManager();
        PetDetailFragment detailFragment = (PetDetailFragment) fm.findFragmentByTag(TAG_DETAIL);
        detailFragment.setPet(pet);
    }

    @Override
    public void saveChanges(Pet pet, boolean edit){
        FragmentManager fm = getSupportFragmentManager();
        PetListFragment petListFragment = (PetListFragment) fm.findFragmentByTag(TAG_LIST);
        petListFragment.updateList(pet, edit);

        FragmentTransaction ft = fm.beginTransaction();
        EditPetFragment editPetFragment = (EditPetFragment) fm.findFragmentByTag(TAG_EDIT);
        if (editPetFragment != null){
            ft.remove(editPetFragment);
        }

        ft.commit();


    }
}
