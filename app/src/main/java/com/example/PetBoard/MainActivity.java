package com.example.PetBoard;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.PetBoard.db.Pet;

public class MainActivity extends AppCompatActivity implements
        PetListFragment.PetClickedListener,
        ButtonFragment.ButtonClickedListener,
        EditPetFragment.SaveChangesListener{


    Pet mPet;

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

        ft.add(R.id.pet_list_container, petListFragment, TAG_LIST);
        ft.add(R.id.pet_detail_container, petDetailFragment, TAG_DETAIL);
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
                petToDelete(mPet);

                FragmentManager fm = getSupportFragmentManager();
                PetDetailFragment detailFragment = (PetDetailFragment) fm.findFragmentByTag(TAG_DETAIL);
                mPet = detailFragment.defaultPet();
                detailFragment.setPet(mPet);

                break;
            case R.id.edit_button:
                edit = true;
                callEditFragment(edit);
                break;
        }
    }

    public void petToDelete(Pet pet){
        FragmentManager fm = getSupportFragmentManager();
        PetListFragment petListFragment = (PetListFragment) fm.findFragmentByTag(TAG_LIST);
        try{
            petListFragment.deletePet(pet);
        } catch (NullPointerException npe){
            Toast.makeText(this, "Pet does not exist", Toast.LENGTH_LONG).show();
        }
    }

    private void callEditFragment(boolean edit) {

        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        try {
            PetDetailFragment detail = (PetDetailFragment) fm.findFragmentByTag(TAG_DETAIL);
            mPet = detail.getCurrentPet();
        } catch (NullPointerException npe){
            Log.d(TAG, "No current pet.");
        }
        if(edit){
            EditPetFragment editPetFragment = EditPetFragment.newInstance(mPet);
            ft.replace(android.R.id.content, editPetFragment,TAG_EDIT);
        } else {
            EditPetFragment editPetFragment = EditPetFragment.newInstance();
            ft.replace(android.R.id.content, editPetFragment,TAG_EDIT);
        }

        ft.addToBackStack(TAG_EDIT);

        ft.commit();
    }



    @Override
    public void petClicked (Pet pet){
        try {
            FragmentManager fm = getSupportFragmentManager();
            PetDetailFragment detailFragment = (PetDetailFragment) fm.findFragmentByTag(TAG_DETAIL);
            mPet = pet;
            detailFragment.setPet(mPet);
        } catch (NullPointerException npe){
            mPet = pet;
        }
    }

    @Override
    public void saveChanges(Pet pet){
        FragmentManager fm = getSupportFragmentManager();
        PetListFragment petListFragment = (PetListFragment) fm.findFragmentByTag(TAG_LIST);
        petListFragment.updateList(pet);

        FragmentTransaction ft = fm.beginTransaction();
        EditPetFragment editPetFragment = (EditPetFragment) fm.findFragmentByTag(TAG_EDIT);
        if (editPetFragment != null){
            ft.remove(editPetFragment);
        }

        ft.commit();


    }
}
