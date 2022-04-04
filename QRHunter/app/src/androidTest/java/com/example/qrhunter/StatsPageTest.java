package com.example.qrhunter;

import static org.junit.Assert.assertTrue;

import android.view.View;
import android.widget.EditText;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.robotium.solo.Solo;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.util.Random;

public class StatsPageTest {
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
    public void statsPageTest(){

        solo.assertCurrentActivity("Wrong Activity",MainMenu.class);
        solo.clickOnView(solo.getView(R.id.stats_icon));
        solo.assertCurrentActivity("Wrong Activity",StatsPage.class);
        assertTrue(solo.waitForText("Highest Score: 134"));
        assertTrue(solo.waitForText("Lowest Score: 111"));
    }
    @Test
    public void updateStats(){

        solo.assertCurrentActivity("Wrong Activity",MainMenu.class);
        MainMenuTest.swipeLeftToDelete(solo,"QR Code #1");
        solo.assertCurrentActivity("Wrong Activity",MainMenu.class);
        solo.clickOnView(solo.getView(R.id.stats_icon));
        solo.assertCurrentActivity("Wrong Activity",StatsPage.class);
        assertTrue(solo.waitForText("Lowest Score: 134"));
        assertTrue(solo.waitForText("Highest Score: 134"));
    }


    @After
    public void tearDown() throws Exception{
        solo.finishOpenedActivities();
    }

}
