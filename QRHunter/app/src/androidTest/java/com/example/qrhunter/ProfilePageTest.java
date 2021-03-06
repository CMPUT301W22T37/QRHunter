package com.example.qrhunter;

import static org.junit.Assert.assertTrue;

import android.app.Activity;
import android.widget.EditText;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.robotium.solo.Solo;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.util.Random;

public class ProfilePageTest {
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
    public void profilePageTest(){

        solo.assertCurrentActivity("Wrong Activity",MainMenu.class);

        solo.clickOnView(solo.getView(R.id.profile_icon));
        solo.assertCurrentActivity("Wrong Activity",ProfilePage.class);
        assertTrue(solo.waitForText(username));
        assertTrue(solo.waitForText("Game Stats QR Code"));
        solo.clickOnView(solo.getView(R.id.GenerateAccountQR));
        assertTrue(solo.waitForText(username));

    }


    @After
    public void tearDown() throws Exception{
        solo.finishOpenedActivities();
    }
}
