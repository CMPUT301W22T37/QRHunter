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

public class StatsPageTest {
    private Solo solo;
    @Rule
    public ActivityTestRule<CreateAccount> rule =
            new ActivityTestRule<>(CreateAccount.class,true,true);
    @Before
    public void setUp() throws Exception{
        solo = new Solo(InstrumentationRegistry.getInstrumentation(),rule.getActivity());
    }


    @Test
    public void statsPageTest(){
        solo.assertCurrentActivity("Wrong Activity",CreateAccount.class);
        Random rand = new Random();
        int upperbound = 10000;
        int random_user = rand.nextInt(upperbound);
        solo.enterText((EditText) solo.getView(R.id.Username_EditText),"testUser" + random_user);
        solo.enterText((EditText) solo.getView(R.id.email_EditText),"test@gmail.com");
        solo.clickOnButton("Create Account");
        solo.assertCurrentActivity("Wrong Activity",MainMenu.class);
        solo.clickOnButton("STATS");
        solo.assertCurrentActivity("Wrong Activity",StatsPage.class);
        assertTrue(solo.waitForText("Highest Score: 134"));
        assertTrue(solo.waitForText("Lowest Score: 111"));
    }
    @Test
    public void updateStats(){
        solo.assertCurrentActivity("Wrong Activity",CreateAccount.class);
        Random rand = new Random();
        int upperbound = 10000;
        int random_user = rand.nextInt(upperbound);
        solo.enterText((EditText) solo.getView(R.id.Username_EditText),"testUser" + random_user);
        solo.enterText((EditText) solo.getView(R.id.email_EditText),"test@gmail.com");
        solo.clickOnButton("Create Account");
        solo.assertCurrentActivity("Wrong Activity",MainMenu.class);
        String text = solo.clickInList(0).get(0).getText().toString();
        solo.assertCurrentActivity("Wrong Activity",QrCodePage.class);

        solo.clickOnButton("Delete Code");
        solo.assertCurrentActivity("Wrong Activity",MainMenu.class);
        solo.clickOnButton("STATS");
        solo.assertCurrentActivity("Wrong Activity",StatsPage.class);
        assertTrue(solo.waitForText("Lowest Score: 134"));
        assertTrue(solo.waitForText("Highest Score: 134"));
    }


    @After
    public void tearDown() throws Exception{
        solo.finishOpenedActivities();
    }

}
