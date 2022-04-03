package com.example.qrhunter;

import static org.junit.Assert.assertTrue;

import android.widget.EditText;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.robotium.solo.Solo;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.util.Random;

public class QRPageTest {
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
    public void clickSocial(){
        solo.assertCurrentActivity("Wrong Activity",MainMenu.class);
        // currently, we are initializing user with two qr codes
        String text = solo.clickInList(0).get(0).getText().toString();
        solo.assertCurrentActivity("Wrong Activity",QrCodePage.class);
        assertTrue(solo.waitForText(text));
        solo.clickOnView(solo.getView(R.id.social_button));
        solo.assertCurrentActivity("Wrong Activity",QRSocialPage.class);
        assertTrue(solo.waitForText(username));

    }
    @Test
    public void commentTest(){
        solo.assertCurrentActivity("Wrong Activity",MainMenu.class);
        // currently, we are initializing user with two qr codes
        String text = solo.clickInList(0).get(0).getText().toString();
        solo.assertCurrentActivity("Wrong Activity",QrCodePage.class);
        solo.enterText((EditText) solo.getView(R.id.comment_edit_text),username+" Comment");
        solo.clickOnButton("Add");
        assertTrue(solo.waitForText(username+ " Comment"));
    }

    @After
    public void tearDown() throws Exception{
        solo.finishOpenedActivities();
    }
}
