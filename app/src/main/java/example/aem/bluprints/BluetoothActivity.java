package example.aem.bluprints;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.Writer;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.oned.Code39Writer;
import com.google.zxing.qrcode.QRCodeWriter;
import java.io.File;
import java.io.FileDescriptor;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import example.aem.bluprints.AEMPrinter.BARCODE_HEIGHT;
import example.aem.bluprints.AEMPrinter.BARCODE_TYPE;
import example.aem.bluprints.CardReader.CARD_TRACK;
import static example.aem.bluprints.AEMPrinter.ESCAPE_CENTER;
import static example.aem.bluprints.AEMPrinter.ESCAPE_DOUBLE_HEIGHT;
import static example.aem.bluprints.AEMPrinter.ESCAPE_EXCL;
import static example.aem.bluprints.AEMPrinter.ESCAPE_SEQ;
import static example.aem.bluprints.AEMPrinter.ESCAPE_a;


public class BluetoothActivity extends AppCompatActivity implements IAemCardScanner, IAemScrybe{

    AEMScrybeDevice m_AemScrybeDevice;
    static final int REQUEST_IMAGE_CAPTURE = 1;
    CardReader m_cardReader = null;
    AEMPrinter m_AEM_Printer = null;
    ArrayList<String> printerList;
    CARD_TRACK cardTrackType;
    int glbPrinterWidth;
    MyClient mcl = null;
    EditText editText;
    private PrintWriter printOut;
    Socket socketConnection;
    Spinner spinner;
    private Thread thread = null;
    String responseString,data, txtIP="",creditData;
    private static final int REFRESH = 0;
    char[] batteryStatusCommand=new char[]{0x1B,0x7E,0x42,0x50,0x7C,0x47,0x45,0x54,0x7C,0x42,0x41,0x54,0x5F,0x53,0x54,0x5E};
    char[] offCommand=new char[]{0x1B,0x7E,0x42,0x50,0x7C,0x50,0x52,0x4E,0x7C,0x50,0x57,0x52,0x4F,0x46,0x46,0x5E};
    char[] end=new char[]{0x1B,0x7E,0x42,0x50,0x7C,0x47,0x45,0x54,0x7C,0x45,0x4E,0x44,0x5F,0x50,0x52,0x5E};
    char[] start=new char[]{0x1B,0x7E,0x42,0x50,0x7C,0x47,0x45,0x54,0x7C,0x53,0x54,0x41,0x52,0x54,0x50,0x5E};
    char[] paperCommand=new char[]{0x1B,0x7E,0x42,0x50,0x7C,0x47,0x45,0x54,0x7C,0x50,0x41,0x5F,0x53,0x54,0x53,0x5E};
    char[] printerStatus=new char[]{0x1B,0x7E,0x42,0x50,0x7C,0x47,0x45,0x54,0x7C,0x50,0x52,0x4E,0x5F,0x53,0x54,0x5E};
    char[] printerConfiguration=new char[]{0x1B,0x7E,0x42,0x50,0x7C,0x47,0x45,0x54,0x7C,0x43,0x4F,0x4E,0x46,0x49,0x47,0x5E};
    char[] printerVersion=new char[]{0x1B,0x7E,0x42,0x50,0x7C,0x47,0x45,0x54,0x7C,0x50,0x52,0x4E,0x56,0x45,0x52,0x5E};
    char[] testLed=new char[]{0x1B,0x7E,0x42,0x50,0x7C,0x54,0x53,0x54,0x54,0x53,0x54,0x4c,0x45,0x44,0x5E};
    char[] testPrint=new char[]{0x1B,0x7E,0x42,0x50,0x7C,0x54,0x53,0x54,0x7c,0x50,0x52,0x5F,0x49,0x4E,0x54,0x5E};
    char[] printCount=new char[]{0x1B,0x7E,0x42,0x50,0x7C,0x54,0x53,0x54,0x7c,0x50,0x52,0x5F,0x43,0x4E,0x54,0x5E};
    char[] labelEnd=new char[]{0x1B,0x69};
    TextView txtBatteryStatus;
    public Handler mHandler;
    String[] responseArray = new String[1];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bluetooth);
        printerList = new ArrayList<String>();
        creditData = new String();
        editText = (EditText) findViewById(R.id.edittext);
        txtBatteryStatus=(TextView) findViewById(R.id.txtBatteryStatus);

        spinner=(Spinner)findViewById(R.id.spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,R.array.printer_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view,int position, long id) {
                if(position==1) {
                    glbPrinterWidth=48;
                    onSetPrinterType(view);
                } else{
                    glbPrinterWidth=32;
                    onSetPrinterType(view);
                }
            }
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub

            }
        });
        // btnSetPrnType = (Button) findViewById(R.id.printerType);
        // btnSetPrnType.setText("Set 3 Inch AEMPrinter");
        m_AemScrybeDevice = new AEMScrybeDevice(this);
        Button discoverButton = (Button) findViewById(R.id.pairing);
        registerForContextMenu(discoverButton);
    }
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.setHeaderTitle("Select AEMPrinter to connect");
        for (int i = 0; i < printerList.size(); i++) {
            menu.add(0, v.getId(), 0, printerList.get(i));
        }
    }
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        super.onContextItemSelected(item);
        String printerName = item.getTitle().toString();
        try {
            m_AemScrybeDevice.connectToPrinter(printerName);
            m_cardReader = m_AemScrybeDevice.getCardReader(this);
            m_AEM_Printer = m_AemScrybeDevice.getAemPrinter();
            Toast.makeText(BluetoothActivity.this,"Connected with " + printerName,Toast.LENGTH_SHORT ).show();
          //  String data=new String(batteryStatusCommand);
          //  m_AEM_Printer.print(data);
          //  m_cardReader.readMSR();
        } catch (IOException e) {
            if (e.getMessage().contains("Service discovery failed")) {
                Toast.makeText(BluetoothActivity.this,"Not Connected\n"+ printerName + " is unreachable or off otherwise it is connected with other device",Toast.LENGTH_SHORT ).show();
            }
            else {
                Toast.makeText(BluetoothActivity.this,"Unable to connect",Toast.LENGTH_SHORT ).show();
            }
        }
        return true;
    }
    @Override
    protected void onDestroy()
    {
        if (m_AemScrybeDevice != null)
        {
            try
            {
                m_AemScrybeDevice.disConnectPrinter();
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
        super.onDestroy();
    }
    public void onShowPairedPrinters(View v) {
        printerList = m_AemScrybeDevice.getPairedPrinterByName();
        if (printerList.size() > 0)
            openContextMenu(v);
        else
            showAlert("No Paired Printers found");
    }
    public void onDisconnectDevice(View v) {
        if (m_AemScrybeDevice != null) {
            try {
                m_AemScrybeDevice.disConnectPrinter();
                Toast.makeText(BluetoothActivity.this, "disconnected", Toast.LENGTH_SHORT).show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void onPrintHindiBillBluetooth(int numChars)
    {
        String data = "";
        String d = "";
        if(numChars == 32) {
            data = "     दो ईन्च प्रिन्टर:टेस्ट प्रिन्ट             ";
            d =    "________________________________";
            try {
                m_AEM_Printer.setFontType(AEMPrinter.DOUBLE_HEIGHT);
                m_AEM_Printer.PrintHindi(data);
                m_AEM_Printer.print(d);
                data = "कोड। विवरण | रेट । मात्रा । राशि(रू)";
                m_AEM_Printer.PrintHindi(data);
                m_AEM_Printer.print(d);
                data = "12।कोलगेट    ।70.00 । 2 । 140.00\n"+
                        "22।मैगी सूप   ।25.00 । 2 । 50.00\n"+
                        "32।लक्स सोप  ।45.00 । 2 । 90.00\n"+
                        "42।डाबर हनी ।60.00 । 1 । 120.00\n"+
                        "62।मैगीनूडल   ।10.00 । 9 । 90.00\n";
                //   "___________________________";
                m_AEM_Printer.PrintHindi(data);
                m_AEM_Printer.print(d);
                data="	टोटल राशि (रू):          590.00";

                m_AEM_Printer.setFontType(AEMPrinter.DOUBLE_HEIGHT);
                m_AEM_Printer.PrintHindi(data);
                m_AEM_Printer.print(d);
                data="      	धन्यवाद                      ";
                m_AEM_Printer.setFontType(AEMPrinter.DOUBLE_WIDTH);
                m_AEM_Printer.PrintHindi(data);

                data="	       आपका दिन मंगलमय हो             ";

                m_AEM_Printer.setFontType(AEMPrinter.DOUBLE_HEIGHT);
                m_AEM_Printer.PrintHindi(data);

            }
            catch (IOException e)
            {
                if (e.getMessage().contains("socket closed"))
                    showAlert("AEMPrinter not connected");
            }
        }

        else
        {
            data = "         तीन इन्च प्रिन्टर: टेस्ट प्रिन्ट            \n";
            d =    "________________________________________________\n";

            try
            {
                m_AEM_Printer.setFontType(AEMPrinter.DOUBLE_HEIGHT);
                m_AEM_Printer.PrintHindi(data);
                m_AEM_Printer.print(d);
                data = " कोड |  विवरण     | रेट  ।  मात्रा  ।     राशि(रू) ";
                m_AEM_Printer.PrintHindi(data);
                m_AEM_Printer.print(d);
                data =  " 12 ।  कोलगेट     ।  70.00  ।  2   ।    140.00\n"+
                        " 22 ।  मैगी सूप    ।  25.00  ।  2   ।     50.00\n"+
                        " 32 ।  लक्स सोप   ।  45.00  ।  2   ।     90.00\n"+
                        " 42 ।  डाबर हनी  ।  60.00  ।  1   ।    120.00\n"+
                        " 62 ।  मैगीनूडल    ।  10.00  ।  9   ।     90.00\n";
                m_AEM_Printer.PrintHindi(data);
                m_AEM_Printer.print(d);
                data = "             टोटल राशि (रू):   590.00\n";

                m_AEM_Printer.setFontType(AEMPrinter.DOUBLE_HEIGHT);
                m_AEM_Printer.PrintHindi(data);
                m_AEM_Printer.print(d);

                data = "\n                धन्यवाद                         ";
                //m_AEM_Printer.setFontType(AEMPrinter.DOUBLE_WIDTH);
                m_AEM_Printer.setFontType(AEMPrinter.DOUBLE_HEIGHT);
                m_AEM_Printer.PrintHindi(data);

                data="             आपका दिन मंगलमय हो                     ";
                //m_AEM_Printer.setFontType(AEMPrinter.DOUBLE_WIDTH);
                m_AEM_Printer.setFontType(AEMPrinter.DOUBLE_HEIGHT);
                m_AEM_Printer.PrintHindi(data);

            }
            catch (IOException e)
            {
                if (e.getMessage().contains("socket closed"))
                    showAlert("AEMPrinter not connected");
            }

        }
    }

    public void onPrintHindiBill(View v) {
        if (m_AEM_Printer == null) {
            Toast.makeText(BluetoothActivity.this, "AEMPrinter not connected", Toast.LENGTH_SHORT).show();
            return;
        }
        int numChars = glbPrinterWidth;//CheckPrinterWidth();
        Toast.makeText(BluetoothActivity.this, "Printing " + numChars + " Character/Line Bill", Toast.LENGTH_SHORT).show();
        onPrintHindiBillBluetooth(numChars);
    }
    public void onSetPrinterType(View v) {
        if(glbPrinterWidth == 32) {
            glbPrinterWidth = 32;
            //showAlert("32 Characters / Line or 2 Inch (58mm) AEMPrinter Selected!");
        } else {
            glbPrinterWidth = 48;
          //  showAlert("48 Characters / Line or 3 Inch (80mm) AEMPrinter Selected!");
        }
    }
    public void onPrintBill(View v) throws IOException {
        int numChars = glbPrinterWidth;//CheckPrinterWidth();
        Toast.makeText(BluetoothActivity.this, "Printing " + numChars + " Character/Line Bill", Toast.LENGTH_SHORT).show();
        onPrintBillBluetooth(numChars);
    }


    @Override
    public void onScanPacket(String buffer) {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append(buffer);
        String temp = "";
        try {
            temp = stringBuffer.toString();
        }
        catch (Exception e)
        {
            // TODO: handle exception
        }
        data = temp;
        final String strData=data.replace("|","&");
        //Log.e("BufferData",data);
        String[] formattedData=strData.split("&",3);
       // Log.e("Response Data",formattedData[2]);
        String responseString =formattedData[2];
        responseArray[0]=responseString.replace("^","");
        Log.e("Response Array",responseArray[0]);
        BluetoothActivity.this.runOnUiThread(new Runnable() {
            public void run() {
                String replacedData=data.replace("|","&");
                String[] formattedData=replacedData.split("&",3);
                //Log.e("Response Data",formattedData[2]);
                // response=formattedData[2];

                 String response=formattedData[2];
               //  Log.e("Response",response);
                 if(response.contains("BAT")){
                     txtBatteryStatus.setText(response.replace("^","").replace("BAT","")+"%");
                 }
               //  Log.e("Edited Response : ",response.replace("^",""));
                 editText.setText(response.replace("^",""));
                /* if(response.contains("NOPAPER")){
                     try {
                         m_AemScrybeDevice.disConnectPrinter();
                     } catch (IOException e) {
                         e.printStackTrace();
                     }
                 }*/
            }
        });

       // thread = new MyThread();
       // thread.start();

    }

    private void sendMsg(){
        Message msg = new Message();
        msg.what = REFRESH;
        mHandler.sendMessage(msg);

    }

    public void onPrintBillBluetooth(int numChars) throws IOException {
        if (m_AEM_Printer == null) {
            Toast.makeText(BluetoothActivity.this, "AEMPrinter not connected", Toast.LENGTH_SHORT).show();
            return;
        }
        String data = "TWO INCH PRINTER: TEST PRINT \n";
        String d =    "_________________________________\n";

        try {
            if(numChars == 32) {
                m_AEM_Printer.setFontType(AEMPrinter.FONT_NORMAL);
                m_AEM_Printer.setFontType(AEMPrinter.TEXT_ALIGNMENT_CENTER);
                m_AEM_Printer.print(data);
                m_AEM_Printer.print(d);
                data = "CODE|DESC|RATE(Rs)|QTY |AMT(Rs)\n";
                m_AEM_Printer.POS_FontCENTER();
                m_AEM_Printer.print(data);

                data = "13|ColgateGel |35.00|02|70.00\n" +
                        "29|Pears Soap |25.00|01|25.00\n" +
                        "88|Lux Shower |46.00|01|46.00\n" +
                        "15|Dabur Honey|65.00|01|65.00\n" +
                        "52|Dairy Milk |20.00|10|200.00\n" +
                        "128|Maggie TS |36.00|04|144.00\n" +
                        "13|ColgateGel |35.00|02|70.00\n" +
                        "29|Pears Soap |25.00|01|25.00\n" +
                        "88|Lux Shower |46.00|01|46.00\n" +
                        "15|Dabur Honey|65.00|01|65.00\n" +
                        "52|Dairy Milk |20.00|10|200.00\n" +
                        "128|Maggie TS |36.00|04|144.00\n" +
                        "13|ColgateGel |35.00|02|70.00\n" +
                        "29|Pears Soap |25.00|01|25.00\n" +
                        "88|Lux Shower |46.00|01|46.00\n" +
                        "15|Dabur Honey|65.00|01|65.00\n" +
                        "52|Dairy Milk |20.00|10|200.00\n" +
                        "128|Maggie TS |36.00|04|144.00\n" +
                        "13|ColgateGel |35.00|02|70.00\n" +
                        "29|Pears Soap |25.00|01|25.00\n" +
                        "88|Lux Shower |46.00|01|46.00\n" +
                        "15|Dabur Honey|65.00|01|65.00\n" +
                        "52|Dairy Milk |20.00|10|200.00\n" +
                        "128|Maggie TS |36.00|04|144.00\n" +
                        "13|ColgateGel |35.00|02|70.00\n" +
                        "29|Pears Soap |25.00|01|25.00\n" +
                        "88|Lux Shower |46.00|01|46.00\n" +
                        "15|Dabur Honey|65.00|01|65.00\n" +
                        "52|Dairy Milk |20.00|10|200.00\n" +
                        "128|Maggie TS |36.00|04|144.00\n" +
                        "13|ColgateGel |35.00|02|70.00\n" +
                        "29|Pears Soap |25.00|01|25.00\n" +
                        "88|Lux Shower |46.00|01|46.00\n" +
                        "15|Dabur Honey|65.00|01|65.00\n" +
                        "52|Dairy Milk |20.00|10|200.00\n" +
                        "128|Maggie TS |36.00|04|144.00\n" +
                        "13|ColgateGel |35.00|02|70.00\n" +
                        "29|Pears Soap |25.00|01|25.00\n" +
                        "88|Lux Shower |46.00|01|46.00\n" +
                        "15|Dabur Honey|65.00|01|65.00\n" +
                        "52|Dairy Milk |20.00|10|200.00\n" +
                        "128|Maggie TS |36.00|04|144.00\n" +
                        "13|ColgateGel |35.00|02|70.00\n" +
                        "29|Pears Soap |25.00|01|25.00\n" +
                        "88|Lux Shower |46.00|01|46.00\n" +
                        "15|Dabur Honey|65.00|01|65.00\n" +
                        "52|Dairy Milk |20.00|10|200.00\n" +
                        "128|Maggie TS |36.00|04|144.00\n" +
                        "13|ColgateGel |35.00|02|70.00\n" +
                        "29|Pears Soap |25.00|01|25.00\n" +
                        "88|Lux Shower |46.00|01|46.00\n" +
                        "15|Dabur Honey|65.00|01|65.00\n" +
                        "52|Dairy Milk |20.00|10|200.00\n" +
                        "128|Maggie TS |36.00|04|144.00\n" +
                        "13|ColgateGel |35.00|02|70.00\n" +
                        "29|Pears Soap |25.00|01|25.00\n" +
                        "88|Lux Shower |46.00|01|46.00\n" +
                        "15|Dabur Honey|65.00|01|65.00\n" +
                        "52|Dairy Milk |20.00|10|200.00\n" +
                        "128|Maggie TS |36.00|04|144.00\n" +
                        "13|ColgateGel |35.00|02|70.00\n" +
                        "29|Pears Soap |25.00|01|25.00\n" +
                        "88|Lux Shower |46.00|01|46.00\n" +
                        "15|Dabur Honey|65.00|01|65.00\n" +
                        "52|Dairy Milk |20.00|10|200.00\n" +
                        "128|Maggie TS |36.00|04|144.00\n" +
                        "13|ColgateGel |35.00|02|70.00\n" +
                        "29|Pears Soap |25.00|01|25.00\n" +
                        "88|Lux Shower |46.00|01|46.00\n" +
                        "15|Dabur Honey|65.00|01|65.00\n" +
                        "52|Dairy Milk |20.00|10|200.00\n" +
                        "128|Maggie TS |36.00|04|144.00\n" +
                        "13|ColgateGel |35.00|02|70.00\n" +
                        "29|Pears Soap |25.00|01|25.00\n" +
                        "88|Lux Shower |46.00|01|46.00\n" +
                        "15|Dabur Honey|65.00|01|65.00\n" +
                        "52|Dairy Milk |20.00|10|200.00\n" +
                        "128|Maggie TS |36.00|04|144.00\n" +
                        "13|ColgateGel |35.00|02|70.00\n" +
                        "29|Pears Soap |25.00|01|25.00\n" +
                        "88|Lux Shower |46.00|01|46.00\n" +
                        "15|Dabur Honey|65.00|01|65.00\n" +
                        "52|Dairy Milk |20.00|10|200.00\n" +
                        "128|Maggie TS |36.00|04|144.00\n" +
                        "13|ColgateGel |35.00|02|70.00\n" +
                        "29|Pears Soap |25.00|01|25.00\n" +
                        "88|Lux Shower |46.00|01|46.00\n" +
                        "15|Dabur Honey|65.00|01|65.00\n" +
                        "52|Dairy Milk |20.00|10|200.00\n" +
                        "128|Maggie TS |36.00|04|144.00\n" +
                        "_______________________________\n";

                m_AEM_Printer.setFontType(AEMPrinter.FONT_NORMAL);
                m_AEM_Printer.print(data);
                m_AEM_Printer.setFontType(AEMPrinter.DOUBLE_HEIGHT);
                m_AEM_Printer.setFontType(AEMPrinter.TEXT_ALIGNMENT_CENTER);
                data = "TOTAL AMOUNT (Rs.)550.00\n";
                m_AEM_Printer.print(data);
                m_AEM_Printer.POS_FontCENTER();
                m_AEM_Printer.print(d);
                data = "Thank you!\n";
                m_AEM_Printer.POS_FontCENTER();
                m_AEM_Printer.print(data);

                } else {
                m_AEM_Printer.setFontType(ESCAPE_SEQ); //esc
                m_AEM_Printer.setFontType(ESCAPE_a); //a
                m_AEM_Printer.setFontType(ESCAPE_CENTER); //0x01
                m_AEM_Printer.setFontType(ESCAPE_SEQ); //esc
                m_AEM_Printer.setFontType(ESCAPE_EXCL); //!
                m_AEM_Printer.setFontType(ESCAPE_DOUBLE_HEIGHT); //esc

                data = "THREE INCH PRINTER:TEST PRINT \n";
                m_AEM_Printer.POS_FontCENTER();
                m_AEM_Printer.print(data);
                m_AEM_Printer.setCarriageReturn();
                data = 	"CODE|   DESCRIPTION   |RATE(Rs)|QTY |AMOUNT(Rs)\n";
                m_AEM_Printer.POS_FontCENTER();
                m_AEM_Printer.print(data);

                data =  " 13 |Colgate Total Gel | 35.00  | 02 |  70.00\n"+
                        " 29 |Pears Soap 250g   | 25.00  | 01 |  25.00\n"+
                        " 88 |Lux Shower Gel 500| 46.00  | 01 |  46.00\n"+
                        " 15 |Dabur Honey 250g  | 65.00  | 01 |  65.00\n"+
                        " 52 |Cadbury Dairy Milk| 20.00  | 10 | 200.00\n"+
                        "128 |Maggie Totamto Sou| 36.00  | 04 | 144.00\n";
                m_AEM_Printer.POS_FontTAHOMA();
                m_AEM_Printer.print(data);
                data = "TOTAL AMOUNT (Rs.)   550.00\n";
                m_AEM_Printer.POS_FontCENTER();
                m_AEM_Printer.print(data);
                data = "Thank you! \n";
                m_AEM_Printer.POS_FontCENTER();
                m_AEM_Printer.print(data);
                m_AEM_Printer.setCarriageReturn();
                m_AEM_Printer.setCarriageReturn();
            }
        }
        catch (IOException e) {
            if (e.getMessage().contains("socket closed"))
                Toast.makeText(BluetoothActivity.this,"AEMPrinter not connected", Toast.LENGTH_SHORT).show();
        }
    }

    public void onPrint(View v) {
        String data = editText.getText().toString();
         if(m_AEM_Printer != null ) {
            try {
                if (glbPrinterWidth==32) {
                    m_AEM_Printer.print(data);
                     m_AEM_Printer.setCarriageReturn();

                } else {
                    m_AEM_Printer.print(data);
                    m_AEM_Printer.setCarriageReturn();

                }

            }
            catch (IOException e)
            {
                if (e.getMessage().contains("socket closed"))
                    Toast.makeText(BluetoothActivity.this,"AEMPrinter not connected", Toast.LENGTH_SHORT).show();
            }
        }
        else {
            mcl.sendDataOnSocket(data, printOut);
            mcl.sendDataOnSocket("\n\n\n", printOut);
        }
    }

    public void onPrintQRCodeRaster(View v) throws WriterException, IOException {
        if (m_AEM_Printer == null) {
        Toast.makeText(BluetoothActivity.this, "AEMPrinter not connected", Toast.LENGTH_SHORT).show();
        return;
    }
        String text= editText.getText().toString();
        if(text.isEmpty()){
            showAlert("Write Text To Generate QR Code");
        }
        else {
            Writer writer = new QRCodeWriter();
            String finalData = Uri.encode(text, "UTF-8");
            showAlert("QR " + text);
            try {
                BitMatrix bm = writer.encode(finalData, BarcodeFormat.QR_CODE, 300, 300);
                Bitmap bitmap = Bitmap.createBitmap(300, 300, Config.ARGB_8888);
                for (int i = 0; i < 300; i++) {
                    for (int j = 0; j < 300; j++) {
                        bitmap.setPixel(i, j, bm.get(i, j) ? Color.BLACK : Color.WHITE);
                    }
                }
                //showAlert("generating qr bitmap " + wifiConnection);
                Bitmap resizedBitmap = null;
                int numChars = glbPrinterWidth;
                  if(numChars == 32){
                      resizedBitmap = Bitmap.createScaledBitmap(bitmap, 384, 384, false);
                      m_AEM_Printer.printImage(resizedBitmap);
                      m_AEM_Printer.setLineFeed(5);

                  } else {
                      resizedBitmap = Bitmap.createScaledBitmap(bitmap, 384, 384, false);
                      m_AEM_Printer.printImageThreeInch(resizedBitmap);
                      m_AEM_Printer.setLineFeed(5);

                  }

            } catch (WriterException e) {
                showAlert("Error WrQR: " + e.toString());
            }
        }
    }

    public void onPrintMultilingual(View v) {
        if (m_AEM_Printer == null) {
            Toast.makeText(BluetoothActivity.this, "AEMPrinter not connected", Toast.LENGTH_SHORT).show();
            return;
        }
        String data = editText.getText().toString();

        if (data.isEmpty()) {
            showAlert("Write Text");
        } else {
            try {
                if (glbPrinterWidth==32) {
                    data="उचित संसदीय प्रक्रिया का पालन नहीं करने के लिए कांग्रेस";
                    m_AEM_Printer.printTextAsImage("Name " + data);
                    data="उचित संसदीय प्रक्रिया का पालन नहीं करने के लिए कांग्रेस";
                    m_AEM_Printer.printTextAsImage(data);
                    data="उचित संसदीय प्रक्रिया का पालन नहीं करने के लिए कांग्रेस";
                    m_AEM_Printer.printTextAsImage(data);
                    data="उचित संसदीय प्रक्रिया का पालन नहीं करने के लिए कांग्रेस";
                    m_AEM_Printer.printTextAsImage(data);
                }
                else {
                    m_AEM_Printer.printTextAsImageThreeInch(data);
                }
                m_AEM_Printer.setCarriageReturn();
                m_AEM_Printer.setCarriageReturn();
                m_AEM_Printer.setCarriageReturn();
                m_AEM_Printer.setCarriageReturn();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    public void onPrintReverseText(View v) {
        if (m_AEM_Printer == null) {
            Toast.makeText(BluetoothActivity.this, "AEMPrinter not connected", Toast.LENGTH_SHORT).show();
            return;
        }
        String data = editText.getText().toString();
        if (data.isEmpty()) {
            showAlert("Write Text");
        } else {
            try {
                if (glbPrinterWidth==32)
                {
                    m_AEM_Printer.printReverseText(data);
                }
                else {
                    m_AEM_Printer.printReverseText(data);
                }
                m_AEM_Printer.setCarriageReturn();
                m_AEM_Printer.setCarriageReturn();
                m_AEM_Printer.setCarriageReturn();
                m_AEM_Printer.setCarriageReturn();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void onPrintBarcode(View v) {
        onPrintBarcodeBT();
    }

    public void onPrintBarcodeBT() {
        if (m_AEM_Printer == null) {
            showAlert("AEMPrinter not connected");
            return;
        }
        String text = editText.getText().toString();
        if (text.isEmpty()) {
            showAlert("Write Text TO Generate Barcode");
        } else {
            try {
                if(glbPrinterWidth==32) {
                    m_AEM_Printer.printBarcode(text, BARCODE_TYPE.CODE39, BARCODE_HEIGHT.DOUBLEDENSITY_FULLHEIGHT);

                     // generateBarCode(text);
                    m_AEM_Printer.setCarriageReturn();
                    m_AEM_Printer.setCarriageReturn();
                    m_AEM_Printer.setCarriageReturn();


                } else {
              //      m_AEM_Printer.printBarcodeThreeInch(text, BARCODE_TYPE.CODE39, BARCODE_HEIGHT.DOUBLEDENSITY_FULLHEIGHT);
               //     m_AEM_Printer.POS_ThreeInchCENTER();
                    m_AEM_Printer.printBarcodeThreeInch(text, AEMPrinter.BARCODE_TYPE.CODE39, AEMPrinter.BARCODE_HEIGHT.DOUBLEDENSITY_FULLHEIGHT);
                    m_AEM_Printer.POS_FontCENTER();
                    m_AEM_Printer.setLineFeed(4);

                }
            } catch (IOException e) {
                showAlert("AEMPrinter not connected");
            }
        }
    }

    public void onPrintImage(View v)
    {
        onPrintImageBT();
    }

    private void onPrintImageBT() {
        try {
            InputStream is = getAssets().open("bluprintlogo1.jpg");
            Bitmap inputBitmap = BitmapFactory.decodeStream(is);
            Bitmap resizedBitmap = null;
            if(glbPrinterWidth == 32)
                resizedBitmap = Bitmap.createScaledBitmap(inputBitmap, 384, 384, false);

            else
                resizedBitmap = Bitmap.createScaledBitmap(inputBitmap, 384, 384, false);
            //m_AEM_Printer.printBitImage(resizedBitmap,BluetoothActivity.this,m_AEM_Printer.IMAGE_CENTER_ALIGNMENT);

            RasterBT(resizedBitmap);

        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void onPrintImageRaster(View v) {
        if (m_AEM_Printer == null)
        {
            Toast.makeText(BluetoothActivity.this, "AEMPrinter not connected", Toast.LENGTH_SHORT).show();
            return;
        }
        try
        {
            InputStream is = getAssets().open("aadharAEM.jpg");
            Bitmap inputBitmap = BitmapFactory.decodeStream(is);
            Bitmap resizedBitmap = null;
           /*if(glbPrinterWidth == 32)
    		     resizedBitmap = Bitmap.createScaledBitmap(inputBitmap, 350, 140, false);
           else*/
            resizedBitmap = Bitmap.createScaledBitmap(inputBitmap, 284, 284, false);
            RasterBT(resizedBitmap);
        }
        catch (IOException e)
        {
            showAlert("IO Exception: " + e.toString());
        }
    }

    public void onPrintRasterImage(View v) {
        selectImage();
    }
    public void selectImage(){
        Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent, REQUEST_IMAGE_CAPTURE);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK && null !=data){
            Uri selectedImageUri = data.getData();
            uriToBitmap(selectedImageUri);

        } else {
            Toast.makeText(BluetoothActivity.this, "You have not selected and image", Toast.LENGTH_SHORT).show();
        }
    }
    private void uriToBitmap(Uri selectedFileUri) {
        try {
            ParcelFileDescriptor parcelFileDescriptor = getContentResolver().openFileDescriptor(selectedFileUri, "r");
            FileDescriptor fileDescriptor = parcelFileDescriptor.getFileDescriptor();
            Bitmap image = BitmapFactory.decodeFileDescriptor(fileDescriptor);
            Bitmap resizedBitmap=null;
            if(glbPrinterWidth == 32)
                resizedBitmap = Bitmap.createScaledBitmap(image, 384, 384, false);
            else
                resizedBitmap = Bitmap.createScaledBitmap(image, 577, 700, false);
           // resizedBitmap = Bitmap.createScaledBitmap(image, 384, 384, false);
            if (m_AEM_Printer == null)
            {
                Toast.makeText(BluetoothActivity.this, "AEMPrinter not connected", Toast.LENGTH_SHORT).show();
                return;
            }
            RasterBT(resizedBitmap);
            parcelFileDescriptor.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    CardReader.MSRCardData creditDetails;
    public void onScanMSR(final String buffer, CARD_TRACK cardTrack) {
        cardTrackType = cardTrack;

        creditData = buffer;
        BluetoothActivity.this.runOnUiThread(new Runnable()
        {
            public void run()
            {
                editText.setText(buffer.toString());
            }
        });
    }
    public void onScanDLCard(final String buffer) {
        CardReader.DLCardData dlCardData = m_cardReader.decodeDLData(buffer);
        String name = "NAME:" + dlCardData.NAME + "\n";
        String SWD = "SWD Of: " + dlCardData.SWD_OF + "\n";
        String dob = "DOB: " + dlCardData.DOB + "\n";
        String dlNum = "DLNUM: " + dlCardData.DL_NUM + "\n";
        String issAuth = "ISS AUTH: " + dlCardData.ISS_AUTH + "\n";
        String doi = "DOI: " + dlCardData.DOI + "\n";
        String tp = "VALID TP: " + dlCardData.VALID_TP + "\n";
        String ntp = "VALID NTP: " + dlCardData.VALID_NTP + "\n";

        final String data = name + SWD + dob + dlNum + issAuth + doi + tp + ntp;

        runOnUiThread(new Runnable()
        {
            public void run()
            {
                editText.setText(data);
            }
        });
    }

    public void onScanRCCard(final String buffer) {
        CardReader.RCCardData rcCardData = m_cardReader.decodeRCData(buffer);
        String regNum = "REG NUM: " + rcCardData.REG_NUM + "\n";
        String regName = "REG NAME: " + rcCardData.REG_NAME + "\n";
        String regUpto = "REG UPTO: " + rcCardData.REG_UPTO + "\n";

        final String data = regNum + regName + regUpto;

        runOnUiThread(new Runnable()
        {
            public void run()
            {
                editText.setText(data);
            }
        });
    }
    @Override
    public void onScanRFD(final String buffer) {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append(buffer);
        String temp = "";
        try
        {
            temp = stringBuffer.deleteCharAt(8).toString();
        }
        catch (Exception e)
        {
            // TODO: handle exception
        }
        final String data = temp;

        BluetoothActivity.this.runOnUiThread(new Runnable()
        {
            public void run()
            {
                //rfText.setText("RF ID:   " + data);
                editText.setText("ID " + data);
                try {
                    m_AEM_Printer.print(data);
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        });
    }

    public void onDiscoveryComplete(ArrayList<String> aemPrinterList) {
        printerList = aemPrinterList;
        for(int i=0;i<aemPrinterList.size();i++)
        {
            String Device_Name=aemPrinterList.get(i);
            String status = m_AemScrybeDevice.pairPrinter(Device_Name);
            Log.e("STATUS", status);
        }
    }

    public void onDecodeCreditData(View v) {
        if (m_cardReader == null)
        {
            showAlert("AEMPrinter not connected");
            return;
        }

        if (!(creditData.length() > 0))
        {
            showAlert("The data is unavailable");
            return;
        }
        creditDetails = m_cardReader.decodeCreditCard(creditData, cardTrackType);
        String cardNumber = "cardNumber: " + creditDetails.m_cardNumber;
        String HolderName = "HolderName: " + creditDetails.m_AccoundHolderName;
        String ExpirayDate = "Expiray Date: " + creditDetails.m_expiryDate;
        String ServiceCode = "Service Code: " + creditDetails.m_serviceCode;
        String pvki = "PVKI: " + creditDetails.m_pvkiNumber;
        String pvv = "PVV: " + creditDetails.m_pvvNumber;
        String cvv = "CVV: " + creditDetails.m_cvvNumber;

        showAlert(cardNumber + "\n" + HolderName + "\n" + ExpirayDate + "\n"
                + ServiceCode + "\n" + pvki + "\n" + pvv + "\n" + cvv);
    }

    public void showAlert(String alertMsg) {
        AlertDialog.Builder alertBox = new AlertDialog.Builder(BluetoothActivity.this);

        alertBox.setMessage(alertMsg).setCancelable(false).setPositiveButton("OK", new OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // TODO Auto-generated method stub
                        return;
                    }
                });

        AlertDialog alert = alertBox.create();
        alert.show();
    }

    public void onSuccess(String response, Socket socket, String dstAddress, int dstPort, PrintWriter out) {
        txtIP = dstAddress;
        printOut = out;
        socketConnection = socket;
        Log.i("PrintWriter",out+"");
        Log.i("SocketOut", socket+"'");
        editText.setText(txtIP);
        showAlert("AEMPrinter Connected"+" "+response);
    }

    public void onError(String response) {
        showAlert("Please try again... "+" "+response);
    }


    protected void printRasterImageBT(String img_file_path) {
        if (m_AEM_Printer == null) {
            showAlert("AEMPrinter not connected");
            return;
        }
        if(new File(img_file_path).exists()) {
            Bitmap image = BitmapFactory.decodeFile(img_file_path);
            if(image!=null) {
                RasterBT(image);
            }
        }
    }

    protected void RasterBT(Bitmap image) {
        try {
            if (glbPrinterWidth == 32) {
                m_AEM_Printer.printImage(image, getApplicationContext(), AEMPrinter.IMAGE_CENTER_ALIGNMENT);
                m_AEM_Printer.setLineFeed(5);

            } else {
                m_AEM_Printer.printImageThreeInch(image);
                m_AEM_Printer.setLineFeed(4);
            }
        }
        catch (IOException e) {
            showAlert("IO EX:  " + e.toString());
        }
    }
}
