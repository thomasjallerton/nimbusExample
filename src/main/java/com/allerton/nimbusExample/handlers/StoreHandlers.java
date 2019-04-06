package com.allerton.nimbusExample.handlers;

import annotation.annotations.function.DocumentStoreServerlessFunction;
import annotation.annotations.persistent.StoreEventType;
import com.allerton.nimbusExample.models.UserDetail;

public class StoreHandlers {

    @DocumentStoreServerlessFunction(
            dataModel = UserDetail.class,
            method = StoreEventType.INSERT
    )
    public void newItem(UserDetail newItem) {
        System.out.println("New user was created! " + newItem);
    }
}
