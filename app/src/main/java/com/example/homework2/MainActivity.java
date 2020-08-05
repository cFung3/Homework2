package com.example.homework2;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import org.opencv.android.OpenCVLoader;
import org.opencv.android.Utils;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.QRCodeDetector;

//Casper Fung
public class MainActivity extends AppCompatActivity {

    ImageView imgView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (!OpenCVLoader.initDebug()) {
            Log.d("Casper test", "OpenCV has error");
        } else {
            Log.d("Casper test", "OpenCV is fine");
        }

        imgView = findViewById(R.id.imageView);
        Button btn = findViewById(R.id.button);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final Bitmap bit = ((BitmapDrawable) imgView.getDrawable()).getBitmap();
                String stringResultFromQRCode = decodeQRCode(bit);
                String[] lines = stringResultFromQRCode.split(";");

                Mat img = null;
                Mat mat = new Mat(bit.getWidth(), bit.getHeight(), CvType.CV_8UC4);
                Utils.matToBitmap(mat, bit);

                int lineWidth = 3;
                Scalar lineColor = new Scalar(255,0,0,255);
                int count = 0;

                while (count < lines.length){
                    Point[] poi = { new Point(), new Point()};
                    String[] str = lines[count].split(" ");
                    for (int i = 0; i < str.length; i++){
                        String[] x = str[i].split(",");
                        poi[i] = new Point (Integer.valueOf (x[0]),Integer.valueOf(x[1]));

                    }

                    count ++;
                    Imgproc.line(mat, poi[0], poi[1], lineColor, lineWidth);

                }

                Bitmap bitmap = Bitmap.createBitmap(mat.width(), mat.height(), Bitmap.Config.ARGB_8888);
                Utils.matToBitmap(mat, bitmap);
                ImageView imgView = findViewById(R.id.imageView);
                imgView.setImageBitmap(bitmap);

            }
        });
    }

    String decodeQRCode(Bitmap bitmap) {
        Mat mat = new Mat(bitmap.getWidth(), bitmap.getHeight(), CvType.CV_8UC4);
        Utils.bitmapToMat(bitmap, mat);
        QRCodeDetector qrCodeDetector = new QRCodeDetector();
        String result = qrCodeDetector.detectAndDecode(mat);
        Log.d("Casper test", ""+result);
        return result;

    }
}