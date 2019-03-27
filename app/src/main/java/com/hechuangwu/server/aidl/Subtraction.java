package com.hechuangwu.server.aidl;

import android.os.RemoteException;

/**
 * Created by cwh on 2019/3/27.
 * 功能:
 */
public class Subtraction extends ISubtractionInterface.Stub {
    @Override
    public int subtraction(int a, int b) throws RemoteException {
        return (a-b);
    }
}
