package com.example.rocali.movieclub.Models;

import android.content.Context;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by rocali on 9/25/15.
 */
public class Fbase {
    private Context context;
    public Firebase firebaseRef;

    public Fbase(Context context){
        this.context = context;
        com.firebase.client.Firebase.setAndroidContext(context);
        firebaseRef = new com.firebase.client.Firebase("https://torrid-heat-8747.firebaseio.com/");
    }
    public void addPartyToCloud(Party party) {
        Firebase searchedRef = firebaseRef.child("Parties");
        searchedRef.push().setValue(party);
    }
    //FIREBASE PART
    public void fetchParties(){
        final Model model = Model.getInstance(context);
        Firebase searchedRef = firebaseRef.child("Parties");
        searchedRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                ArrayList<Party> parties = new ArrayList<Party>(){};
                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                    Party party = postSnapshot.getValue(Party.class);
                    parties.add(party);
                    //model.savePartyIntoDB(party);
                }
                model.setParties(parties);

                //populateListView(false);
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                System.out.println("The read failed: " + firebaseError.getMessage());
            }
        });
    }
}
