package com.example.android.popularmovies;

import android.content.Context;

import androidx.room.Room;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.example.android.popularmovies.database.AppDatabase;
import com.example.android.popularmovies.database.FavouriteDao;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;

@RunWith(AndroidJUnit4.class)
public class DatabaseCreationTest {

/*    private FavouriteDao favouriteDao;
    private AppDatabase db;

    @Before
    public void createDb() {
        Context context = ApplicationProvider.getApplicationContext();
        db = Room.inMemoryDatabaseBuilder(context, AppDatabase.class).build();
        favouriteDao = db.favouriteDao();
    }

    @After
    public void closeDb() throws IOException {
        db.close();
    }

    @Test
    public void writeFavouriteAndReadInList() throws Exception {
            List<User> byName = userDao.findUsersByName("george");
                    assertThat(byName.get(0), equalTo(user));

        FavouriteEntry favourite = new FavouriteEntry(123, "tytul", "www.google.com");



        favouriteDao.insertFavourite(favourite);

        LiveData<List<FavouriteEntry>> liveData = favouriteDao.loadAllFavourites();



        List<FavouriteEntry> queriedList = liveData.getValue();

        if (queriedList != null) {
            assertEquals(liveData.getValue().get(0), favourite);
        } else {
            fail();
        }

    }*/
}
