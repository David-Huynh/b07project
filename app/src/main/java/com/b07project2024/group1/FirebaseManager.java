package com.b07project2024.group1;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Logger;

public class FirebaseManager {
    private static FirebaseManager firebaseManager;
    private final DatabaseReference reference;
    private final FirebaseDatabase database;

    private FirebaseManager () {
        FirebaseDatabase fd = FirebaseDatabase.getInstance("https://b07projectgroup1-default-rtdb.firebaseio.com/");
        fd.setPersistenceEnabled(true);
        fd.setLogLevel(Logger.Level.DEBUG);
        reference = fd.getReference();
        database = fd;
    }

    public static synchronized FirebaseManager getInstance() {
        if (firebaseManager == null) {
            firebaseManager = new FirebaseManager();
        }
        return firebaseManager;
    }

    public FirebaseDatabase getDatabase() {
        return database;
    }

    public DatabaseReference getReference() {
        return reference;
    }
}
