package com.hechuangwu.server.aidl;

import android.os.RemoteException;

/**
 * Created by cwh on 2019/3/27.
 * 功能:
 */
public class Addition extends IAdditionlInterface.Stub {
    @Override
    public int addition(int a, int b) throws RemoteException {
        return (a+b);
    }
}
