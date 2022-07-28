package com.example.qrcards;

import androidx.annotation.NonNull;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.SurfaceView;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.budiyev.android.codescanner.CodeScanner;
import com.budiyev.android.codescanner.CodeScannerView;
import com.budiyev.android.codescanner.DecodeCallback;
import com.google.android.gms.vision.CameraSource;

import com.google.zxing.Result;
import com.google.zxing.qrcode.QRCodeReader;

import java.text.SimpleDateFormat;
import java.util.Date;

public class ScannerActivity extends AppCompatActivity {


    QRCodeReader qrCodeReader;
    CameraSource cameraSource;
    SurfaceView surfaceView;
    TextView qr_value;
    String contantsString;
    CodeScanner mCodeScanner;
    Record record;
    String the_state;
    private static final int REQUEST_CAMERA_PERMISSION = 201;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scanner);

       the_state= getIntent().getStringExtra("state");

       getIntent().removeExtra("state");

        int permission= ActivityCompat.checkSelfPermission(ScannerActivity.this,
                Manifest.permission.CAMERA);
        if(permission== PackageManager.PERMISSION_DENIED){

            ActivityCompat.requestPermissions(ScannerActivity.this,
                    new String[]{Manifest.permission.CAMERA},100);

        }


        CodeScannerView scannerView = findViewById(R.id.scanner_view);
        mCodeScanner = new CodeScanner(this, scannerView);
        mCodeScanner.setDecodeCallback(new DecodeCallback() {
            @Override
            public void onDecoded(@NonNull final Result result) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(ScannerActivity.this, result.getText(), Toast.LENGTH_SHORT).show();
                        if(result.getText()!=null){
                            Intent intent=new Intent(ScannerActivity.this,QrListActivity.class);

                            SimpleDateFormat simpleDateFormat=new SimpleDateFormat("EEE,d MMM yyyy HH:mm:s a");
                            Date date=new Date();
                            record=new Record();
                            record.setName(result.getText());
                            record.setQr_code_value(result.getText());
                            record.setDate(simpleDateFormat.format(date));
                            record.setState(the_state);
                            record.setNumber(1);
                            intent.putExtra("record_value",record);
                            setResult(Activity.RESULT_OK,intent);
                            finish();

                        }

                    }
                });
            }
        });
        scannerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCodeScanner.startPreview();
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        mCodeScanner.startPreview();
    }

    @Override
    protected void onPause() {
        mCodeScanner.releaseResources();
        super.onPause();
    }
}