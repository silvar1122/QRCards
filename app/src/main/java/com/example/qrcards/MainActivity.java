package com.example.qrcards;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;


import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

public class MainActivity extends AppCompatActivity {

    String content="Elizabethdhfjkhgfdlhdfg";
    ImageView qr_image;
    EditText qr_text_input;
    Button generate_qn,share_qr,scan_qr;
    Bitmap bmp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();

        generate_qn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                GenerateQR(qr_text_input.getText().toString());
            }
        });

        share_qr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String imgBitmapPath= MediaStore.Images.Media.insertImage(getContentResolver(),bmp,"title",null);
                Uri imgBitmapUri=Uri.parse(imgBitmapPath);
                Intent shareIntent=new Intent(Intent.ACTION_SEND);
                shareIntent.setType("image/*");
                shareIntent.putExtra(Intent.EXTRA_STREAM,imgBitmapUri);
                startActivity(Intent.createChooser(shareIntent,"share image using"));
            }
        });

        scan_qr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(MainActivity.this,ScannerActivity.class);
                startActivity(intent);





            }
        });

    }
    public void init(){
        scan_qr=(Button)findViewById(R.id.scan_qr_arrival);
        qr_image=(ImageView)findViewById(R.id.qr_image);
        share_qr=(Button)findViewById(R.id.share_img);
        qr_text_input=(EditText) findViewById(R.id.qr_text_input);
        generate_qn=(Button)findViewById(R.id.generate_qr_code);
    }
    public void GenerateQR(String text){

        QRCodeWriter qrCodeWriter=new QRCodeWriter();
        try {
            BitMatrix bitMatrix = qrCodeWriter.encode(text ,BarcodeFormat.QR_CODE, 512, 512);
            int width = bitMatrix.getWidth();
            int height = bitMatrix.getHeight();
            bmp = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
            for (int x = 0; x < width; x++) {
                for (int y = 0; y < height; y++) {
                    bmp.setPixel(x, y, bitMatrix.get(x, y) ? Color.BLACK : Color.WHITE);
                }
            }
            qr_image.setImageBitmap(bmp);

        } catch (WriterException e) {
            e.printStackTrace();
        }
    }
}