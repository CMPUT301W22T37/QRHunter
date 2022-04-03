package com.example.qrhunter;

import static org.junit.Assert.assertTrue;

import android.view.View;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.robotium.solo.Solo;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.util.Random;

public class OwnerPageTest {

    private Solo solo;
    private String username;
    private FloatingActionButton ownerButton;



    @Rule
    public ActivityTestRule<MainActivity> rule =
            new ActivityTestRule<>(MainActivity.class,true,true);
    @Before
    public void setUp() throws Exception{
        Random rand = new Random();
        int upperbound = 10000;
        int random_user = rand.nextInt(upperbound);
        username = "TestOwner"+random_user;
        User user = new TestUser(username, username+"@gmail.com");
        user.setOwner(true);

        solo = new Solo(InstrumentationRegistry.getInstrumentation(),rule.getActivity());
    }

    @Test
    public void OwnerTest() throws InterruptedException {
        solo.clickOnView(solo.getView(R.id.owner_button));
        solo.assertCurrentActivity("Wrong Activity",OwnerActivity.class);
        solo.clickOnView(solo.getView(R.id.codes_button));
        assertTrue(solo.getView(R.id.code_list_view).getVisibility() == View.VISIBLE);
        assertTrue(solo.getView(R.id.user_list_view).getVisibility() == View.GONE);
        solo.clickOnView(solo.getView(R.id.users_button));
        assertTrue(solo.getView(R.id.user_list_view).getVisibility() == View.VISIBLE);
        assertTrue(solo.getView(R.id.code_list_view).getVisibility() == View.GONE);
    }

    @After
    public void tearDown() throws Exception{
        solo.finishOpenedActivities();
    }
}
