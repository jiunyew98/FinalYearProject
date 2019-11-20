package com.example.logindemo;


//temporary storage while app is running
public class Singleton {
    UserProfile userProfile;

    private static final Singleton ourInstance = new Singleton();

    public static Singleton getInstance() {
        return ourInstance;
    }

    private Singleton() {
    }
}
