package example.aem.bluprints;

import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Typeface;
import android.hardware.usb.UsbDevice;
import android.util.Log;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.Socket;

public class AEMPrinter {

	BluetoothSocket bluetoothSocket;
	Socket socket;
	int effectivePrintWidth = 48;
	//UsbController usbCtrl = null;
	UsbDevice dev = null;
	Context context;

	public AEMPrinter(BluetoothSocket socket) {
		bluetoothSocket = socket;
	}
	public AEMPrinter(Socket socket) {
		socket = socket;
	}

	  /* // New Change
	   public byte[] ESC_font = new byte[]{0x1b};
	   public byte[] ESC_alignmenText = new byte[]{0x61};
	   public static final byte FONT_NORMAL = 0X00; //font normal
	   public static final byte FONT_CALIBRI = 0X01; // font Calibri
	   public static final byte FONT_Tahoma = 0X02; // font tahoma
	   public static final byte FONT_Verdana = 0X03; // font verdana
	   public static final byte DOUBLE_HIEGHT = 0X10;
	   public static final byte DOUBLE_WIDTH = 0X20;
	   public static final byte DOUBLE_UNDERLINE = (byte) 0X80;
	   public static final byte LEFT = 0X00;
	   public static final byte CENTER = 0X01;
	   public static final byte RIGHT = 0X02;
	   private static final byte NEGATIVE_TEXT = 0X01;
	   public static final byte FONT_001 = 0X03;
	   public static final byte FONT_002 = 0X14;
	   public static final byte FONT_003 = 0X16;

	   //Three Inch Commands
	   public byte[] ESC_dollors_nL_nHp = new byte[]{27, 36, 0, 0};
	   public byte[] ESC_dollors_nL_nH = new byte[]{27, 36, 0, 0};
	   public byte[] GS_w_n = new byte[]{29, 119, 3};
	   public byte[] GS_H_n = new byte[]{29, 72, 0};
	   public byte[] GS_f_n = new byte[]{29, 102, 0};
	   public byte[] GS_h_n = new byte[]{29, 104, -94};
	   public byte[] GS_k_m_n_ = new byte[]{29, 107, 65, 12};
	   public byte[] GS_exclamationmark_n = new byte[]{29, 33, 0};
	   public byte[] ESC_M_n = new byte[]{27, 77, 0};
	   public byte[] GS_E_n = new byte[]{27, 69, 0};
	   public byte[] ESC_line_n = new byte[]{27, 45, 0};
	   public byte[] ESC_lbracket_n = new byte[]{27, 123, 0};
	   public byte[] GS_B_n = new byte[]{29, 66, 0};
	   public byte[] ESC_V_n = new byte[]{27, 86, 0};
	   public byte[] FS_line_n = new byte[]{28, 45, 0};
	   public byte[] ESC_a_n = new byte[]{27, 97, 0};
	   public byte[] ESC_3_n = new byte[]{27, 51, 0};
	   public byte[] ESC_SP_n = new byte[]{27, 32, 0};
	   public byte[] GS_W_nL_nH = new byte[]{29, 87, 118, 2};
	   public byte[] ESC_font_normal = new byte[]{27, 33, 0};

	   // checked
	   public byte[] ESC_FONT_NORMAL = new byte[]{27, 77, 0}; //FONT_NORMAL  checked
	   public byte[] ESC_FONT_CALIBRI = new byte[]{27, 33, 1}; //FONT_CALIBRI checked
	   public byte[] ESC_FONT_TAHOMA = new byte[]{27, 33, 2}; // FONT_Tahoma checked
	   public byte[] ESC_FONT_VERDANA = new byte[]{27, 33, 3}; // FONT_Verdana checked
	   public byte[] ESC_LEFT = new byte[]{27, 97, 0}; // LEFT
	   public byte[] ESC_RIGHT = new byte[]{27, 97, 2}; // RIGHT
	   public byte[] ESC_CENTER = new byte[]{27, 97, 1}; // CENTER
	   public byte[] ESC_NEGATIVETEXT = new byte[]{29, 66, 1}; // Negative text


	   public byte[] ESC_DOUBLE_HIEGHT = new byte[]{27, 33, 10}; // DOUBLE_HIEGHT
	   public byte[] ESC_DOUBLE_WIDTH = new byte[]{27, 33, 20}; // DOUBLE_WIDTH
	   public byte[] ESC_DOUBLE_UNDERLINE = new byte[]{27, 33, 80}; // DOUBLE_UNDERLINE
	   public byte[] ESC_Initialize_printer = new byte[]{27, 64}; // DOUBLE_UNDERLINE
	   public byte[] ESC_BOLD_ADDED = new byte[]{27, 33, 8}; // Bold*/

	public byte[] ESC_font = new byte[]{27};
	public byte[] ESC_alignmenText = new byte[]{97};
	public static final byte FONT_NORMAL = 0;
	public static final byte FONT_CALIBRI = 1;
	public static final byte FONT_Tahoma = 2;
	public static final byte FONT_Verdana = 3;
	public static final byte BOLD_ON = 0x08;
	public static final byte BOLD_OFF = 0x08;
	public static final byte DOUBLE_HIEGHT = 16;
	public static final byte DOUBLE_WIDTH = 32;
	public static final byte UNDERLINE = (byte)0x80;
	//	   public static final byte DOUBLE_UNDERLINE = -128;
	public static final byte LEFT = 0;
	public static final byte CENTER = 1;
	public static final byte RIGHT = 2;
	private static final byte NEGATIVE_TEXT = 1;

	public byte[] ESC_dollors_nL_nH = new byte[]{27, 36, 0, 0};
	public byte[] GS_w_n = new byte[]{29, 119, 3};
	public byte[] GS_h_n = new byte[]{29, 104, -94};
	public byte[] GS_k_m_n_ = new byte[]{29, 107, 65, 12};


	public byte[] ESC_print_position_nL_nH = new byte[]{27, 36, 0, 0};
	public byte[] ESC_char_font_n = new byte[]{27, 77, 0};
	public byte[] ESC_justification_n = new byte[]{27, 97, 0};
	public byte[] ESC_line_spacing_n = new byte[]{27, 51, 0};
	public byte[] ESC_SP_n = new byte[]{27, 32, 0};
	public byte[] ESC_underline_n = new byte[]{27, 45, 0};
	public byte[] ESC_rotation_n = new byte[]{27, 86, 0};
	public byte[] ESC_font_normal = new byte[]{27, 33, 0};
	public byte[] GS_print_barcode_m_n = new byte[]{29, 107, 65, 12};
	public byte[] GS_barcode_width_n = new byte[]{29, 119, 3};
	public byte[] GS_H_n = new byte[]{29, 72, 0};
	public byte[] GS_f_n = new byte[]{29, 102, 0};
	public byte[] GS_char_size_n = new byte[]{29, 33, 0};
	public byte[] GS_E_n = new byte[]{29, 69, 0};
	public byte[] GS_reverse_printing_n = new byte[]{29, 66, 0};
	public byte[] GS_printing_width_nL_nH = new byte[]{29, 87, 118, 2};
	public byte[] GS_barcode_height_n = new byte[]{29, 104, 94};
	public byte[] ESC_FONT_NORMAL = new byte[]{27, 33, 0};
	public byte[] ESC_FONT_CALIBRI = new byte[]{27, 33, 1};
	public byte[] ESC_FONT_TAHOMA = new byte[]{27, 33, 2};
	public byte[] ESC_FONT_VERDANA = new byte[]{27, 33, 3};
	public byte[] ESC_LEFT = new byte[]{27, 97, 0};
	public byte[] ESC_CENTER = new byte[]{27, 97, 1};
	public byte[] ESC_RIGHT = new byte[]{27, 97, 2};
	public byte[] ESC_DOUBLE_HIEGHT = new byte[]{27, 33, 16};
	public byte[] ESC_DOUBLE_WIDTH = new byte[]{27, 33, 32};
	public byte[] ESC_DOUBLE_UNDERLINE = new byte[]{27, 33, -128};
	public byte[] ESC_Initialize_printer = new byte[]{27, 64};
	public byte[] ESC_HORIZANTAL_TAB = new byte[]{9};
	public byte[] ESC_BOLD_ADDED = new byte[]{27, 33, 8};
	public byte[] ESC_FEED_PAPERLINE = new byte[]{27, 100, 1};
	public byte[] ESC_FEED_MINIMUM_UNIT = new byte[]{27, 74, 1};
	public byte[] ESC_BOLD = new byte[]{27, 69, 1};
	public byte[] ESC_CHANGECHARACTERFONT = new byte[]{27, 77, 1};
	public byte[] ESC_Select_character_size = new byte[]{29, 33, 1};
	public byte[] ESC_FEED_AUTOCUT = new byte[]{29, 86, 1};
	public byte[] ESC_LEFT_MARGIN = new byte[]{29, 76, 1};
	public byte[] ESC_Peripheral_Device = new byte[]{27, 61, 1};
	public byte[] ESC_Double_Strike = new byte[]{27, 71, 1};
	public byte[] GS_Smoothing_mode = new byte[]{29, 98, 1};
	public byte[] ESC_NEGATIVE_TEXT = new byte[]{29, 66, 1};
	public byte[] ESC_font_highlight = new byte[]{27, 33, 1};
	public byte[] ESC_font_bold = new byte[]{27, 33, 3};
	public byte[] LF = new byte[]{10};
	public byte[] CR = new byte[]{13};
	public static final byte DOUBLE_HEIGHT = 8;
	public static final byte ESCAPE_SEQ = 27;
	public static final byte ESCAPE_a = 97;
	public static final byte ESCAPE_CENTER = 1;
	public static final byte ESCAPE_EXCL = 33;
	public static final byte ESCAPE_DOUBLE_HEIGHT = 16;
	private static final byte NEGATIVE_CHAR = 14;
	// private static final byte UNDERLINE = 21;
	private static final byte LINE_FEED = 10;
	private static final byte CARRIAGE_RETURN = 13;
	public static final byte TEXT_ALIGNMENT_LEFT = 1;
	public static final byte TEXT_ALIGNMENT_RIGHT = 2;
	public static final byte TEXT_ALIGNMENT_CENTER = 3;
	private final byte BARCODE_TYPE_UPCA = 65;
	private final byte BARCODE_TYPE_EAN13 = 67;
	private final byte BARCODE_TYPE_EAN8 = 68;
	private final byte BARCODE_TYPE_CODE39 = 69;
	private final byte BARCODE_TYPE_CODE128 = 115;
	Context m_Context;
	public static final byte IMAGE_LEFT_ALIGNMENT = 108;
	public static final byte IMAGE_CENTER_ALIGNMENT = 99;
	public static final byte IMAGE_RIGHT_ALIGNMENT = 114;

	public void POS_set_default() throws IOException {
		byte[] data = new byte[]{0x1b, 0x40};
		this.bluetoothSocket.getOutputStream().write(data, 0, data.length);
	}
	public void POS_ESC_print_position_nL_nH() throws IOException {
		byte[] data = byteArraysToBytes(new byte[][]{this.ESC_print_position_nL_nH});
		this.bluetoothSocket.getOutputStream().write(data, 0, data.length);
	}

	public void POS_ESC_char_font_n() throws IOException {
		byte[] data = byteArraysToBytes(new byte[][]{this.ESC_char_font_n});
		this.bluetoothSocket.getOutputStream().write(data, 0, data.length);
	}

	public void POS_ESC_line_spacing_n() throws IOException {
		byte[] data = byteArraysToBytes(new byte[][]{this.ESC_line_spacing_n});
		this.bluetoothSocket.getOutputStream().write(data, 0, data.length);
	}
	public void POS_Set_text_style( byte font, byte hight_width, byte bold_flag, byte uline_flag) throws IOException {
		byte text_style;
		text_style = (byte) (font | hight_width | bold_flag | uline_flag);
		byte[] data = new byte[]{0x1b, 0x21, text_style};
		this.bluetoothSocket.getOutputStream().write(data, 0, data.length);
	}

	public void POS_ESC_SP_n() throws IOException {
		byte[] data = byteArraysToBytes(new byte[][]{this.ESC_SP_n});
		this.bluetoothSocket.getOutputStream().write(data, 0, data.length);
	}

	public void POS_ESC_underline_n() throws IOException {
		byte[] data = byteArraysToBytes(new byte[][]{this.ESC_underline_n});
		this.bluetoothSocket.getOutputStream().write(data, 0, data.length);
	}

	public void POS_ESC_rotation_n() throws IOException {
		byte[] data = byteArraysToBytes(new byte[][]{this.ESC_rotation_n});
		this.bluetoothSocket.getOutputStream().write(data, 0, data.length);
	}

	public void POS_GS_print_barcode_m_n() throws IOException {
		byte[] data = byteArraysToBytes(new byte[][]{this.GS_print_barcode_m_n});
		this.bluetoothSocket.getOutputStream().write(data, 0, data.length);
	}

	public void POS_GS_barcode_width_n() throws IOException {
		byte[] data = byteArraysToBytes(new byte[][]{this.GS_barcode_width_n});
		this.bluetoothSocket.getOutputStream().write(data, 0, data.length);
	}

	public void POS_GS_H_n() throws IOException {
		byte[] data = byteArraysToBytes(new byte[][]{this.GS_H_n});
		this.bluetoothSocket.getOutputStream().write(data, 0, data.length);
	}

	public void POS_GS_f_n() throws IOException {
		byte[] data = byteArraysToBytes(new byte[][]{this.GS_f_n});
		this.bluetoothSocket.getOutputStream().write(data, 0, data.length);
	}

	public void POS_GS_char_size_n() throws IOException {
		byte[] data = byteArraysToBytes(new byte[][]{this.GS_char_size_n});
		this.bluetoothSocket.getOutputStream().write(data, 0, data.length);
	}

	public void POS_GS_E_n() throws IOException {
		byte[] data = byteArraysToBytes(new byte[][]{this.GS_E_n});
		this.bluetoothSocket.getOutputStream().write(data, 0, data.length);
	}

	public void POS_GS_reverse_printing_n() throws IOException {
		byte[] data = byteArraysToBytes(new byte[][]{this.GS_reverse_printing_n});
		this.bluetoothSocket.getOutputStream().write(data, 0, data.length);
	}

	public void POS_GS_barcode_height_n() throws IOException {
		byte[] data = byteArraysToBytes(new byte[][]{this.GS_barcode_height_n});
		this.bluetoothSocket.getOutputStream().write(data, 0, data.length);
	}

	public void POS_FontThreeInch() throws IOException {
		byte[] data = byteArraysToBytes(new byte[][]{this.ESC_font_normal});
		this.bluetoothSocket.getOutputStream().write(data, 0, data.length);
	}

	public void POS_CALIBRI() throws IOException {
		byte[] data = byteArraysToBytes(new byte[][]{this.ESC_FONT_CALIBRI});
		this.bluetoothSocket.getOutputStream().write(data, 0, data.length);
	}

	public void POS_FontTAHOMA() throws IOException {
		byte[] data = byteArraysToBytes(new byte[][]{this.ESC_FONT_TAHOMA});
		this.bluetoothSocket.getOutputStream().write(data, 0, data.length);
	}

	public void POS_FontVERDANA() throws IOException {
		byte[] data = byteArraysToBytes(new byte[][]{this.ESC_FONT_VERDANA});
		this.bluetoothSocket.getOutputStream().write(data, 0, data.length);
	}

	public void POS_FontLEFT() throws IOException {
		byte[] data = byteArraysToBytes(new byte[][]{this.ESC_LEFT});
		this.bluetoothSocket.getOutputStream().write(data, 0, data.length);
	}

	public void POS_FontCENTER() throws IOException {
		byte[] data = byteArraysToBytes(new byte[][]{this.ESC_CENTER});
		this.bluetoothSocket.getOutputStream().write(data, 0, data.length);
	}

	public void POS_FontRIGHT() throws IOException {
		byte[] data = byteArraysToBytes(new byte[][]{this.ESC_RIGHT});
		this.bluetoothSocket.getOutputStream().write(data, 0, data.length);
	}

	public void POS_FontDOUBLEHIEGHT() throws IOException {
		byte[] data = byteArraysToBytes(new byte[][]{this.ESC_DOUBLE_HIEGHT});
		this.bluetoothSocket.getOutputStream().write(data, 0, data.length);
	}

	public void POS_FontDOUBLEWIDTH() throws IOException {
		byte[] data = byteArraysToBytes(new byte[][]{this.ESC_DOUBLE_WIDTH});
		this.bluetoothSocket.getOutputStream().write(data, 0, data.length);
	}

	public void POS_FontUNDERLINE() throws IOException {
		byte[] data = byteArraysToBytes(new byte[][]{this.ESC_DOUBLE_UNDERLINE});
		this.bluetoothSocket.getOutputStream().write(data, 0, data.length);
	}

	public void POS_FontThreeInchInitialize_printer() throws IOException {
		byte[] data = byteArraysToBytes(new byte[][]{this.ESC_Initialize_printer});
		this.bluetoothSocket.getOutputStream().write(data, 0, data.length);
	}

	public void POS_PARTIALCUT() throws IOException {
		byte[] data = byteArraysToBytes(new byte[][]{this.ESC_BOLD_ADDED});
		this.bluetoothSocket.getOutputStream().write(data, 0, data.length);
	}

	public void POS_ESC_FEED_PAPERLINE() throws IOException {
		byte[] data = byteArraysToBytes(new byte[][]{this.ESC_FEED_PAPERLINE});
		this.bluetoothSocket.getOutputStream().write(data, 0, data.length);
	}

	public void POS_ESC_FEED_MINIMUM_UNIT() throws IOException {
		byte[] data = byteArraysToBytes(new byte[][]{this.ESC_FEED_MINIMUM_UNIT});
		this.bluetoothSocket.getOutputStream().write(data, 0, data.length);
	}

	public void POS__ESC_BOLD() throws IOException {
		byte[] data = byteArraysToBytes(new byte[][]{this.ESC_BOLD});
		this.bluetoothSocket.getOutputStream().write(data, 0, data.length);
	}

	public void POS_ESC_CHANGECHARACTERFONT() throws IOException {
		byte[] data = byteArraysToBytes(new byte[][]{this.ESC_CHANGECHARACTERFONT});
		this.bluetoothSocket.getOutputStream().write(data, 0, data.length);
	}

	public void POS_ESC_Select_character_size() throws IOException {
		byte[] data = byteArraysToBytes(new byte[][]{this.ESC_Select_character_size});
		this.bluetoothSocket.getOutputStream().write(data, 0, data.length);
	}

	public void POS_ESC_FEED_AUTOCUT() throws IOException {
		byte[] data = byteArraysToBytes(new byte[][]{this.ESC_FEED_AUTOCUT});
		this.bluetoothSocket.getOutputStream().write(data, 0, data.length);
	}

	public void POS_ESC_LEFT_MARGIN() throws IOException {
		byte[] data = byteArraysToBytes(new byte[][]{this.ESC_LEFT_MARGIN});
		this.bluetoothSocket.getOutputStream().write(data, 0, data.length);
	}

	public void POS_ESC_Peripheral_Device() throws IOException {
		byte[] data = byteArraysToBytes(new byte[][]{this.ESC_Peripheral_Device});
		this.bluetoothSocket.getOutputStream().write(data, 0, data.length);
	}

	public void POS_ESC_Double_Strike() throws IOException {
		byte[] data = byteArraysToBytes(new byte[][]{this.ESC_Double_Strike});
		this.bluetoothSocket.getOutputStream().write(data, 0, data.length);
	}

	public void POS_GS_Smoothing_mode() throws IOException {
		byte[] data = byteArraysToBytes(new byte[][]{this.GS_Smoothing_mode});
		this.bluetoothSocket.getOutputStream().write(data, 0, data.length);
	}

	public void POS_Font_highlight_ThreeInch() throws IOException {
		byte[] data = byteArraysToBytes(new byte[][]{this.ESC_font_highlight});
		this.bluetoothSocket.getOutputStream().write(data, 0, data.length);
	}

	public void POS_Font_bold_ThreeInch() throws IOException {
		byte[] data = byteArraysToBytes(new byte[][]{this.ESC_font_bold});
		this.bluetoothSocket.getOutputStream().write(data, 0, data.length);
	}

	public void POS_S_AlignThreeInch(int align) throws IOException {
		if (align >= 0 && align <= 2) {
			byte[] data = this.ESC_justification_n;
			data[2] = (byte)align;
			this.bluetoothSocket.getOutputStream().write(data, 0, data.length);
		}

	}

	public void POS_FeedLineThreeInch() throws IOException {
		byte[] data = byteArraysToBytes(new byte[][]{this.CR, this.LF});
		this.bluetoothSocket.getOutputStream().write(data, 0, data.length);
	}

	public void POS_SetRightSpacing(int nDistance) throws IOException {
		if (!(nDistance < 0 | nDistance > 255)) {
			byte[] data = this.ESC_SP_n;
			data[2] = (byte)nDistance;
			this.bluetoothSocket.getOutputStream().write(data, 0, data.length);
		}

	}

	public void printThreeInch(String text) throws IOException {
		if (bluetoothSocket == null)
			return;
		byte header[] = null;
		byte strbuf[] = null;
		// header = new byte[] { 0x1b, 0x40, 0x1c, 0x26, 0x1b, 0x39, 0x05 };
		header = new byte[] { 0x1b, 0x40/*, 0x1c, 0x26, 0x1b, 0x39, 0x01*/ };
		strbuf =text.getBytes();
		byte buffer[] = byteArraysToBytes(new byte[][] {header, strbuf });
		bluetoothSocket.getOutputStream().write(buffer, 0, buffer.length);
	}



	public void setFontType(byte FONT) throws IOException {
		bluetoothSocket.getOutputStream().write(FONT);
	}

	public void sendByte(byte bt) throws IOException {
		bluetoothSocket.getOutputStream().write(bt);
	}

	public void sendByteArrayBT(byte[] byteArr) throws IOException {
		bluetoothSocket.getOutputStream().write(byteArr, 0, byteArr.length);
	}

	public void setLineFeed(int noOfFeeds) throws IOException {
		for(int i = 0; i < noOfFeeds ; i++)
			bluetoothSocket.getOutputStream().write(LINE_FEED);
	}

	public void setCarriageReturn() throws IOException
	{
		bluetoothSocket.getOutputStream().write(LINE_FEED);
	}

	// printing text
	public void print(String text) throws IOException {
		if (bluetoothSocket == null)
			return;
		bluetoothSocket.getOutputStream().write(text.getBytes(), 0, text.getBytes().length);
	}

	public void PrintHindi(String text) throws IOException {
		if (bluetoothSocket == null)
			return;

		int ctr2, ctr1, ctr3  = 0;
		int addFlag = 0, nextCtrFlag = 0, E0Flag = 0, hindiFlag = 0;
		//final byte[] btArr = data.getBytes(Charset.forName("UTF-8"));
		byte[] btArr = text.getBytes();
		final byte[] newArr = new byte[1000];
		final byte[] arrToSend = new byte[1000];
		byte b1;//, b2, b3;
		ctr2=0;

		for(ctr1 = 0; ctr1<btArr.length; ctr1++) {
			b1 = btArr[ctr1];
			nextCtrFlag = 0;
			if((b1 > 0) && (b1 < 127))//english characters/spl chars
				 {
				newArr[ctr2] = b1;
				addFlag = 0;
				nextCtrFlag = 1;
				hindiFlag = 0;
			}
			else if(b1 == -32)//0xE0
			{
				addFlag = 0;
				E0Flag = 1;
				hindiFlag = 0;
			}
			else if(b1 == -91)
			{
				if(E0Flag == 1)
				{
					addFlag = 1;
					E0Flag = 2;
					hindiFlag = 0;
				}
				else if(E0Flag == 2)
					hindiFlag = 1;
			}
			else if(b1 == -92)
			{
				if(E0Flag == 1)
				{
					addFlag = 0;
					E0Flag = 2;
					hindiFlag = 0;
				}
				else if(E0Flag == 2)
					hindiFlag = 1;

			}
			else if(b1 < 0) //hindi chars
				hindiFlag = 1;
			if(hindiFlag == 1)
			{
				if(addFlag == 1)
					newArr[ctr2] = (byte)(b1 + 128 - 64) ;
				else
					newArr[ctr2] = b1;
				nextCtrFlag = 1;
				addFlag = 0;
				E0Flag = 0;
				hindiFlag = 0;
			}
			if(nextCtrFlag ==1)
				ctr2++;
		}
		b1 = -123;
		byte b2;

		for (ctr1 = 0; ctr1<ctr2; ctr1++)
		{
			if(newArr[ctr1] == -108) //ou
			{
				//m_AEM_Printer.setFontType(b1); //a
				arrToSend[ctr3++] = b1;
				b2 = -52;
				// m_AEM_Printer.setFontType(b2);
				arrToSend[ctr3++] = b2;
			}
			else if(newArr[ctr1] == -109) //o
			{
				//m_AEM_Printer.setFontType(b1);
				arrToSend[ctr3++] = b1;
				b2 = -53;
				arrToSend[ctr3++] = b2;
				//m_AEM_Printer.setFontType(b2);

			}
			else if(newArr[ctr1] == -122) //aa
			{
				//m_AEM_Printer.setFontType(b1);
				arrToSend[ctr3++] = b1;
				b2 = -66;
				arrToSend[ctr3++] = b2;
				// m_AEM_Printer.setFontType(b2);
			}
			else if(((ctr2-ctr1)>=2)&&(newArr[ctr1] == -107)&&(newArr[ctr1+1] == -51)&&(newArr[ctr1+2] == -73))//ksha
			{
				b2 = -87;
				arrToSend[ctr3++] = b2;
				//m_AEM_Printer.setFontType(b2);//ksha
				ctr1+=2;
			}
			else if(((ctr2-ctr1)>=2)&&(newArr[ctr1] == -107)&&(newArr[ctr1+1] == -51)&&(newArr[ctr1+2] == -80))//kra
			{
				b2 = -124;
				arrToSend[ctr3++] = b2;
				//m_AEM_Printer.setFontType(b2);//send code for kra
				ctr1+=2;
			}
			else if(((ctr2-ctr1)>=2)&&(newArr[ctr1] == -105)&&(newArr[ctr1+1] == -51)&&(newArr[ctr1+2] == -80))//gra
			{
				b2 = -122;
				arrToSend[ctr3++] = b2;
				//m_AEM_Printer.setFontType(b2);//send code for gra
				ctr1+=2;
			}
			else if(((ctr2-ctr1)>=2)&&(newArr[ctr1] == -105)&&(newArr[ctr1+1] == -51)&&(newArr[ctr1+2] == -81))//gya
			{
				b2 = -114;
				arrToSend[ctr3++] = b2;
				//m_AEM_Printer.setFontType(b2);//send code for gya
				ctr1+=2;
			}
			else if(((ctr2-ctr1)>=2)&&(newArr[ctr1] == -86)&&(newArr[ctr1+1] == -51)&&(newArr[ctr1+2] == -80))//pra
			{
				b2 = -115;
				arrToSend[ctr3++] = b2;
				//m_AEM_Printer.setFontType(b2);//send code for pra
				ctr1+=2;
			}
			else if(((ctr2-ctr1)>=2)&&(newArr[ctr1] == -97)&&(newArr[ctr1+1] == -51)&&(newArr[ctr1+2] == -80))//tra
			{
				b2 = -69;
				arrToSend[ctr3++] = b2;
				//m_AEM_Printer.setFontType(b2);//send code for tra
				ctr1+=2;
			}

			else if(((ctr2-ctr1)>=2)&&(newArr[ctr1] == -92)&&(newArr[ctr1+1] == -51)&&(newArr[ctr1+2] == -80))//thra
			{
				b2 = -111;
				arrToSend[ctr3++] = b2;
				///m_AEM_Printer.setFontType(b2);//send code for thra
				ctr1+=2;
			}

			else if(((ctr2-ctr1)>=2)&&(newArr[ctr1] == -92)&&(newArr[ctr1+1] == -51)&&(newArr[ctr1+2] == -92))//ttha
			{
				b2 = -116;
				arrToSend[ctr3++] = b2;
				//m_AEM_Printer.setFontType(b2);//send code for ttha
				ctr1+=2;
			}
			else if(((ctr2-ctr1)>=2)&&(newArr[ctr1] == -74)&&(newArr[ctr1+1] == -51)&&(newArr[ctr1+2] == -80))//shra
			{
				b2 = -79;
				arrToSend[ctr3++] = b2;
				// m_AEM_Printer.setFontType(b2);//send code for shra
				ctr1+=2;
			}
			else if(((ctr2-ctr1)>=2)&&(newArr[ctr1] == -90)&&(newArr[ctr1+1] == -51)&&(newArr[ctr1+2] == -80))//dra
			{
				b2 = -108;
				arrToSend[ctr3++] = b2;
				//m_AEM_Printer.setFontType(b2);//send code for dra
				ctr1+=2;
			}
			else if(((ctr2-ctr1)>=2)&&(newArr[ctr1] == -90)&&(newArr[ctr1+1] == -51)&&(newArr[ctr1+2] == -75))//dva
			{
				b2 = -70;
				arrToSend[ctr3++] = b2;
				//m_AEM_Printer.setFontType(b2);//send code for dva
				ctr1+=2;
			}
			else if(((ctr2-ctr1)>=2)&&(newArr[ctr1] == -90)&&(newArr[ctr1+1] == -51)&&(newArr[ctr1+2] == -81))//dya
			{
				b2 = -119;
				arrToSend[ctr3++] = b2;
				//m_AEM_Printer.setFontType(b2);//send code for dya
				ctr1+=2;
			}
			else if(((ctr2-ctr1)>=2)&&(newArr[ctr1] == -71)&&(newArr[ctr1+1] == -51)&&(newArr[ctr1+2] == -81))//hya
			{
				b2 = -110;
				arrToSend[ctr3++] = b2;
				//m_AEM_Printer.setFontType(b2);//send code for hya
				ctr1+=2;
			}
			else if(((ctr2-ctr1)>=2)&&(newArr[ctr1] == -80)&&(newArr[ctr1+1] == -51))//r matra
			{
				//m_AEM_Printer.setFontType(newArr[ctr1+2]);
				arrToSend[ctr3++] = newArr[ctr1+2];
				if(ctr2-ctr1 > 2)
				{
					if((newArr[ctr1+3] == -66) || (newArr[ctr1+3] == -65) || (newArr[ctr1+3] == -64))//matras
					{
						//m_AEM_Printer.setFontType(newArr[ctr1+3]);
						arrToSend[ctr3++] = newArr[ctr1+3];
						ctr1++;
					}
				}
				b2 = -67;
				arrToSend[ctr3++] = b2;
				//m_AEM_Printer.setFontType(b2);//send code for r matra
				ctr1+=2;
			}
			else if((ctr2-ctr1 > 1) && (newArr[ctr1] == -80)&& (newArr[ctr1+1] == -63))//ru
			{
				b2 = -76;
				arrToSend[ctr3++] = b2;
				//m_AEM_Printer.setFontType(b2);//send code for ru
				ctr1+=1;
			}
			else if((ctr2-ctr1 > 1) && (newArr[ctr1] == -80)&& (newArr[ctr1+1] == -62))//roo
			{
				b2 = -77;
				arrToSend[ctr3++] = b2;
				//m_AEM_Printer.setFontType(b2);//send code for roo
				ctr1+=1;
			}
			else
				arrToSend[ctr3++] = newArr[ctr1];
			//m_AEM_Printer.setFontType(newArr[ctr1]);
		}

		// m_AEM_Printer.print(s);

		int len = ctr3;
		for (ctr1 = 0; ctr1 < len; ctr1++)
		{
			if(ctr1 > 0) {
				if (arrToSend[ctr1] == -65)//small e matra
				{
					arrToSend[ctr1] = arrToSend[ctr1 - 1];
					arrToSend[ctr1 - 1] = -65;

					if (ctr1 > 2) {
						if (arrToSend[ctr1 - 2] == -51)//in case of halant character
						{
							byte temp = arrToSend[ctr1 - 3];
							arrToSend[ctr1 - 3] = -65; //small e matra
							arrToSend[ctr1 - 2] = temp;
							arrToSend[ctr1 - 1] = -51;
						}
					}
				}

			}

		}

		int matraFlag = 0;
		ctr2 = 0;
		for (ctr1 = 0; ctr1 < len; ctr1++)
		{
			if(matraFlag == 2)
			{
				matraFlag = 0;
				b2 = -60;
				newArr[ctr2++] = b2; //send additional or-ing character for better visibility of matra
			}
			newArr[ctr2++] = arrToSend[ctr1];
			if(matraFlag > 0)
				matraFlag++;
			if(arrToSend[ctr1] == -65) //small e matra
				matraFlag = 1;
		}
		if(matraFlag == 2)//in case matra is at last character
		{
			matraFlag = 0;
			b2 = -60;
			newArr[ctr2++] = b2;
			//m_AEM_Printer.setFontType(b2); //send additional or-ing character for better visibility of matra
		}

		bluetoothSocket.getOutputStream().write(newArr, 0,ctr2);
		setCarriageReturn();

	}


	public void printImage(Bitmap resizedBitmap) {
		PrintRasterImage PrintRasterImage = new PrintRasterImage(getResizedBitmap(resizedBitmap));
		PrintRasterImage.PrepareImage(example.aem.bluprints.PrintRasterImage.dither.floyd_steinberg, 128);
		byte[] imgStr =PrintRasterImage.getPrintImageData();
		try
		{
			bluetoothSocket.getOutputStream().write(imgStr, 0, imgStr.length);
		}
		catch(IOException e) {
			System.out.print("IOException ");
		}
	}

	public enum BARCODE_TYPE {UPCA, EAN13, EAN8, CODE39,CODE128};

	public enum BARCODE_HEIGHT {DOUBLEDENSITY_FULLHEIGHT, TRIPLEDENSITY_FULLHEIGHT, DOUBLEDENSITY_HALFHEIGHT, TRIPLEDENSITY_HALFHEIGHT};

	/*public Bitmap createQRCode(String text) throws WriterException	                                            	{

		Writer writer = new QRCodeWriter();
		String finalData = Uri.encode(text, "UTF-8");

		BitMatrix bm = writer.encode(finalData, BarcodeFormat.QR_CODE, 350, 255);
	    Bitmap bitmap = Bitmap.createBitmap(350, 255, Config.ARGB_8888);

	    for(int i = 0; i < 350; i++)
	    {
	    	for(int j = 0; j < 255; j++)
	    	{
	    		bitmap.setPixel(i, j, bm.get(i, j) ? Color.BLACK: Color.WHITE);
	    	}
	    }

	    return bitmap;
	}*/

	// printing barcode
	public void printBarcode(String barcodeData, BARCODE_TYPE barcodetype, BARCODE_HEIGHT barcodeheight) throws IOException {
		if (bluetoothSocket == null)
			return;
		byte[] barcodepacket = createBarcodePacket(barcodeData.getBytes(), barcodetype, barcodeheight);
		if(barcodepacket == null)
			return;
		bluetoothSocket.getOutputStream().write(barcodepacket, 0, barcodepacket.length);
	}

	public void printBarcodeThreeInch(String barcodeData, BARCODE_TYPE barcodetype, BARCODE_HEIGHT barcodeheight) throws IOException {
		if (bluetoothSocket == null)
			return;
		byte[] barcodepacket = createBarcodePacketThreeInch(barcodeData.getBytes(), barcodetype, barcodeheight);
		if(barcodepacket == null)
			return;
		bluetoothSocket.getOutputStream().write(barcodepacket, 0, barcodepacket.length);
	}
	private byte[] createBarcodePacketThreeInch(byte[] barcodeBytes, BARCODE_TYPE barcodetype, BARCODE_HEIGHT height)
	{
		if(barcodetype == BARCODE_TYPE.CODE39)
		{
			int type=0x41 + BARCODE_TYPE_CODE39;
			ESC_dollors_nL_nH[2] = 0;
			ESC_dollors_nL_nH[3] = 0;
			GS_w_n[2] = 2;
			GS_h_n[2] = 96;
			GS_f_n[2] = 0;
			GS_H_n[2] = 2;
			GS_k_m_n_[2] = 69;
			GS_k_m_n_[3] = (byte)(barcodeBytes.length);

			byte[] barcodePacket = new byte[barcodeBytes.length+6];
			barcodePacket[0] = 0x1D;
			barcodePacket[1] = 0x6B;
			barcodePacket[2] = (byte) type; // barcode type
			barcodePacket[3] = (byte) (barcodeBytes.length + 2); //length of barcode data
			//barcodePacket[4] = getBarcodeHeight(height);
			barcodePacket[4] = 0x2A;
			byte[] data = byteArraysToBytes(new byte[][]{this.ESC_dollors_nL_nH, this.GS_w_n, this.GS_h_n, this.GS_f_n, this.GS_H_n, this.GS_k_m_n_, barcodeBytes});

			int i = 0;

			for(i = 0; i < barcodeBytes.length; i++)
			{
				barcodePacket[i + 5] = barcodeBytes[i];
			}

			barcodePacket[i + 5] = 0x2A;

			return data;
		}else if(barcodetype == BARCODE_TYPE.UPCA)

		{
			byte[] barcodePacket = new byte[barcodeBytes.length + 4];
			barcodePacket[0] = 0x1D;
			barcodePacket[1] = 0x6B;
			barcodePacket[2] = BARCODE_TYPE_UPCA; // barcode type
			barcodePacket[3] = (byte) (barcodeBytes.length); //length of barcode data
			//barcodePacket[4] = getBarcodeHeight(height);

			for(int i = 0; i < barcodeBytes.length; i++)
			{
				barcodePacket[i + 4] = barcodeBytes[i];
			}

			return barcodePacket;
		}
		else if(barcodetype == BARCODE_TYPE.EAN13) {
			byte[] barcodePacket = new byte[barcodeBytes.length + 4];

			barcodePacket[0] = 0x1D;
			barcodePacket[1] = 0x6B;
			barcodePacket[2] = BARCODE_TYPE_EAN13; // barcode type
			barcodePacket[3] = (byte) (barcodeBytes.length); //length of barcode data
			//barcodePacket[4] = getBarcodeHeight(height);

			for(int i = 0; i < barcodeBytes.length; i++)
			{
				barcodePacket[i + 4] = barcodeBytes[i];
			}
			return barcodePacket;

		}
		else if(barcodetype == BARCODE_TYPE.EAN8)
		{
			byte[] barcodePacket = new byte[barcodeBytes.length + 4];
			barcodePacket[0] = 0x1D;
			barcodePacket[1] = 0x6B;
			barcodePacket[2] = BARCODE_TYPE_EAN8; // barcode type
			barcodePacket[3] = (byte) (barcodeBytes.length); //length of barcode data
			//barcodePacket[4] = getBarcodeHeight(height);

			for(int i = 0; i < barcodeBytes.length; i++)
			{
				barcodePacket[i + 4] = barcodeBytes[i];
			}

			return barcodePacket;
		}

		return null;
	}



	private byte[] createBarcodePacket(byte[] barcodeBytes, BARCODE_TYPE barcodetype, BARCODE_HEIGHT height) {
		if(barcodetype == BARCODE_TYPE.CODE39) {
			byte[] barcodePacket = new byte[barcodeBytes.length + 6];

			barcodePacket[0] = 0x1D;
			barcodePacket[1] = 0x6B;
			barcodePacket[2] = BARCODE_TYPE_CODE39; // barcode type
			//barcodePacket[3] = (byte) (barcodeBytes.length+2); //length of barcode data
			//barcodePacket[4] = getBarcodeHeight(height);
			barcodePacket[3] = 0x2A;

			int i = 0;

			for(i = 0; i < barcodeBytes.length; i++)
			{
				barcodePacket[i + 4] = barcodeBytes[i];
			}

			barcodePacket[i + 4] = 0x2A;
			barcodePacket[i + 5] = 0x00;

			return barcodePacket;

		}else if(barcodetype == BARCODE_TYPE.UPCA){
			byte[] barcodePacket = new byte[barcodeBytes.length + 4];
			barcodePacket[0] = 0x1D;
			barcodePacket[1] = 0x6B;
			barcodePacket[2] = BARCODE_TYPE_UPCA; // barcode type
			barcodePacket[3] = (byte) (barcodeBytes.length); //length of barcode data
			//barcodePacket[4] = getBarcodeHeight(height);

			for(int i = 0; i < barcodeBytes.length; i++) {
				barcodePacket[i + 4] = barcodeBytes[i];
			}

			return barcodePacket;
		}else if(barcodetype == BARCODE_TYPE.EAN13) {
			byte[] barcodePacket = new byte[barcodeBytes.length + 4];

			barcodePacket[0] = 0x1D;
			barcodePacket[1] = 0x6B;
			barcodePacket[2] = BARCODE_TYPE_EAN13; // barcode type
			barcodePacket[3] = (byte) (barcodeBytes.length); //length of barcode data
			//barcodePacket[4] = getBarcodeHeight(height);

			for(int i = 0; i < barcodeBytes.length; i++)
			{
				barcodePacket[i + 4] = barcodeBytes[i];
			}
			return barcodePacket;

		}
		else if(barcodetype == BARCODE_TYPE.EAN8)
		{
			byte[] barcodePacket = new byte[barcodeBytes.length + 4];
			barcodePacket[0] = 0x1D;
			barcodePacket[1] = 0x6B;
			barcodePacket[2] = BARCODE_TYPE_EAN8; // barcode type
			barcodePacket[3] = (byte) (barcodeBytes.length); //length of barcode data
			//barcodePacket[4] = getBarcodeHeight(height);

			for(int i = 0; i < barcodeBytes.length; i++)
			{
				barcodePacket[i + 4] = barcodeBytes[i];
			}

			return barcodePacket;
		}

		return null;
	}

	private byte getBarcodeHeight(BARCODE_HEIGHT height)
	{
		byte mode = 0x61;

		if(height == BARCODE_HEIGHT.DOUBLEDENSITY_FULLHEIGHT)
		{
			mode = 0x61;
		}else if(height == BARCODE_HEIGHT.TRIPLEDENSITY_FULLHEIGHT)
		{
			mode = 0x62;
		}else if(height == BARCODE_HEIGHT.DOUBLEDENSITY_HALFHEIGHT)
		{
			mode = 0x63;
		}else if(height == BARCODE_HEIGHT.TRIPLEDENSITY_HALFHEIGHT)
		{
			mode = 0x64;
		}

		return mode;
	}


	public void printBitImage(Bitmap originalBitmap, Context context, byte image_alignment) throws IOException {
		m_Context = context;
		ImageHandler imageHandler = new ImageHandler(context);
		byte[] imagePacket = imageHandler.getMonoChromeImagePacket(originalBitmap, image_alignment);
		//Bitmap bmp=
		Log.e("Image Byte",imagePacket.toString());
		if(imagePacket == null)
			return;
		bluetoothSocket.getOutputStream().write(imagePacket, 0, imagePacket.length);
		if(deleteFile())
		{
			System.out.print("b");
		}
		else
		{
			System.out.print("b");
		}
	}
	public void printBitImageUsb(Bitmap originalBitmap, Context context, byte image_alignment) throws IOException
	{
		m_Context = context;
		ImageHandler imageHandler = new ImageHandler(context);
		byte[] imagePacket = imageHandler.getMonoChromeImagePacket(originalBitmap, image_alignment);
		if(imagePacket == null)
			return;
		//	usbCtrl.sendByte(imagePacket,dev);
		//bluetoothSocket.getOutputStream().write(imagePacket, 0, imagePacket.length);
		if(deleteFile())
		{
			System.out.print("b");
		}
		else
		{
			System.out.print("b");
		}
	}

	public void printImage(Bitmap originalBitmap, Context applicationContext, byte imageCenterAlignment) throws IOException
	{

		PrintRasterImage PrintRasterImage = new PrintRasterImage(getResizedBitmap(originalBitmap));
		PrintRasterImage.PrepareImage(example.aem.bluprints.PrintRasterImage.dither.floyd_steinberg, 128);
		byte[] imgStr =PrintRasterImage.getPrintImageData();
		try
		{
			bluetoothSocket.getOutputStream().write(imgStr, 0, imgStr.length);
		}
		catch(IOException e)
		{
			System.out.print("IOException ");
		}
	}
	/*
	protected void selectImageFromSDCard()
    {
        FileDialog fileDialog;
        File mPath = new File(Environment.getExternalStorageDirectory() + "//DIR//");
        fileDialog = new FileDialog(BluetoothActivity.this, mPath);
        fileDialog.addFileListener(new FileDialog.FileSelectedListener() {
            @Override
            public void fileSelected(File file) {
                String img_file_path=file.toString();
                String filenameArray[] = img_file_path.split("\\.");
                String extension = filenameArray[filenameArray.length-1];
                String[] img = { "png", "jpg", "bmp","PNG", "JPG", "BMP" };

                if (Arrays.asList(img).contains(extension))
                {
                    printRasterImageBT(img_file_path);

                }
                else{
                    Toast.makeText(getApplicationContext(), "invalid file type", Toast.LENGTH_LONG).show();
                }
            }
        });
        fileDialog.showDialog();
    }

	 */

	public void printImageThreeInch(Bitmap originalBitmap) throws IOException {
		Bitmap resizedBitmap = Bitmap.createScaledBitmap(originalBitmap, 577, originalBitmap.getHeight(), false);
		PrintRasterImage PrintRasterImage = new PrintRasterImage(resizedBitmap);
		PrintRasterImage.PrepareImage(example.aem.bluprints.PrintRasterImage.dither.floyd_steinberg, 128);
		byte[] imgStr =PrintRasterImage.getPrintImageData();
		try
		{

			bluetoothSocket.getOutputStream().write(imgStr, 0, imgStr.length);
		}
		catch(IOException e)
		{
			System.out.print("IOException ");
		}
	}

	public void printTextAsImageThreeInch(String TextToConvert) throws IOException
	{
		Bitmap resizedBitmap = null;
		Converter convert = new Converter();
		Bitmap bmp = convert.textAsBitmap(TextToConvert, 20, 5, Color.BLACK,Typeface.MONOSPACE);
		resizedBitmap = Bitmap.createScaledBitmap(bmp, 577, bmp.getHeight(), false);
		PrintRasterImage PrintRasterImage = new PrintRasterImage(resizedBitmap);
		PrintRasterImage.PrepareImage(example.aem.bluprints.PrintRasterImage.dither.floyd_steinberg, 128);
		byte[] imgStr =PrintRasterImage.getPrintImageData();
		//int fileSize = imgStr.length;
		//int quotient = fileSize % 1152;
		//int absSize = fileSize - quotient;
		//int numPackets = absSize / 1152;
		//Log.e("Size: ", String.valueOf(fileSize), String.valueOf(fileSize));
		//byte[] temp = new byte[1160];
		//int s;
		//int index = 0;

	/*	temp[0] = 0x1D;
		temp[1] = 0x76;
		temp[2] = 0x30;
		temp[3] = 0x00;
		temp[4] = 0x30;
		temp[5] = 0x00;
		temp[6] = 0x18;
		temp[7] = 0x00; */
		//s = 8;

		//for (int i = 0; i < numPackets; i++) {
		//	for (int j = 0; j < 1152; j++)
		//		temp[s++] = imgStr[index++];
		//	bluetoothSocket.getOutputStream().write(temp, 0, temp.length);
		//	s = 8;
		//}

		//	for (int i = index; i < fileSize; i++)
		//	temp[s++] = imgStr[index++];

		//	int diff = 1152 - quotient;
		//	for (int i = 0; i < diff; i++)
		//		temp[s++] = 0x00;
		//	bluetoothSocket.getOutputStream().write(temp, 0, temp.length);

		try
		{
			bluetoothSocket.getOutputStream().write(imgStr, 0, imgStr.length);
		}
		catch(IOException e)
		{
			System.out.print("IOException ");
		}

	}

	public void printReverseText(String TextToConvert) throws IOException
	{
		Bitmap resizedBitmap = null;
		Converter convert = new Converter();
		Bitmap bmp = convert.textAsBitmap(TextToConvert, 30, 5, Color.BLACK,Typeface.MONOSPACE);
		Matrix matrix = new Matrix();
		matrix.postRotate(180);
		Bitmap rotatedBitmap=Bitmap.createBitmap(bmp,0, 0, bmp.getWidth(), bmp.getHeight(), matrix, true);
		//resizedBitmap = Bitmap.createScaledBitmap(bmp, 577, bmp.getHeight(), false);
		PrintRasterImage PrintRasterImage = new PrintRasterImage(getResizedBitmap(rotatedBitmap));
		PrintRasterImage.PrepareImage(example.aem.bluprints.PrintRasterImage.dither.floyd_steinberg, 128);
		byte[] imgStr =PrintRasterImage.getPrintImageData();
		try
		{
			bluetoothSocket.getOutputStream().write(imgStr, 0, imgStr.length);
		}
		catch(IOException e)
		{
			System.out.print("IOException ");
		}
	}

	public void printTextAsImage(String TextToConvert) throws IOException {
		Bitmap resizedBitmap = null;
		Converter convert = new Converter();
		Bitmap bmp = convert.textAsBitmap(TextToConvert, 30, 5, Color.BLACK, Typeface.defaultFromStyle(Typeface.NORMAL));
		//resizedBitmap = Bitmap.createScaledBitmap(bmp, 577, bmp.getHeight(), false);
		PrintRasterImage PrintRasterImage = new PrintRasterImage(getResizedBitmap(bmp));
		PrintRasterImage.PrepareImage(example.aem.bluprints.PrintRasterImage.dither.floyd_steinberg, 128);
		byte[] imgStr =PrintRasterImage.getPrintImageData();
		try
		{
			bluetoothSocket.getOutputStream().write(imgStr, 0, imgStr.length);
		}
		catch(IOException e)
		{
			System.out.print("IOException ");
		}

	}


/*
	public void printTextAsImage(String TextToConvert) throws IOException {

		Converter convert = new Converter();
		Bitmap bmp = convert.textAsBitmap(TextToConvert, 30, 5, Color.BLACK,Typeface.MONOSPACE);
		PrintRasterImage PrintRasterImage = new PrintRasterImage(getResizedBitmap(bmp));
		PrintRasterImage.PrepareImage(dither.floyd_steinberg, 128);
		byte[] imgStr =PrintRasterImage.getPrintImageData();

		int fileSize = imgStr.length;
		int quotient = fileSize % 1152;
			int absSize = fileSize - quotient;
			int numPackets = absSize / 1152;
			//Log.e("Size: ", String.valueOf(fileSize), String.valueOf(fileSize));
			byte[] temp = new byte[1160];
			int s;
			int index = 0;

			temp[0] = 0x1D;
			temp[1] = 0x76;
			temp[2] = 0x30;
			temp[3] = 0x00;
			temp[4] = 0x30;
			temp[5] = 0x00;
			temp[6] = 0x18;
			temp[7] = 0x00;
			s = 8;

			for (int i = 0; i < numPackets; i++) {
				for (int j = 0; j < 1152; j++)
					temp[s++] = imgStr[index++];
				bluetoothSocket.getOutputStream().write(temp, 0, temp.length);
				s = 8;

			}

			for (int i = index; i < fileSize; i++)
				temp[s++] = imgStr[index++];

			int diff = 1152 - quotient;
			for (int i = 0; i < diff; i++)
				temp[s++] = 0x00;
			bluetoothSocket.getOutputStream().write(temp, 0, temp.length);
		}

*/

	public Bitmap getResizedBitmap(Bitmap bm)
	{
		int newWidth = 248	;
		int newHeight = 297;
		int reqWidth = (int) Math.round(effectivePrintWidth*8);
		int width = bm.getWidth();
		int height = bm.getHeight();
		if(width==reqWidth){
			return bm;
		}
		else if(width<reqWidth&&width>16){
			int diff = width%8;
			if(diff!=0){
				newWidth = width - diff;
				newHeight = (int) (width - diff)*height/width;
				float scaleWidth = ((float) newWidth) / width;
				float scaleHeight = ((float) newHeight) / height;
				// CREATE A MATRIX FOR THE MANIPULATION
				Matrix matrix = new Matrix();
				// RESIZE THE BIT MAP
				matrix.postScale(scaleWidth, scaleHeight);

				// "RECREATE" THE NEW BITMAP
				Bitmap resizedBitmap = Bitmap.createBitmap(
						bm, 0, 0, width, height, matrix, false);
				bm.recycle();
				return resizedBitmap;
			}
		}
		else if(width>16){
			newWidth = reqWidth;
			newHeight = (int) reqWidth*height/width;
			float scaleWidth = ((float) newWidth) / width;
			float scaleHeight = ((float) newHeight) / height;
			// CREATE A MATRIX FOR THE MANIPULATION
			Matrix matrix = new Matrix();
			// RESIZE THE BIT MAP
			matrix.postScale(scaleWidth, scaleHeight);

			// "RECREATE" THE NEW BITMAP
			Bitmap resizedBitmap = Bitmap.createBitmap(
					bm, 0, 0, width, height, matrix, false);
			bm.recycle();
			return resizedBitmap;
		}
		return bm;
	}

	private boolean deleteFile()
	{
//		File dir = getFilesDir();
		return m_Context.deleteFile("my_monochrome_image.bmp");

	}

	public void printBytes(byte[] printBytes) throws IOException
	{

		if (bluetoothSocket == null)
			return;

		if(printBytes == null)
			return;

		bluetoothSocket.getOutputStream().write(printBytes, 0, printBytes.length);

	}
	public static byte[] byteArraysToBytes(byte[][] data) {
		int length = 0;

		for(int send = 0; send < data.length; ++send) {
			length += data[send].length;
		}

		byte[] var6 = new byte[length];
		int k = 0;
		for(int i = 0; i < data.length; ++i) {
			for(int j = 0; j < data[i].length; ++j) {
				var6[k++] = data[i][j];
			}
		}
		return var6;
	}
}
