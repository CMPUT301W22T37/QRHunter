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

public class CreateAccountText {
    private Solo solo;
    @Rule
    public ActivityTestRule<CreateAccount> rule =
            new ActivityTestRule<>(CreateAccount.class,true,true);
    @Before
    public void setUp() throws Exception{
        solo = new Solo(InstrumentationRegistry.getInstrumentation(),rule.getActivity());
    }

    @Test
    public void start() throws Exception{
        Activity activity = rule.getActivity();
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
    public void ensureUnique(){
        solo.assertCurrentActivity("Wrong Activity",CreateAccount.class);
        solo.enterText((EditText) solo.getView(R.id.Username_EditText),"sarah" );
        solo.enterText((EditText) solo.getView(R.id.email_EditText),"test@gmail.com");
        solo.clickOnButton("Create Account");
        solo.assertCurrentActivity("Wrong Activity",CreateAccount.class);
        assertTrue(solo.waitForText("Username is not valid, please select another"));
    }
    @Test
    public void ensureInformation(){
        solo.assertCurrentActivity("Wrong Activity",CreateAccount.class);
        solo.enterText((EditText) solo.getView(R.id.Username_EditText),"" );
        solo.enterText((EditText) solo.getView(R.id.email_EditText),"test@gmail.com");
        solo.clickOnButton("Create Account");
        solo.assertCurrentActivity("Wrong Activity",CreateAccount.class);
        assertTrue(solo.waitForText("Must Enter Email and Username"));
        solo.enterText((EditText) solo.getView(R.id.Username_EditText),"testName" );
        solo.enterText((EditText) solo.getView(R.id.email_EditText),"");
        solo.clickOnButton("Create Account");
        solo.assertCurrentActivity("Wrong Activity",CreateAccount.class);
        assertTrue(solo.waitForText("Must Enter Email and Username"));
    }

    @After
    public void tearDown() throws Exception{
        solo.finishOpenedActivities();
    }
}
