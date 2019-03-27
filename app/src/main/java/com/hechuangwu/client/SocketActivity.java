package com.hechuangwu.client;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.hechuangwu.utils.Config;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by cwh on 2019/3/27.
 * 功能:
 */
public class SocketActivity extends Activity implements View.OnClickListener {
    public static SimpleDateFormat simpleDateFormat = new SimpleDateFormat( "MM/dd HH:mm:ss" );
    private final int CONNECT_SUCCESSFUL = 0;
    private final int RECEIVER_SUCCESSFUL = 1;
    private PrintWriter mPrintWriter;
    private Socket mServerSocket;
    private Button mBt_send;
    private TextView mTv_message;
    private EditText mEt_message;
    private Handler mHandler = new Handler( new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case CONNECT_SUCCESSFUL:
                    Toast.makeText( SocketActivity.this, (String) msg.obj, Toast.LENGTH_LONG ).show();
                    mBt_send.setEnabled( true );
                    break;
                case 1:
                    String serverMessage = "server (" + formatDateLong( System.currentTimeMillis() ) + "):\n" + msg.obj + "\n\n";
                    mTv_message.setText( mTv_message.getText() + "\n\n" + serverMessage );
                    break;
            }
            return false;
        }
    } );


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_socket );
        initView();
        initData();
    }

    private void initView() {
        mTv_message = findViewById( R.id.tv_message );
        mEt_message = findViewById( R.id.et_message );
        mBt_send = findViewById( R.id.bt_send );
        mBt_send.setOnClickListener( this );
    }

    private void initData() {
        Intent intent = new Intent();
        intent.setPackage( Config.PACKAGE_NAME );
        intent.setAction( Config.SERVER_SOCKET );
        startService( intent );
        new Thread( new Runnable() {
            @Override
            public void run() {
                connectServer();
            }
        } ).start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mServerSocket != null) {
            try {
                mServerSocket.shutdownInput();
                mServerSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public String formatDateLong(long longTime) {
        Date date = new Date( longTime );
        return simpleDateFormat.format( date );
    }

    private void connectServer() {
        while (mServerSocket == null) {
            try {
                mServerSocket = new Socket( "localhost", 8688 );
                mPrintWriter = new PrintWriter( new OutputStreamWriter( mServerSocket.getOutputStream() ), true );
                Message obtain = Message.obtain();
                obtain.what = 0;
                obtain.obj = "connect successful";
                mHandler.sendMessage( obtain );
            } catch (IOException e) {
                SystemClock.sleep( 1000 );
                e.printStackTrace();
            }
        }
        try {
            BufferedReader bufferedReader = new BufferedReader( new InputStreamReader( mServerSocket.getInputStream() ) );
            while (!this.isFinishing()) {
                String message = bufferedReader.readLine();
                if (!TextUtils.isEmpty( message )) {
                    Message obtain = Message.obtain();
                    obtain.what = RECEIVER_SUCCESSFUL;
                    obtain.obj = message;
                    mHandler.sendMessage( obtain );
                }
            }
            bufferedReader.close();
            mPrintWriter.close();
            mServerSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onClick(View v) {
        final String message = mEt_message.getText().toString().trim();
        if (!TextUtils.isEmpty( message ) && mPrintWriter != null) {
            new Thread( new Runnable() {
                @Override
                public void run() {
                    mPrintWriter.println( message );
                    runOnUiThread( new Runnable() {
                        @Override
                        public void run() {
                            mEt_message.setText( "" );
                            String clientMessage = "client (" + formatDateLong( System.currentTimeMillis() ) + "):\n" + message;
                            mTv_message.setText( mTv_message.getText() + clientMessage );
                        }
                    } );
                }
            } ).start();
        }
    }
}
