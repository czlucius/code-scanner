package com.czlucius.scan.objects;

import android.content.Context;
import android.content.pm.PackageManager;

import androidx.camera.core.CameraControl;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.UseCase;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.LifecycleOwner;

import com.czlucius.scan.callbacks.CameraFailureCallback;
import com.czlucius.scan.callbacks.UseCaseCreator;
import com.czlucius.scan.exceptions.NoCameraException;
import com.czlucius.scan.exceptions.ReferenceInvalidException;
import com.google.common.util.concurrent.ListenableFuture;

import java.lang.ref.WeakReference;
import java.util.concurrent.ExecutionException;

public class CamAccess {
    private Availability mFlash;
    private WeakReference<Context> wctx;
    private boolean mHasCamera;
    private UseCaseCreator mUseCaseCreator;
    private CameraControl cameraControl;

    public CamAccess(final WeakReference<Context> wctx, final UseCaseCreator useCaseCreator) throws NoCameraException {

        this.wctx = wctx;
        assert wctx.get() != null;

        if (!wctx.get().getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)){
            mHasCamera = false;
            throw new NoCameraException("No camera available");
        } else {
            mHasCamera = true;
        }


        if (wctx.get().getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH)){
            mFlash = Availability.OFF;
        } else {
            mFlash = Availability.UNAVAILABLE;
        }

        mUseCaseCreator = useCaseCreator;



    }

    public void startCamera(LifecycleOwner lifecycleOwner, CameraFailureCallback cameraFailureCallback) throws ReferenceInvalidException {
        if (wctx.get() == null) {
            throw new ReferenceInvalidException("Weak reference to context is null.");
        }
        final ListenableFuture<ProcessCameraProvider> cpf = ProcessCameraProvider.getInstance(wctx.get());


        cpf.addListener(() -> {

            // Retrieving the Camera Provider
            ProcessCameraProvider cp;
            try {
                cp = cpf.get();
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
                cp = null;
            }



            // Create the CameraX use cases with the supplied function
            UseCase[] useCases = mUseCaseCreator.create();
            CameraSelector cs = CameraSelector.DEFAULT_BACK_CAMERA;



            try {
                if (cp == null) {
                    throw new NullPointerException("Camera Provider Unavailable");
                }

                cp.unbindAll();
                cameraControl = cp.bindToLifecycle(lifecycleOwner, cs, useCases[0], useCases[1]).getCameraControl();
            } catch (Exception e) {
                cameraFailureCallback.run(e);
                e.printStackTrace();
            }

        }, ContextCompat.getMainExecutor(wctx.get()));

    }

    public void toggleFlash(Availability state) {
        if (state == Availability.UNAVAILABLE) {
            throw new RuntimeException("This must be an error in the code. Please do not set flash state to unavailable. Contact the app developers immediately if you see this message.");
        } else {
            if (cameraControl != null) {
                mFlash = state;
                cameraControl.enableTorch(state.toBoolean());
            }
        }
    }
    public Availability getFlash() {
        return mFlash;
    }

    public boolean hasCamera() {
        return mHasCamera;
    }
}
