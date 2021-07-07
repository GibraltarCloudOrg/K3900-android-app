package com.example.mauiviewcontrol;

import android.graphics.Bitmap;
import android.util.Log;
import android.widget.ImageView;

import java.time.LocalDateTime;
import java.util.Arrays;

public class ImageStreamer {
    private static ImageStreamer singletonInstance = null;
    public static ImageStreamer getImageStreamerSingletonInstance() {
        if (null == singletonInstance)
            singletonInstance = new ImageStreamer();
        return singletonInstance;
    }

    public final Bitmap getImage() {
        //K3900.ImageRequest.Builder imageRequest;
        //long nextTime = 0;
        //imageRequest = K3900.ImageRequest.newBuilder().setTime(mNextTime);
        //Log.d(TAG, "mNextTime: " + mNextTime);
        try {
            if (!mBackend.isAvailable()) {
                ElapseTimeStatisticsModel.append(mNextTime, "Disconnected from the Server.");
                return null;
            }
            ElapseTimeStatisticsModel.start(mNextTime, "Trying getImage()");
            Image img = mBackend.getImage(mNextTime);
            //Image img = mBackend.getImage(imageRequest);
            if (null == img) {
                ElapseTimeStatisticsModel.append(mNextTime, "getImage() failed.");
                return null;
            }
            else {
                ElapseTimeStatisticsModel.end(mNextTime, "getImage() completed.");
            }
            //CineLoop.update(findViewById(R.id.cineLoopSeekBar));
            mNextTime = img.getTime() + 1;
            if (0 >= mNextTime)
                mNextTime = 1;
            //float x1 = mBackend.getPixelSizeX();
            //float x2 = mBackend.getPixelSizeY();
            //float  x3 = mBackend.getScale();
            //int x4 = mImageView.getWidth();
            //int x5 = mImageView.getHeight();
            //float pixelSizeMmX = 1024 * mBackend.getPixelSizeX() / (mBackend.getScale() * mImageView.getWidth());
            //float pixelSizeMmY = 1024 * mBackend.getPixelSizeY() / (mBackend.getScale() * mImageView.getHeight());
            //int width = (int) (512 / sqrt((int)mBackend.getScale()));
            //int height = (int) (512 / sqrt((int)mBackend.getScale()));
            final int width = 512;
            final int height = 512;
            final int size = width * height;
            //int size = (int)(512 * 512 / mBackend.getScale());
            //int size = (int)(width * height / mBackend.getScale());
            byte[] byteArray = new byte[size];
            int[] color = new int[size];
            byteArray = Arrays.copyOf(img.getData(), size);
            for (int i = 0; i < size; i++) {
                int b = ((int) (byteArray[i])) & 0xff;
                color[i] = 0xFF000000 | (b << 16) | (b << 8) | b;
            }
            return Bitmap.createBitmap(color, width, height, Bitmap.Config.ARGB_8888);
            //return Bitmap.createBitmap(color, 512, 512, Bitmap.Config.ARGB_8888);
        } catch (ImageNotAvailableException e) {
            Log.d(TAG, LocalDateTime.now() + " : " + "Image Not Available at Time: " + mNextTime);
            //Log.d(TAG, LocalDateTime.now() + " : " + "Image Not Available at Time: " + imageRequest.getTime());
            //throw e;
        } catch (Exception e) {
            Log.d(TAG, LocalDateTime.now() + " : " + "Lost Communication: Get image failed");
            //throw new LostCommunicationException("Lost Communication: Get image failed", e);
        }
        return null;
    }

    public void updateImaging() {
        if (!mActivateGetImage || null == mImageView)
            return;
        mStopTimer = true;
        try {
            Runnable runnable = new Runnable() {
                public void run() {
                    while (!mStopImaging) {
                        try {
                            if (mBackend.isBatchMode() /*|| mBackend.isTxRunning() || mBackend.isRxRunning()*/)
                                continue;
                            //Log.d(TAG, "updateImaging() BEFORE setUpForImaging.");
                            //setUpForImaging();
                            //Log.d(TAG, "updateImaging() AFTER setUpForImaging.");
                            //final ImageView image = findViewById(R.id.mainImageView);
                            //Log.d(TAG, "updateImaging() BEFORE getImage.");
                            final Bitmap bmap = getImage();
                            //Log.d(TAG, "updateImaging() AFTER getImage.");
                            if (!mEnableImaging || null == bmap)
                                continue;
                            //Log.d(TAG, "updateImaging() BEFORE image.post.");
                            mImageView.post(new Runnable() {
                                @Override
                                public void run() {
                                    if (null != mImageView && mEnableImaging && null != bmap)
                                        mImageView.setImageBitmap(bmap);
                                }
                            });
                            //Log.d(TAG, "updateImaging() AFTER image.post.");
                            //if (mEnableDisplay)
                            //image.setImageBitmap(bmap);
                        } catch (ImageNotAvailableException e) {
                            //Toast.makeText(this, "Image Not Available, Time: " + mNextTime, Toast.LENGTH_LONG).show();
                            Log.d(TAG, e.getMessage());
                        } catch (Exception e) {
                            Log.d(TAG, e.getMessage());
                            //throw new LostCommunicationException("Lost Communication: Get image failed", e);
                        }
                        waitFor(BeamformerClient.getMonitoringDurationMilliSecondsForImaging());
                        //mStopImaging = true;
                    }
                }
            };
            Thread mythread = new Thread(runnable);
            mythread.start();
        }  catch (LostCommunicationException le) {
            Log.d(TAG, le.getCause().getMessage());
            Log.d(TAG, le.getMessage());
        } catch (ImageNotAvailableException ie) {
            Log.d(TAG, ie.getMessage());
        }
    }

    public void clear() {
        mImageView.setImageBitmap(null);
    }

    public void waitFor(int milliSeconds) {
        milliSeconds = milliSeconds < 0 ? 0 : milliSeconds;
        try {
            Thread.sleep(milliSeconds);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //public boolean getActivateGetImage() { return mActivateGetImage; }
    public void setActivateGetImage(boolean activateGetImage) { mActivateGetImage = activateGetImage; }
    //public boolean getEnableImaging() { return mEnableImaging; }
    public void setEnableImaging(boolean enableImaging) { mEnableImaging = enableImaging; }
    public boolean getStopImaging() { return mStopImaging; }
    public void setStopImaging(boolean stopImaging) { mStopImaging = stopImaging; }
    public boolean getStopTimer() { return mStopTimer; }
    public void setStopTimer(boolean stopTimer) { mStopTimer = stopTimer; }
    public ImageView getImageView() { return mImageView; }
    public void setImageView(ImageView imageView) {
        mImageView = imageView;
    }

    public static final String TAG = "Maui-viewer Image Streamer";
    private boolean mStopImaging = true;
    private boolean mStopTimer = false;
    private long mNextTime = 0;
    private boolean mActivateGetImage = true;
    //private boolean mEnableDisplay = true;
    private boolean mEnableImaging = true;
    private ImageView mImageView = null;
    SwitchBackEndModel mBackend = SwitchBackEndModel.getSwitchBackEndModelSingletonInstance();
}
