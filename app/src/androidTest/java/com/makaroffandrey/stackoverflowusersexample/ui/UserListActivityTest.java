package com.makaroffandrey.stackoverflowusersexample.ui;

import android.app.Activity;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.makaroffandrey.stackoverflowusersexample.R;
import com.makaroffandrey.stackoverflowusersexample.TestApplication;
import com.makaroffandrey.stackoverflowusersexample.repo.Resource;
import com.makaroffandrey.stackoverflowusersexample.repo.User;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;

import dagger.android.AndroidInjector;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.doesNotExist;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withEffectiveVisibility;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.when;

@RunWith(AndroidJUnit4.class)
public class UserListActivityTest {

    private UsersListViewModel viewModel;

    private MutableLiveData<Resource<List<User>>> liveData = new MutableLiveData<>();

    private AndroidInjector<Activity> activityInjector = Mockito.mock(AndroidInjector.class);

    @Rule
    public ActivityTestRule<UserListActivity> activityRule = new
            ActivityTestRule<UserListActivity>(UserListActivity.class) {

                @Override
                protected void beforeActivityLaunched() {
                    viewModel = Mockito.mock(UsersListViewModel.class);

                    doAnswer(invocation -> {
                        ((UserListActivity) invocation.getArgument(0)).viewModelFactory = new
                                ViewModelProvider.Factory() {
                                    @NonNull
                                    @Override
                                    public <T extends ViewModel> T create(@NonNull Class<T>
                                                                                  modelClass) {
                                        if (modelClass.isAssignableFrom(UsersListViewModel.class)) {
                                            return (T) viewModel;
                                        }
                                        throw new IllegalArgumentException("Incorrect ViewModel " +
                                                "was " +
                                                "requested" +
                                                " during " +
                                                "testing");
                                    }
                                };
                        return null;
                    }).when(activityInjector).inject(any(UserListActivity.class));
                    ((TestApplication) InstrumentationRegistry.getTargetContext()
                            .getApplicationContext()).activityInjector = activityInjector;
                    when(viewModel.getUsers()).thenReturn(liveData);
                }
            };

    @Before
    public void setUp() {

    }

    @Test
    public void test() throws InterruptedException {
        User user = new User();
        user.display_name = "John Smith";
        user.reputation = 100500;
        liveData.postValue(Resource.loading(null));
        onView(withText(R.string.empty)).check(matches(isDisplayed()));
        onView(withText(R.string.load_error)).check(doesNotExist());
        liveData.postValue(Resource.error(new Exception(), null));
        onView(withText(R.string.empty)).check(matches(isDisplayed()));
        onView(withText(R.string.load_error)).check(matches(withEffectiveVisibility(ViewMatchers
                .Visibility.VISIBLE)));
        onView(withText(user.display_name)).check(doesNotExist());
        ArrayList<User> users = new ArrayList<>();
        users.add(user);
        liveData.postValue(Resource.success(users));
        onView(withText(R.string.empty)).check(doesNotExist());
        onView(withText(R.string.load_error)).check(doesNotExist());
        onView(withText(user.display_name)).check(matches(isDisplayed()));
        liveData.postValue(Resource.error(new Exception(), users));
        onView(withText(R.string.empty)).check(doesNotExist());
        onView(withText(R.string.load_error)).check(matches(withEffectiveVisibility(ViewMatchers
                .Visibility.VISIBLE)));
        onView(withText(user.display_name)).check(matches(isDisplayed()));

    }
}
