package com.capstone.videoeffect;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.capstone.videoeffect.utils.Glob;
import com.capstone.videoeffect.utils.ImageLoadingUtils;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import static android.provider.MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE;
import static android.provider.MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO;

public class PhotoHomeActivity extends AppCompatActivity {

    ImageView ivback;
    LinearLayout clickphoto,uploadPhoto,myphotos;

    public static Bitmap bitmap;
    public static Uri imageUri;
    Uri fileUri;
    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
    String[] PERMISSIONS = {Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.CAMERA};
    int PERMISSION_ALL = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_home);

        clickphoto = findViewById(R.id.clickphoto);
        uploadPhoto = findViewById(R.id.uploadPhoto);
        myphotos = findViewById(R.id.myphotos);

        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());

        ivback = findViewById(R.id.ivback);

        ivback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PhotoHomeActivity.this, HomeActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });

        clickphoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT < 23) {
                    intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                    fileUri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE);
                    Log.d("baby",String.valueOf(fileUri));
                    intent.putExtra(MediaStore.EXTRA_OUTPUT,fileUri);
                    startActivityForResult(intent, 2);
                }
                else {
                    if (checkSelfPermission("android.permission.READ_EXTERNAL_STORAGE") == PackageManager.PERMISSION_GRANTED && checkSelfPermission("android.permission.CAMERA") == PackageManager.PERMISSION_GRANTED ) {
                        Log.d("baby","yes");
                        intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                        fileUri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE);
                        Log.d("baby",String.valueOf(fileUri));
                        intent.putExtra(MediaStore.EXTRA_OUTPUT,fileUri);
                        startActivityForResult(intent, 2);
                    } else{
                        ActivityCompat.requestPermissions(PhotoHomeActivity.this, PERMISSIONS, PERMISSION_ALL);
                    }
                }
//                intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
//                fileUri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE);
//                Log.d("baby",String.valueOf(fileUri));
//                intent.putExtra(MediaStore.EXTRA_OUTPUT,fileUri);
//                startActivityForResult(intent, 2);
            }
        });

        uploadPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT < 23) {
                    openGallery();
                } else if (checkSelfPermission("android.permission.READ_EXTERNAL_STORAGE") == PackageManager.PERMISSION_GRANTED) {
                    openGallery();
                } else if (checkSelfPermission("android.permission.READ_EXTERNAL_STORAGE") != PackageManager.PERMISSION_GRANTED) {
                    requestPermissions(new String[]{"android.permission.READ_EXTERNAL_STORAGE"}, 2);
                }
            }
        });

        myphotos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PhotoHomeActivity.this,MyCreationActivity.class);
                startActivity(intent);
            }
        });
    }

    public Uri getOutputMediaFileUri(int type) {
        return Uri.fromFile(getOutputMediaFile(type));
    }

    /**
     * returning image / video
     */
    private static File getOutputMediaFile(int type) {

        // External sdcard location
        File mediaStorageDir = new File(
                Environment
                        .getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                "Pix");

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                return null;
            }
        }
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss",
                Locale.getDefault()).format(new Date());
        File mediaFile;
        if (type == MEDIA_TYPE_IMAGE) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator
                    + "IMG_" + timeStamp + ".jpg");
        } else if (type == MEDIA_TYPE_VIDEO) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator
                    + "VID_" + timeStamp + ".mp4");
        } else {
            return null;
        }

        return mediaFile;
    }

    private void openGallery() {
        startActivityForResult(new Intent("android.intent.action.PICK", MediaStore.Images.Media.EXTERNAL_CONTENT_URI), 1);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == -1) {
            switch (requestCode) {
                case 1:
                    imageUri = data.getData();
                    new ImageCompressionAsyncTask(true).execute(new String[]{imageUri.toString()});
                    return;
                case 2:
                    Bitmap photo = null;
                    try {
                        photo = MediaStore.Images.Media.getBitmap(this.getContentResolver(), fileUri);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    new ImageCompressionAsyncTask(true).execute(new String[]{fileUri.toString()});
                    bitmap=photo;
                    break;
                default:
                    return;
            }
        }
    }

    class ImageCompressionAsyncTask extends AsyncTask<String, Void, Bitmap> {
        private boolean fromGallery;

        public ImageCompressionAsyncTask(boolean fromGallery) {
            this.fromGallery = fromGallery;
        }

        protected Bitmap doInBackground(String... params) {
            return compressImage(params[0]);
        }

        public Bitmap compressImage(String imageUri) {
            String filePath = getRealPathFromURI(imageUri);
            Log.e("FILE_PATH", filePath);
            Bitmap scaledBitmap = null;
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            Bitmap bmp = BitmapFactory.decodeFile(filePath, options);
            int actualHeight = options.outHeight;
            int actualWidth = options.outWidth;
            float imgRatio = (float) (actualWidth / actualHeight);
            if (((float) actualHeight) > 2048.0f || ((float) actualWidth) > 1440.0f) {
                if (imgRatio < 0.703125f) {
                    actualWidth = (int) (((float) actualWidth) * (2048.0f / ((float) actualHeight)));
                    actualHeight = 2048;
                } else if (imgRatio > 0.703125f) {
                    actualHeight = (int) (((float) actualHeight) * (1440.0f / ((float) actualWidth)));
                    actualWidth = 1440;
                } else {
                    actualHeight = 2048;
                    actualWidth = 1440;
                }
            }
            options.inSampleSize = ImageLoadingUtils.calculateInSampleSize(options, actualWidth, actualHeight);
            options.inJustDecodeBounds = false;
            options.inDither = false;
            options.inPurgeable = true;
            options.inInputShareable = true;
            options.inTempStorage = new byte[16384];
            try {
                bmp = BitmapFactory.decodeFile(filePath, options);
            } catch (OutOfMemoryError exception) {
                exception.printStackTrace();
            }
            try {
                scaledBitmap = Bitmap.createBitmap(actualWidth, actualHeight, Bitmap.Config.ARGB_8888);
            } catch (OutOfMemoryError exception2) {
                exception2.printStackTrace();
            }
            float ratioX = ((float) actualWidth) / ((float) options.outWidth);
            float ratioY = ((float) actualHeight) / ((float) options.outHeight);
            float middleX = ((float) actualWidth) / 2.0f;
            float middleY = ((float) actualHeight) / 2.0f;
            Matrix scaleMatrix = new Matrix();
            scaleMatrix.setScale(ratioX, ratioY, middleX, middleY);
            Canvas canvas = new Canvas(scaledBitmap);
            canvas.setMatrix(scaleMatrix);
            canvas.drawBitmap(bmp, middleX - ((float) (bmp.getWidth() / 2)), middleY - ((float) (bmp.getHeight() / 2)), new Paint(2));
            try {
                int orientation = new ExifInterface(filePath).getAttributeInt("Orientation", 0);
                Log.d("EXIF", "Exif: " + orientation);
                Matrix matrix = new Matrix();
                if (orientation == 6) {
                    matrix.postRotate(90.0f);
                    Log.d("EXIF", "Exif: " + orientation);
                } else if (orientation == 3) {
                    matrix.postRotate(Glob.HUE_CYAN);
                    Log.d("EXIF", "Exif: " + orientation);
                } else if (orientation == 8) {
                    matrix.postRotate(Glob.HUE_VIOLET);
                    Log.d("EXIF", "Exif: " + orientation);
                }
                scaledBitmap = Bitmap.createBitmap(scaledBitmap, 0, 0, scaledBitmap.getWidth(), scaledBitmap.getHeight(), matrix, true);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return scaledBitmap;
        }

        private String getRealPathFromURI(String contentURI) {
            Uri contentUri = Uri.parse(contentURI);
            Cursor cursor = getContentResolver().query(contentUri, null, null, null, null);
            if (cursor == null) {
                return contentUri.getPath();
            }
            cursor.moveToFirst();
            return cursor.getString(cursor.getColumnIndex("_data"));
        }

        protected void onPostExecute(Bitmap result) {
            super.onPostExecute(result);
            bitmap = result;
            SharedPreferences sPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
            Intent cropIntent = new Intent(PhotoHomeActivity.this, CropActivity.class);
            if (sPreferences.getBoolean("isHair", false)) {
                cropIntent.putExtra("hair", false);
                startActivity(cropIntent);
                return;
            }
            cropIntent.putExtra("hair", false);
            startActivity(cropIntent);
        }
    }
}