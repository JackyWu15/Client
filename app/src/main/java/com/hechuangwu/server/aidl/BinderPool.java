package com.hechuangwu.server.aidl;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;

import java.util.concurrent.CountDownLatch;

/**
 * Created by cwh on 2019/3/27.
 * 功能:
 */
public class BinderPool {
    private static volatile BinderPool mBinderPool = null;
    public static final int BINDER_ADD = 0;
    public static final int BINDER_SUB = 1;
    private Context mContext;
    private IBinderPool mIBinderPool;
    private CountDownLatch mCountDownLatch;

    private BinderPool(Context context) {
        this.mContext = context.getApplicationContext();
        bindBinderPoolServer();
    }
    public static BinderPool getInstance(Context context) {
        if (mBinderPool == null) {
            synchronized (BinderPool.class) {
                if (mBinderPool == null) {
                    mBinderPool = new BinderPool( context );
                    return mBinderPool;
                }
            }
        }
        return null;
    }
    private synchronized void bindBinderPoolServer() {
        mCountDownLatch = new CountDownLatch( 1 );
        Intent intent = new Intent();
        intent.setPackage( "com.hechuangwu.server" );
        intent.setAction( "com.hechuangwu.server.binderpool" );
        mContext.bindService( intent,mBinderPoolConnect,Context.BIND_AUTO_CREATE );
        try {
            mCountDownLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public IBinder queryBinder(int type) throws RemoteException {
        IBinder iBinder = mIBinderPool.queryBinder( type );
        return iBinder;
    }

    private ServiceConnection  mBinderPoolConnect = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mIBinderPool = IBinderPool.Stub.asInterface( service );
            try {
                mIBinderPool.asBinder().linkToDeath( mDeathRecipient,0 );
            } catch (RemoteException e) {
                e.printStackTrace();
            }finally {
                mCountDownLatch.countDown();
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };

    private IBinder.DeathRecipient mDeathRecipient = new IBinder.DeathRecipient() {
        @Override
        public void binderDied() {
            mIBinderPool.asBinder().unlinkToDeath( mDeathRecipient,0 );
            mBinderPool = null;
            bindBinderPoolServer();
        }
    };

}
