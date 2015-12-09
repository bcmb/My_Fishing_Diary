package net.validcat.fishing.camera;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.ImageView;

import net.validcat.fishing.data.Constants;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Denis on 07.07.2015.
 */
public class CameraManager {
    public static final String LOG_TAG = CameraManager.class.getSimpleName();

    private AlbumStorageDirFactory mAlbumStorageDirFactory = null;
    private String mCurrentPhotoPath;

    private File getAlbumDir() {
        File storageDir = null;
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
            storageDir = mAlbumStorageDirFactory.getAlbumStorageDir(getAlbumName());
            if (storageDir != null)
                if (!storageDir.mkdirs())
                    if (!storageDir.exists()){
                        Log.d("CameraSample", "failed to create directory");
                        return null;
                    }
        } else {
            Log.v(LOG_TAG, "External storage is not mounted READ/WRITE.");
        }

        return storageDir;
    }

    private String getAlbumName() {
        return Constants.FOLDER_NAME;
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        String imageFileName = timeStamp + "_"; //Constants.EXTENSION_JPG +
        File albumF = getAlbumDir();
        File imageF = File.createTempFile(imageFileName, Constants.EXTENSION_JPG, albumF);
        return imageF;
    }

    private File setUpPhotoFile() throws IOException {
        File f = createImageFile();
        mCurrentPhotoPath = f.getAbsolutePath();

        return f;
    }

    public void startCameraForResult(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.FROYO) {
            mAlbumStorageDirFactory = new FroyoAlbumDirFactory();
        } else {
            mAlbumStorageDirFactory = new BaseAlbumDirFactory();
        }

        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        File f = null;
        try {
            f = setUpPhotoFile();
            mCurrentPhotoPath = f.getAbsolutePath();
            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
        } catch (IOException e) {
            e.printStackTrace();
            f = null;
            mCurrentPhotoPath = null;
        }

//        if (hasPermissionInManifest(activity, "android.permission.CAMERA"));
        activity.startActivityForResult(takePictureIntent, Constants.REQUEST_CODE_PHOTO);
    }


    public boolean hasPermissionInManifest(Context context, String permissionName) {
        final String packageName = context.getPackageName();
        try {
            final PackageInfo packageInfo = context.getPackageManager()
                    .getPackageInfo(packageName, PackageManager.GET_PERMISSIONS);
            final String[] declaredPermisisons = packageInfo.requestedPermissions;
            if (declaredPermisisons != null && declaredPermisisons.length > 0) {
                for (String p : declaredPermisisons) {
                    if (p.equals(permissionName)) {
                        return true;
                    }
                }
            }
        } catch (PackageManager.NameNotFoundException e) {

        }
        return false;
    }

    private void handleSmallCameraPhoto(Intent intent, ImageView ivPhoto) {
        Bundle extras = intent.getExtras();
        Bitmap bitmap = (Bitmap) extras.get("data");
        ivPhoto.setImageBitmap(bitmap);
    }

    private void handleBigCameraPhoto(Context context, ImageView ivPhoto) {
        if (mCurrentPhotoPath != null) {
            setPic(ivPhoto);
            galleryAddPic(context);
            mCurrentPhotoPath = null;
        }
    }

    private void setPic(ImageView ivPhoto) {
		/* There isn't enough memory to open up more than a couple camera photos */
		/* So pre-scale the target bitmap into which the file is decoded */
		/* Get the size of the ImageView */
        int targetW = ivPhoto.getWidth();
        int targetH = ivPhoto.getHeight();

		/* Get the size of the image */
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);
        int photoW = bmOptions.outWidth;
        int photoH = bmOptions.outHeight;

		/* Figure out which way needs to be reduced less */
        int scaleFactor = 1;
        if ((targetW > 0) || (targetH > 0)) {
            scaleFactor = Math.min(photoW/targetW, photoH/targetH);
        }

		/* Set bitmap options to scale the image decode target */
        bmOptions.inJustDecodeBounds = false;
        bmOptions.inSampleSize = scaleFactor;
        bmOptions.inPurgeable = true;

		/* Decode the JPEG file into a Bitmap */
        Bitmap bitmap = BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);

        try {
            ExifInterface ei = new ExifInterface(mCurrentPhotoPath);
            int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
            Log.d(LOG_TAG, "Orientaton = " + orientation);

            int degree = 0;

            switch (orientation) {
                case ExifInterface.ORIENTATION_NORMAL:
                    degree = 0;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_90:
                    degree = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    degree = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    degree = 270;
                    break;
                case ExifInterface.ORIENTATION_UNDEFINED:
                    degree = 0;
                    break;
                default:
                    degree = 90;
            }
            Bitmap photo = rotateImageIfRequired(bitmap, degree);
            photo = cropToSquare(photo);
//            ivPhoto.setImageBitmap(rotateImageIfRequired(bitmap, degree));
            ivPhoto.setImageBitmap(photo);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Rotate an image if required.
     * @param img
     * @return
     */
    private static Bitmap rotateImageIfRequired(Bitmap img, int rotation) {
        // Detect rotation
        if (rotation != 0) {
            Matrix matrix = new Matrix();
            matrix.postRotate(rotation);
            Bitmap rotatedImg = Bitmap.createBitmap(img, 0, 0, img.getWidth(), img.getHeight(), matrix, true);
            img.recycle();

            return rotatedImg;
        } else {
            return img;
        }
    }

    /**
     * Get the rotation of the last image added.
     * @param context
     * @return
     */
    private static int getRotation(Context context) {
        int rotation =0;
        ContentResolver content = context.getContentResolver();
        Cursor mediaCursor = content.query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                new String[] { "orientation", "date_added" },null, null,"date_added desc");

        if (mediaCursor != null && mediaCursor.getCount() !=0 ) {
            while(mediaCursor.moveToNext()){
                rotation = mediaCursor.getInt(0);
                break;
            }
        }
        mediaCursor.close();
        return rotation;
    }

    private void galleryAddPic(Context context) {
        Intent mediaScanIntent = new Intent("android.intent.action.MEDIA_SCANNER_SCAN_FILE");
        File f = new File(mCurrentPhotoPath);
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        context.sendBroadcast(mediaScanIntent);
    }

    public void setPhotoToImageView(Context context, int requestCode, ImageView ivPhoto) {
        if (requestCode == Constants.REQUEST_CODE_PHOTO) {
            handleBigCameraPhoto(context, ivPhoto);
        }
    }

    public static Bitmap scaleDownBitmap(Bitmap photo, int newHeight, Context context){
        final float densityMultiplier = context.getResources().getDisplayMetrics().density;
        int h = (int) (newHeight*densityMultiplier);
        int w = (int) (h*photo.getWidth()/((double)photo.getHeight()));
        photo = Bitmap.createScaledBitmap(photo, w, h, true);

        return photo;
    }

    public static Bitmap cropToSquare (Bitmap bitmap){
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        int newWidth = (height > width)? width:height;
        int newHeight = (height>width)?height - (height-width):height;
        int cropW = (width-height)/2;
        cropW = (cropW<0)?0:cropW;
        int cropH = (height-width)/2;
        cropH = (cropH<0)?0:cropH;
        Bitmap cropImg = Bitmap.createBitmap(bitmap,cropW,cropH,newWidth,newHeight);

        return cropImg;
    }

    //TODO add to activity
//    // Some lifecycle callbacks so that the image can survive orientation change
//    @Override
//    protected void onSaveInstanceState(Bundle outState) {
//        outState.putParcelable(BITMAP_STORAGE_KEY, mImageBitmap);
//        outState.putParcelable(VIDEO_STORAGE_KEY, mVideoUri);
//        outState.putBoolean(IMAGEVIEW_VISIBILITY_STORAGE_KEY, (mImageBitmap != null) );
//        outState.putBoolean(VIDEOVIEW_VISIBILITY_STORAGE_KEY, (mVideoUri != null) );
//        super.onSaveInstanceState(outState);
//    }
//
//    @Override
//    protected void onRestoreInstanceState(Bundle savedInstanceState) {
//        super.onRestoreInstanceState(savedInstanceState);
//        mImageBitmap = savedInstanceState.getParcelable(BITMAP_STORAGE_KEY);
//        mVideoUri = savedInstanceState.getParcelable(VIDEO_STORAGE_KEY);
//        mImageView.setImageBitmap(mImageBitmap);
//        mImageView.setVisibility(
//                savedInstanceState.getBoolean(IMAGEVIEW_VISIBILITY_STORAGE_KEY) ?
//                        ImageView.VISIBLE : ImageView.INVISIBLE
//        );
//        mVideoView.setVideoURI(mVideoUri);
//        mVideoView.setVisibility(
//                savedInstanceState.getBoolean(VIDEOVIEW_VISIBILITY_STORAGE_KEY) ?
//                        ImageView.VISIBLE : ImageView.INVISIBLE
//        );
//    }

}