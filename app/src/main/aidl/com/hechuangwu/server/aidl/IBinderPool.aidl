// IBinderPool.aidl
package com.hechuangwu.server.aidl;

// Declare any non-default types here with import statements

interface IBinderPool {
    IBinder queryBinder(int type);
}
