package com.hechuangwu.client;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.util.Log;
import android.view.View;

import com.hechuangwu.utils.Config;

/**
 * Created by cwh on 2019/3/18.
 * 功能:
 */
public class MessengerActivity extends Activity {
    private static final int MSG_RECEIVER = 1001;
    private static final int MSG_REPLY= 1002;
    private static final String MSG_RECEIVER_DATA="msg_receiver_data";
    private static final String MSG_REPLY_DATA="msg_reply_data";
    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_messenger );
    }
    private static Messenger mMessenger = new Messenger( new MessengerHandler() );
    private static class MessengerHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage( msg );
            switch (msg.what){
                case MSG_REPLY:
                    Bundle data = msg.getData();
                    String msgData = data.getString( MSG_REPLY_DATA );
                    Log.i( "data", "handleMessage: >>>reply>>>"+msgData );
                    break;
            }
        }
    }

    private ServiceConnection mServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Messenger messenger = new Messenger( service );
            Message message = Message.obtain();
            message.what = MSG_RECEIVER;
            Bundle bundle = new Bundle();
            bundle.putString( MSG_RECEIVER_DATA,"hello messenger server" );
            message.setData( bundle );
            message.replyTo = mMessenger;
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

    public void back(View view) {
        finish();
    }
}
