package com.sixfifty.drunkmehome;

import java.io.IOException;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.opengl.Visibility;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class CurrentLocationActivity extends Activity implements LocationListener {
    private static final int LOC_UPDATE_MIN_DISTANCE = 5;
	private static final int LOC_UPDATE_MIN_TIME = 65000;
	private LocationManager locationManager;
	private TextView addressText;
	private TextView locationName;
	private Button btnSaveLoc;
	private AlertDialog alert; 
	private Location currentLoc;
	private TextView txtCurrent;
	private Button btnClear;
	
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        setUpViews();
        setUpLocation();
    }

	private void setUpViews() {
		txtCurrent = (TextView)findViewById(R.id.location_text);
		addressText = (TextView)findViewById(R.id.address_text);
		btnSaveLoc = (Button)findViewById(R.id.save_location);
		btnClear = (Button)findViewById(R.id.clear_location);
		
		btnSaveLoc.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Geocoder gcoder = new Geocoder(CurrentLocationActivity.this);
				List<Address> addresses;
				try {
					addresses = gcoder.getFromLocation(currentLoc.getLatitude(), currentLoc.getLongitude(), 1);

					if (addresses != null && addresses.size() >= 1) {
						addressText.setText(buildTextFromAddress(addresses.get(0)));
						addressText.setVisibility(View.VISIBLE);
						btnClear.setVisibility(View.VISIBLE);
					}
//					AlertDialog.Builder builder = new AlertDialog.Builder(CurrentLocationActivity.this);
//					builder.setTitle("Verify your location");
//					final CharSequence[] items = new CharSequence[1];
//					for (int i = 0; i < items.length; i++) {
//						items[i] = addresses.get(i).getFeatureName() + addresses.get(i).getAddressLine(0);
//					}
//					builder.setSingleChoiceItems(items, -1, new DialogInterface.OnClickListener() {
//					    public void onClick(DialogInterface dialog, int item) {
//					        Toast.makeText(getApplicationContext(), items[item], Toast.LENGTH_SHORT).show();
//					        CurrentLocationActivity.this.finishChoice(items[item]);
//					    }
//					});
//					alert = builder.create();
//					alert.show();
				} catch(IOException ioe) {
					
				}
			}
		});
		
		btnClear.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				addressText.setText("");
				addressText.setVisibility(View.GONE);
				btnClear.setVisibility(View.GONE);
			}
		});
	}
	
	private CharSequence buildTextFromAddress(Address address) {
		StringBuilder addrText = new StringBuilder();
		for (int i = 0; i < 7; i++) {
			if (address.getAddressLine(i) != null) {
				addrText.append(address.getAddressLine(i) + "\n");
			} else {
				break;
			}
		}
		return addrText.toString();
	}
	
	private void setUpLocation() {
		locationManager = (LocationManager)getSystemService(LOCATION_SERVICE);
		requestLocationUpdates();
	}

	private void requestLocationUpdates() {
		locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, LOC_UPDATE_MIN_TIME, LOC_UPDATE_MIN_DISTANCE, this);
	}

	@Override
	public void onLocationChanged(Location location) {
		String locationString = String.format(
				"@ %f, %f +/- %fm",
				location.getLatitude(),
				location.getLongitude(),
				location.getAccuracy());
		txtCurrent.setText(locationString);
		currentLoc = location;
		btnSaveLoc.setVisibility(View.VISIBLE);
	}

	@Override
	public void onProviderDisabled(String provider) {}

	@Override
	public void onProviderEnabled(String provider) {}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {}
}