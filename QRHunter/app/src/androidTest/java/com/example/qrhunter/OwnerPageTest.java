package com.example.qrhunter;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.robotium.solo.Solo;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.util.Random;

public class OwnerPageTest {

    private Solo solo;
    private String username;



    @Rule
    public ActivityTestRule<MainActivity> rule =
            new ActivityTestRule<>(MainActivity.class,true,true);
    @Before
    public void setUp() throws Exception{
        Random rand = new Random();
        int upperbound = 10000;
        int random_user = rand.nextInt(upperbound);
        username = "TestUser"+random_user;
        User user = new TestUser(username, username+"@gmail.com");

        solo = new Solo(InstrumentationRegistry.getInstrumentation(),rule.getActivity());
    }

    @Test
    public void autoSignIn() throws InterruptedException {
        solo.assertCurrentActivity("Wrong Activity",MainMenu.class);
    }
}
