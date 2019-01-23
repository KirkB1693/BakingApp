package com.example.android.bakingapp.IdlingResources;

import android.support.test.espresso.IdlingResource;

/**
 * Contains a static reference IdlingResource, and should be available only in a mock build type.
 */
public class EspressoIdlingResource {

    private static final String RESOURCE = "GLOBAL";

    private static final SimpleCountingIdlingResource mCountingIdlingResource =
            new SimpleCountingIdlingResource(RESOURCE);

    public static void increment() {
        mCountingIdlingResource.increment();
    }

    public static void decrement() {
        mCountingIdlingResource.decrement();
    }

    public static IdlingResource getIdlingResource() {
        return mCountingIdlingResource;
    }
}