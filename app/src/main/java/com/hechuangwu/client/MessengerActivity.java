package com.hechuangwu.client;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.view.View;

import com.hechuangwu.utils.Config;

/**
 * Created by cwh on 2019/3/18.
 * 功能:
 */
public class MessengerActivity extends Activity {
    private static final int MSG = 1001;
    private static final String MSG_DATA="msg_data";
    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_messenger );
    }
    private ServiceConnection mServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Messenger messenger = new Messenger( service );
            Message message = Message.obtain();
            message.what = MSG;
            Bundle bundle = new Bundle();
            bundle.putString( MSG_DATA,"hello messenger server" );
            message.setData( bundle );
            try {
                messenger.send( message );
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };
    public void bind(View view) {
        Intent intent = new Intent();
        intent.setPackage( Config.PACKAGE_NAME );
        intent.setAction( Config.SERVER_MESSENGER );
        bindService( intent,mServiceConnection,Context.BIND_AUTO_CREATE );
    }

    @Override
    protected void onDestroy() {
        unbindService( mServiceConnection );
        super.onDestroy();
    }
}
