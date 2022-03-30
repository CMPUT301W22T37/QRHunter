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

public class QRSearchPageTest {
    private Solo solo;
    @Rule
    public ActivityTestRule<CreateAccount> rule =
            new ActivityTestRule<>(CreateAccount.class,true,true);
    @Before
    public void setUp() throws Exception{
        solo = new Solo(InstrumentationRegistry.getInstrumentation(),rule.getActivity());
    }

    @Test
    public void openSearch(){
        solo.assertCurrentActivity("Wrong Activity",CreateAccount.class);
        Random rand = new Random();
        int upperbound = 10000;
        int random_user = rand.nextInt(upperbound);
        solo.enterText((EditText) solo.getView(R.id.Username_EditText),"testUser" + random_user);
        solo.enterText((EditText) solo.getView(R.id.email_EditText),"test@gmail.com");
        solo.clickOnButton("Create Account");
        solo.assertCurrentActivity("Wrong Activity",MainMenu.class);
        View searchButton = solo.getCurrentActivity().findViewById(R.id.search_qr_button);
        solo.clickOnView(searchButton);

        solo.assertCurrentActivity("Wrong Activity",SearchQRPage.class);
        assertTrue(solo.waitForText("QR Codes Near Me"));
    }

    @After
    public void tearDown() throws Exception{
        solo.finishOpenedActivities();
    }
}
