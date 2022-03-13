package com.example.qrhunter;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import android.app.Activity;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;
import com.robotium.solo.Solo;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

public class MainMenuTest {
    private Solo solo;
    @Rule
    public ActivityTestRule<CreateAccount> rule =
            new ActivityTestRule<>(CreateAccount.class,true,true);
    @Before
    public void setUp() throws Exception{
        solo = new Solo(InstrumentationRegistry.getInstrumentation(),rule.getActivity());
    }

    @Test
    public void addUser(){
        solo.assertCurrentActivity("Wrong Activity",CreateAccount.class);
        Random rand = new Random();
        int upperbound = 10000;
        int random_user = rand.nextInt(upperbound);
        solo.enterText((EditText) solo.getView(R.id.Username_EditText),"testUser" + random_user);
        solo.enterText((EditText) solo.getView(R.id.email_EditText),"test@gmail.com");
        solo.clickOnButton("Create Account");
        solo.assertCurrentActivity("Wrong Activity",MainMenu.class);
    }

    @Test
    public void clickQR(){
        addUser();
        // currently, we are initializing user with two qr codes
        String text = solo.clickInList(0).get(0).getText().toString();
        solo.assertCurrentActivity("Wrong Activity",QrCodePage.class);
        assertTrue(solo.waitForText(text));

    }
    @Test
    public void deleteQR() {
        addUser();
        // currently, we are initializing user with two qr codes
        assertTrue(solo.waitForText("Codes Scanned: 2"));
        assertTrue(solo.waitForText("Total Score: 245"));
        String text = solo.clickInList(0).get(0).getText().toString();
        solo.assertCurrentActivity("Wrong Activity",QrCodePage.class);

        solo.clickOnButton("Delete Code");
        solo.assertCurrentActivity("Wrong Activity",MainMenu.class);
        for(TextView textView : solo.getCurrentViews(TextView.class)) {
            Log.d("DEBUG", "Text on the screen: " + textView.getText().toString());
        }
        assertTrue(solo.waitForText("Codes Scanned: 1"));
        assertTrue(solo.waitForText("Total Score: 134"));
        assertFalse(text.equals(solo.clickInList(0).get(0).getText().toString())); //check that it has been removed from the list
    }

    @After
    public void tearDown() throws Exception{
        solo.finishOpenedActivities();
    }
}
