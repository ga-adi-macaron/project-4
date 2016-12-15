package com.scottlindley.sudokuapp;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Scott Lindley on 12/14/2016.
 */

public class PuzzTransformer {

    public PuzzTransformer() {

    }

    public void transformAndPost(List<Integer> key, String difficulty) {
        ArrayList<Integer> key2 = new ArrayList<>();
        ArrayList<Integer> key3 = new ArrayList<>();
        ArrayList<Integer> key4 = new ArrayList<>();

        for (int i = 0; i < key.size(); i++) {
            if (key.get(i) != 0) {
                int shiftA = key.get(i) + 2;
                int shiftB = key.get(i) + 5;
                int shiftC = key.get(i) + 7;
                if (shiftA > 9) {
                    shiftA = shiftA - 9;
                }
                if (shiftB > 9) {
                    shiftB = shiftB - 9;
                }
                if (shiftC > 9) {
                    shiftC = shiftC - 9;
                }
                key2.add(shiftA);
                key3.add(shiftB);
                key4.add(shiftC);
            } else {
                key2.add(0);
                key3.add(0);
                key4.add(0);
            }
        }

        ArrayList<ArrayList<Integer>> transformedKeys = new ArrayList<>();
        transformedKeys.add((ArrayList<Integer>) key);
        transformedKeys.add(mirrorX(key));
        transformedKeys.add(mirrorY(key));
        transformedKeys.add(mirrorY(mirrorX(key)));
        transformedKeys.add(key2);
        transformedKeys.add(mirrorX(key2));
        transformedKeys.add(mirrorY(key2));
        transformedKeys.add(mirrorY(mirrorX(key2)));
        transformedKeys.add(key3);
        transformedKeys.add(mirrorX(key3));
        transformedKeys.add(mirrorY(key3));
        transformedKeys.add(mirrorY(mirrorX(key3)));
        transformedKeys.add(key4);
        transformedKeys.add(mirrorX(key4));
        transformedKeys.add(mirrorY(key4));
        transformedKeys.add(mirrorY(mirrorX(key4)));

        FirebaseDatabase db = FirebaseDatabase.getInstance();
        DatabaseReference ref = db.getReference("Puzzles");

        for (int i = 0; i < transformedKeys.size(); i++) {
            ref.child(difficulty).push().setValue(transformedKeys.get(i));
        }
    }

    private ArrayList<Integer> mirrorX(List<Integer> key){
        ArrayList<Integer> mirrored = new ArrayList<>();
        ArrayList<Integer> row = new ArrayList<>();
        for(int i=0; i<key.size(); i++){
            row.add(key.get(i));
            if (i%9==8){
                Collections.reverse(row);
                mirrored.addAll(row);
                row.clear();
            }
        }
        return mirrored;
    }

    private ArrayList<Integer> mirrorY(List<Integer> key){
        ArrayList<Integer> mirrored = new ArrayList<>();
        ArrayList<ArrayList<Integer>> rows = new ArrayList<>();
        ArrayList<Integer> row = new ArrayList<>();
        for(int i=0; i<key.size(); i++){
            row.add(key.get(i));
            if(i%9==8){
                ArrayList<Integer> addedRow = new ArrayList<>();
                addedRow.addAll(row);
                rows.add(addedRow);
                row.clear();
            }
        }
        Collections.reverse(rows);
        for(ArrayList<Integer> r : rows){
            mirrored.addAll(r);
        }
        return mirrored;
    }
}
