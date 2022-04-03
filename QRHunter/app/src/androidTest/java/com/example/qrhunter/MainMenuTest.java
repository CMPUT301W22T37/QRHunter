package com.example.qrhunter;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import android.app.Activity;
import android.content.Context;
import android.provider.Settings;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.google.firebase.firestore.FirebaseFirestore;
import com.robotium.solo.Solo;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.util.HashMap;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

public class MainMenuTest {
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


    @Test
    public void clickQR(){
        solo.assertCurrentActivity("Wrong Activity",MainMenu.class);
        // currently, we are initializing user with two qr codes
        String text = solo.clickInList(0).get(0).getText().toString();
        solo.assertCurrentActivity("Wrong Activity",QrCodePage.class);
        assertTrue(solo.waitForText(text));

    }
//    @Test
//    public void deleteQR() {
//        addUser();
//        // currently, we are initializing user with two qr codes
//        assertTrue(solo.waitForText("Codes Scanned: 2"));
//        assertTrue(solo.waitForText("Total Score: 245"));
//        String text = solo.clickInList(0).get(0).getText().toString();
//        solo.assertCurrentActivity("Wrong Activity",QrCodePage.class);
//
//        solo.clickOnButton("Delete Code");
//        solo.assertCurrentActivity("Wrong Activity",MainMenu.class);
//        for(TextView textView : solo.getCurrentViews(TextView.class)) {
//            Log.d("DEBUG", "Text on the screen: " + textView.getText().toString());
//        }
//        assertTrue(solo.waitForText("Codes Scanned: 1"));
//        assertTrue(solo.waitForText("Total Score: 134"));
//        assertFalse(text.equals(solo.clickInList(0).get(0).getText().toString())); //check that it has been removed from the list
//    }

    @After
    public void tearDown() throws Exception{
        solo.finishOpenedActivities();
    }
}
