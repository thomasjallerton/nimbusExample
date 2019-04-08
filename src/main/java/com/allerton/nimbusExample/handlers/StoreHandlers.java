package com.allerton.nimbusExample.handlers;

import com.allerton.nimbusExample.models.UserDetail;
import com.nimbusframework.nimbuscore.annotation.annotations.function.DocumentStoreServerlessFunction;
import com.nimbusframework.nimbuscore.annotation.annotations.persistent.StoreEventType;

public class StoreHandlers {

    @DocumentStoreServerlessFunction(
            dataModel = UserDetail.class,
            method = StoreEventType.INSERT
    )
    public void newItem(UserDetail newItem) {
        System.out.println("New user was created! " + newItem);
    }
}
