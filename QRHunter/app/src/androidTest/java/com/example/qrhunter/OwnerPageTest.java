package com.example.qrhunter;

import static org.junit.Assert.assertTrue;

import android.graphics.Point;
import android.util.Log;
import android.view.Display;
import android.view.View;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.FirebaseFirestore;
import com.robotium.solo.Solo;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.util.Random;

public class OwnerPageTest {

    private Solo solo;
    private FirebaseFirestore db;
    private String username;
    private FloatingActionButton ownerButton;
    private User deleteUser;



    @Rule
    public ActivityTestRule<MainActivity> rule =
            new ActivityTestRule<>(MainActivity.class,true,true);
    @Before
    public void setUp() throws Exception{
        db = FirebaseFirestore.getInstance();
        Random rand = new Random();
        int upperbound = 10000;
        int random_user = rand.nextInt(upperbound);
        username = "TestOwner"+random_user;
        User user = new TestUser(username, username+"@gmail.com");
        deleteUser = new TestUser("DeleteThisBoi", "DeleteThisBoi@gmail.com");

        solo = new Solo(InstrumentationRegistry.getInstrumentation(),rule.getActivity());
    }

    @Test
    public void OwnerTest() throws InterruptedException {
        solo.clickOnView(solo.getView(R.id.owner_button));
        solo.assertCurrentActivity("Wrong Activity",OwnerActivity.class);

        solo.clickOnView(solo.getView(R.id.codes_button));
        assertTrue(solo.getView(R.id.code_list_view).getVisibility() == View.VISIBLE);
        assertTrue(solo.getView(R.id.user_list_view).getVisibility() == View.GONE);

        String deleteCode = deleteUser.getCode(0).getUniqueHash();
        swipeLeftToDelete(solo,deleteCode);
        assertTrue(db.collection("QRCodes").document(deleteCode).get() == null);

        solo.clickOnView(solo.getView(R.id.users_button));
        assertTrue(solo.getView(R.id.user_list_view).getVisibility() == View.VISIBLE);
        assertTrue(solo.getView(R.id.code_list_view).getVisibility() == View.GONE);

        swipeLeftToDelete(solo,"*DeleteThisBoi");
        assertTrue(db.collection("Users").document("DeleteThisBoi").get() == null);
    }

    public void swipeLeftToDelete(Solo solo,String text){
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

    @After
    public void tearDown() throws Exception{
        solo.finishOpenedActivities();
    }
}
