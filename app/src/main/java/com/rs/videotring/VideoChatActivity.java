package com.rs.videotring;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.util.Log;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.opentok.android.OpentokError;
import com.opentok.android.Publisher;
import com.opentok.android.PublisherKit;
import com.opentok.android.Session;
import com.opentok.android.Stream;
import com.opentok.android.Subscriber;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

public class VideoChatActivity extends AppCompatActivity  implements Session.SessionListener,
        PublisherKit.PublisherListener {


    public static String API_KEY = "46613642";

   /* public static String SESSION_ID = "2_MX40NjYxMzY0Mn5-MTU4NTIyMjk5ODI4NH5kUjNiaHJzRWJjblptRC83NHRDY2xadDd-fg";
    public static String TOKEN = "T1==cGFydG5lcl9pZD00NjYxMzY0MiZzaWc9MWYwMTUxZTYyY2NjYjg4YjFhYzYwOTU5N2M0ZjExMGRhNTkyYzViNjpzZXNzaW9uX2lkPTJfTVg0ME5qWXhNelkwTW41LU1UVTROVEl5TWprNU9ESTROSDVrVWpOaWFISnpSV0pqYmxwdFJDODNOSFJEWTJ4YWREZC1mZyZjcmVhdGVfdGltZT0xNTg1MjIzMDc0Jm5vbmNlPTAuOTg4MTk1OTMyNzE4OTY0MiZyb2xlPXB1Ymxpc2hlciZleHBpcmVfdGltZT0xNTg3ODE1MDcwJmluaXRpYWxfbGF5b3V0X2NsYXNzX2xpc3Q9";
    */

    public static String SESSION_ID = "2_MX40NjYxMzY0Mn5-MTU4ODM5NTYyNDAwOH5NRmZsc0VmMVdTdmxHZ2pvWEhXVjRPc2R-fg";
    public static String TOKEN = "T1==cGFydG5lcl9pZD00NjYxMzY0MiZzaWc9ZDI3NDc0OTM3NGJmMGJmOWUwY2I2YWI5YzI0MDY2MGM5ZDEzYzUwOTpzZXNzaW9uX2lkPTJfTVg0ME5qWXhNelkwTW41LU1UVTRPRE01TlRZeU5EQXdPSDVOUm1ac2MwVm1NVmRUZG14SFoycHZXRWhYVmpSUGMyUi1mZyZjcmVhdGVfdGltZT0xNTg4Mzk1NzQzJm5vbmNlPTAuMTA5MzQ1NTExNjE2NTQ1ODImcm9sZT1wdWJsaXNoZXImZXhwaXJlX3RpbWU9MTU5MDk4NzY5MyZpbml0aWFsX2xheW91dF9jbGFzc19saXN0PQ==";

    public static final String LOG_TAG = VideoChatActivity.class.getSimpleName();
    public static final int RC_VIDEO_APP_PERM = 124;

    FrameLayout mPublisherVIewController;
    FrameLayout mSubscriberVIewController;
    Session mSession;
    Publisher mPublisher;
    Subscriber mSubscriber;

    ImageView closeVideoChatBtn;
   /* DatabaseReference userRef;
    String userID = "";*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_chat);

        closeVideoChatBtn  = findViewById(R.id.close_video_chat_btn);

        RequestPermissions();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        EasyPermissions.onRequestPermissionsResult(requestCode,permissions,grantResults, VideoChatActivity.this);

    }


    @AfterPermissionGranted(RC_VIDEO_APP_PERM)
    private void RequestPermissions(){

        String[] perms = {Manifest.permission.INTERNET, Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO};

        if (EasyPermissions.hasPermissions(this,perms)){

            mPublisherVIewController = findViewById(R.id.publisher_container);
            mSubscriberVIewController = findViewById(R.id.subscriber_container);

            // 1. Initialze and connect to the session

            mSession = new Session.Builder(this,API_KEY, SESSION_ID).build();
            mSession.setSessionListener(VideoChatActivity.this);
            mSession.connect(TOKEN);
        }
        else {

            EasyPermissions.requestPermissions(this,"This app needs Camera and Mic, Please allow.",RC_VIDEO_APP_PERM,perms);
        }

    }

    @Override
    public void onStreamCreated(PublisherKit publisherKit, Stream stream) {

    }

    @Override
    public void onStreamDestroyed(PublisherKit publisherKit, Stream stream) {

    }

    @Override
    public void onError(PublisherKit publisherKit, OpentokError opentokError) {

    }

    // 2. Publishing the stream to the session
    @Override
    public void onConnected(Session session) {

        Log.i(LOG_TAG, "Session Connected");

        mPublisher = new Publisher.Builder(this).build();
        mPublisher.setPublisherListener(VideoChatActivity.this);

        mPublisherVIewController.addView(mPublisher.getView());

        if (mPublisher.getView() instanceof GLSurfaceView){

            ((GLSurfaceView) mPublisher.getView()).setZOrderOnTop(true);
        }

        mSession.publish(mPublisher);
    }

    @Override
    public void onDisconnected(Session session) {

        Log.i(LOG_TAG, "Stream Disconnected");
    }


    // 3. Subscribing to the stream received
    @Override
    public void onStreamReceived(Session session, Stream stream) {

        Log.i(LOG_TAG, "Stream received");

        if (mSubscriber == null){

            mSubscriber = new Subscriber.Builder(this,stream).build();
            mSession.subscribe(mSubscriber);
            mSubscriberVIewController.addView(mSubscriber.getView());
        }
    }

    @Override
    public void onStreamDropped(Session session, Stream stream) {
        Log.i(LOG_TAG, "Stream Dropped");

        if (mSubscriber !=  null){

            mSubscriber = null;
            mSubscriberVIewController.removeAllViews();
        }
    }

    @Override
    public void onError(Session session, OpentokError opentokError) {
        Log.i(LOG_TAG, "Stream Error");
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

}
