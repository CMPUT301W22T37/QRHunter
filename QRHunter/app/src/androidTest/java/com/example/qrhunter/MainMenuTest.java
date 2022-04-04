package com.example.qrhunter;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import android.app.Activity;
import android.content.Context;
import android.graphics.Point;
import android.provider.Settings;
import android.util.Log;
import android.view.Display;
import android.view.View;
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
    public static void swipeLeftToDelete(Solo solo,String text){
        //adapted from https://stackoverflow.com/questions/24664730/writing-a-robotium-test-to-swipe-open-an-item-on-a-swipeable-listview
        // By C0D3LIC1OU5

        //and https://gist.github.com/mgasiorowski/9892682
        // by Maciej Gąsiorowski
        int fromX, toX, fromY, toY;
        int[] location = new int[2];
        View row = solo.getText(text);
        row.getLocationInWindow(location);

        // fail if the view with text cannot be located in the window
        if (location.length == 0) {
            assertTrue(false); //failed
        }
        fromX = location[0] + 100;
        fromY = location[1];

        toX = location[0];
        toY = fromY;
        solo.drag(fromX, toX, fromY, toY, 10);
        Display display = solo.getCurrentActivity().getWindowManager().getDefaultDisplay();
        int width = 0;
        if (android.os.Build.VERSION.SDK_INT <= 13) {
            width = display.getWidth();
        } else {
            Point size = new Point();
            display.getSize(size);
            width = size.x;
        }
        solo.clickOnScreen(width-10,fromY);
    }
    @Test
    public void deleteQR() {

        // currently, we are initializing user with two qr codes
        assertTrue(solo.waitForText("Codes Scanned: 2"));
        assertTrue(solo.waitForText("Total Score: 245"));
        swipeLeftToDelete(solo,"QR Code #1");

        solo.assertCurrentActivity("Wrong Activity",MainMenu.class);

        assertTrue(solo.waitForText("Codes Scanned: 1"));
        assertTrue(solo.waitForText("Total Score: 134"));
    }

    @After
    public void tearDown() throws Exception{
        solo.finishOpenedActivities();
    }
}
