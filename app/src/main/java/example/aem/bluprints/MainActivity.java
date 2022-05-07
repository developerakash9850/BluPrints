package example.aem.bluprints;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.app.TabActivity;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;

import java.io.PrintWriter;
import java.net.Socket;

import example.aem.bluprints.R;

public class MainActivity extends TabActivity {

    public Socket socketConnection;
    private String txtIP="";
    private PrintWriter printOut;
    TabHost tabHost;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tabHost = getTabHost();
        // Tab for Photos
        TabSpec wifiSpec = tabHost.newTabSpec("Wifi").setIndicator("WiFi");
        // setting Title and Icon for the Tab
      //  Intent wifiIntent = new Intent(this, WiFiActivity.class);
      //  wifiSpec.setContent(wifiIntent);

        // Tab for Songs
        TabSpec btspec = tabHost.newTabSpec("B.T.").setIndicator("B.T.");
        Intent btIntent = new Intent(this, BluetoothActivity.class);
        btspec.setContent(btIntent);

        // Tab for Videos
        TabSpec usbspec = tabHost.newTabSpec("USB").setIndicator("USB");
       // Intent usbIntent = new Intent(this, UsbActivity.class);
      //  usbspec.setContent(usbIntent);
        // Adding all TabSpec to TabHost
       // tabHost.addTab(wifiSpec); // Adding photos tab
        tabHost.addTab(btspec); // Adding songs tab
      //  tabHost.addTab(usbspec); // Adding videos tab
        tabHost.setOnTabChangedListener(new AnimatedTabHostListener(context,tabHost));
    }

    public void onSuccess(String response, Socket socket, String dstAddress, int dstPort, PrintWriter out)
    {
        txtIP = dstAddress;
        printOut = out;
        socketConnection = socket;
        Log.i("PrintWriter",out+"");
        Log.i("SocketOut", socket+"'");
        showAlert("AEMPrinter Connected"+" "+response);
    }
        
    public void onError(String response) {
        showAlert("Please Connect First... "+" "+response);
    }

    public void showAlert(String alertMsg)
    {
        AlertDialog.Builder alertBox = new AlertDialog.Builder(MainActivity.this);

        alertBox.setMessage(alertMsg).setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // TODO Auto-generated method stub
                        return;
                    }
                });

        AlertDialog alert = alertBox.create();
        alert.show();
    }
}
