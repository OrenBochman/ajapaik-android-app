package ee.ajapaik.android;

import android.content.ComponentName;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.ContextCompat;

import ee.ajapaik.android.fragment.AlbumFragment;
import ee.ajapaik.android.fragment.NearestFragment;
import ee.ajapaik.android.fragment.util.AlertFragment;
import ee.ajapaik.android.util.Settings;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.content.pm.PackageManager.PERMISSION_GRANTED;

public class NearestActivity extends AlbumActivity {
    private static final int DIALOG_ERROR_LOCATION_DISABLED = 2;
    private static final int ACCESS_FINE_LOCATION_PERMISSION = 6000;

    private static final int MIN_DISTANCE_IN_METERS = 100;

    private final LocationService.Connection m_connection = new LocationService.Connection() {
        public void onLocationChanged(Location newLocation) {
            Settings settings = getSettings();
            Location oldLocation = settings.getLocation();

            AlbumFragment fragment = (AlbumFragment) getFragment();
            if (shouldLoadNearestPhotos(newLocation, oldLocation, fragment)) {
                settings.setLocation(newLocation);

                if(fragment != null) {
                    fragment.invalidate();
                }
            }
        }

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            super.onServiceConnected(name, service);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (!isEnabled() && checkSelfPermission(ACCESS_FINE_LOCATION) == PERMISSION_GRANTED) {
                    showDialogFragment(DIALOG_ERROR_LOCATION_DISABLED);
                }
            } else {
                if (!isEnabled()) {
                    showDialogFragment(DIALOG_ERROR_LOCATION_DISABLED);
                }
            }
        }
    };

    private boolean shouldLoadNearestPhotos(Location newLocation, Location oldLocation, AlbumFragment fragment) {
        return oldLocation == null || oldLocation.distanceTo(newLocation) > MIN_DISTANCE_IN_METERS
                || fragment == null || fragment.getAlbum() == null;
    }

    protected AlbumFragment createFragment() {
        return new NearestFragment();
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (ContextCompat.checkSelfPermission(this, ACCESS_FINE_LOCATION) != PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{ACCESS_FINE_LOCATION}, ACCESS_FINE_LOCATION_PERMISSION);
        } else {
            m_connection.connect(this);
        }
        this.setTitle(getResources().getString(R.string.nearest_title));
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        switch (requestCode) {
            case ACCESS_FINE_LOCATION_PERMISSION: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                   if (!((LocationManager)getSystemService(LOCATION_SERVICE)).isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                       showDialogFragment(DIALOG_ERROR_LOCATION_DISABLED);
                   } else {
                       m_connection.connect(this);
                   }
                } else if (isDoNotAskAgainChecked()) {
                    showDialogFragment(DIALOG_ERROR_LOCATION_DISABLED);
                } else {
                    AlbumsActivity.start(this);
                }
            }
        }
    }

    private boolean isDoNotAskAgainChecked() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !shouldShowRequestPermissionRationale(ACCESS_FINE_LOCATION);
    }

    @Override
    protected void onStop() {
        m_connection.disconnect(this);
        super.onStop();
    }

    @Override
    protected DialogFragment createDialogFragment(int requestCode) {
        if(requestCode == DIALOG_ERROR_LOCATION_DISABLED) {
            return AlertFragment.create(
                    getString(R.string.nearest_dialog_error_location_disabled_title),
                    getString(R.string.nearest_dialog_error_location_disabled_message),
                    getString(R.string.nearest_dialog_error_location_disabled_close),
                    getString(R.string.nearest_dialog_error_location_disabled_open));
        }

        return super.createDialogFragment(requestCode);
    }

    @Override
    public void onDialogFragmentDismissed(DialogFragment fragment, int requestCode, int resultCode) {
        if(requestCode == DIALOG_ERROR_LOCATION_DISABLED) {
            if(resultCode == AlertFragment.RESULT_POSITIVE) {
                LocationService.startSettings(this);
            } else {
                AlbumsActivity.start(this);
            }
            return;
        }

        super.onDialogFragmentDismissed(fragment, requestCode, resultCode);
    }

    @Override
    public void onDialogFragmentCancelled(DialogFragment fragment, int requestCode) {
        if(requestCode == DIALOG_ERROR_LOCATION_DISABLED) {
            AlbumsActivity.start(this);
        }

        super.onDialogFragmentCancelled(fragment, requestCode);
    }
}
