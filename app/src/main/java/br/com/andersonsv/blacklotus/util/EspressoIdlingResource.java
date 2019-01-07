package br.com.andersonsv.blacklotus.util;

import android.support.test.espresso.IdlingResource;

import br.com.andersonsv.blacklotus.util.SimpleCountingIdlingResource;

public class EspressoIdlingResource {
    private static final String RESOURCE = "GLOBAL";

    private static SimpleCountingIdlingResource mCountingIdlingResource =
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
