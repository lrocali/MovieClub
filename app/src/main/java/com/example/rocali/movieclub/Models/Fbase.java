package com.example.rocali.movieclub.Models;

import android.content.Context;
import android.util.Log;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by rocali on 9/25/15.
 */
public class Fbase {
    private Context context;
    public Firebase firebaseRef;

    public Fbase(Context context){
        this.context = context;
        Firebase.getDefaultConfig().setPersistenceEnabled(true);
        Firebase.setAndroidContext(context);

        firebaseRef = new com.firebase.client.Firebase("https://torrid-heat-8747.firebaseio.com/");
    }
    public void addPartyToCloud(Party party) {
        Firebase addPartyRef= firebaseRef.child("Parties").child(party.getId());
        //String newID = addPartyRef.getKey();
        //party.setFBkey(newID);
        addPartyRef.setValue(party);
    }
    public void updatePartyInCloud(Party party) {
        Log.v("TAG", "UPDATING TO FIREBASE" + party.getId());
        Firebase searchedRef = firebaseRef.child("Parties");
        searchedRef.child(party.getId()).setValue(party);
        //Map<String, Object> upp = new HashMap<String, Object>();
        //upp.put(party.getFBkey(),party);
        //searchedRef.child();
    }
    //FIREBASE PART
    public void fetchParties() {
        final Model model = Model.getInstance(context);
        try {
            Firebase searchedRef = firebaseRef.child("Parties");
            searchedRef.keepSynced(true);
            searchedRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot snapshot) {
                    ArrayList<Party> parties = new ArrayList<Party>() {
                    };
                    for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                        Party party = postSnapshot.getValue(Party.class);
                        parties.add(party);
                    }
                    model.setParties(parties);

                }

                @Override
                public void onCancelled(FirebaseError firebaseError) {
                    System.out.println("The read failed: " + firebaseError.getMessage());
                }
            });

        } catch (Exception e) {
            Log.v("TAG", "NOT CONECTED");
        }
    }


}
