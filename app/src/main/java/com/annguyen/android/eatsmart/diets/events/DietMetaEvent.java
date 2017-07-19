package com.annguyen.android.eatsmart.diets.events;

import com.annguyen.android.eatsmart.entities.Diet;

import java.util.List;

/**
 * Created by annguyen on 6/27/2017.
 */

public class DietMetaEvent {
    public static final int DIET_META_SUCCESS = 101;
    public static final int DIET_META_FAIL = 404;

    private int eventCode;
    private String errMsg;
    private Diet newDiet;


    public DietMetaEvent(int eventCode, String errMsg, Diet newDiet) {
        this.eventCode = eventCode;
        this.errMsg = errMsg;
        this.newDiet = newDiet;
    }

    public int getEventCode() {
        return eventCode;
    }

    public String getErrMsg() {
        return errMsg;
    }

    public Diet getNewDiet() {
        return newDiet;
    }
}
