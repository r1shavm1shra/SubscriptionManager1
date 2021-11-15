package com.group10.myapplication.ui.login;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Surface;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;

import com.group10.myapplication.R;
import com.group10.myapplication.StringUtils;
import com.group10.myapplication.data.model.UserAccount;
import com.group10.myapplication.data.UserAccountViewModel;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import timber.log.Timber;

import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.provider.Settings;
import android.util.Log;

import java.io.IOException;
import java.util.Currency;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * Fragment for user account creation.
 */

//Call GPS Currency() to get the user's currency

public class AccountFragment extends Fragment implements View.OnClickListener {
    private EditText mEtUsername;
    private EditText mEtPassword;
    private EditText mEtConfirm;
    private TextView mTvCurrency;

    private UserAccountViewModel mUserAccountViewModel;


	LocationManager locationManager;
	Context mContext;
	Location location;

    private final String TAG = getClass().getSimpleName();

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
    }

	@Override
	public void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Activity activity = requireActivity();
		mUserAccountViewModel = new ViewModelProvider((ViewModelStoreOwner) activity).get(UserAccountViewModel.class);
	}

	@Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_account, container, false);

        Activity activity = requireActivity();
		int rotation = activity.getWindowManager().getDefaultDisplay().getRotation();

		mEtUsername = v.findViewById(R.id.username);
		mEtPassword = v.findViewById(R.id.password);
		mEtConfirm = v.findViewById(R.id.password_confirm);
		mTvCurrency = v.findViewById(R.id.show_currency);
		mTvCurrency.setText("Currency: " + GPSCurrency());

		Button btnAdd = v.findViewById(R.id.done_button);
		btnAdd.setOnClickListener(this);
		Button btnCancel = v.findViewById(R.id.cancel_button);
		btnCancel.setOnClickListener(this);
		setOnFocusChangeListener(mEtUsername,"Enter Username");
		setOnFocusChangeListener(mEtPassword,"Enter Password");
		setOnFocusChangeListener(mEtConfirm,"Retype Password");

		Button btnExit = v.findViewById(R.id.exit_button);
		if (rotation == Surface.ROTATION_0 || rotation == Surface.ROTATION_180) {
			btnExit.setOnClickListener(this);
		}
		else {
			btnExit.setVisibility(View.GONE);
			btnExit.invalidate();
		}

		return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        try {
            AppCompatActivity activity = (AppCompatActivity) requireActivity();
			ActionBar actionBar = activity.getSupportActionBar();
			if (actionBar != null) {
				actionBar.setSubtitle(getResources().getString(R.string.account));
			}
		}
        catch (NullPointerException npe) {
            Timber.e("Could not set subtitle");
        }
    }


    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onClick(View view) {
    	final int viewId = view.getId();
    	if (viewId == R.id.done_button) {
			createAccount();
		} else if (viewId == R.id.cancel_button) {
			mEtUsername.setText("");
			mEtPassword.setText("");
			mEtConfirm.setText("");
		} else if (viewId == R.id.exit_button) {
			FragmentActivity activity = requireActivity();
			activity.getSupportFragmentManager().popBackStack();
		} else {
			Timber.e("Invalid button click");
		}
    }

    private void createAccount() {
        FragmentActivity activity = requireActivity();
        final String username = mEtUsername.getText().toString();
        final String password = mEtPassword.getText().toString();
        final String confirm = mEtConfirm.getText().toString();
		if (password.equals(confirm) && !TextUtils.isEmpty(username) && !TextUtils.isEmpty(password)) {

			try {
				MessageDigest digest = MessageDigest.getInstance("SHA-256");
				byte[] sha256HashBytes = digest.digest(password.getBytes(StandardCharsets.UTF_8));
				String sha256HashStr = StringUtils.bytesToHex(sha256HashBytes);

				// New way: create new UserAccount, then add it to ViewModel
				UserAccount userAccount = new UserAccount(username, sha256HashStr);
				mUserAccountViewModel.insert(userAccount);
				Toast.makeText(activity.getApplicationContext(), "New UserAccount added", Toast.LENGTH_SHORT).show();

			} catch (NoSuchAlgorithmException e) {
				Toast.makeText(activity, "Error: No SHA-256 algorithm found", Toast.LENGTH_SHORT).show();
				e.printStackTrace();
			}
		} else if ((username.equals("")) || (password.equals("")) || (confirm.equals(""))) {
			Toast.makeText(activity.getApplicationContext(), "Missing entry", Toast.LENGTH_SHORT).show();
		} else {
			Timber.e("An unknown account creation error occurred.");
			FragmentManager manager = getParentFragmentManager();
			AccountErrorDialogFragment fragment = new AccountErrorDialogFragment();
			fragment.show(manager, "account_error");
		}
	}
	private void setOnFocusChangeListener(EditText editText, String name){
		editText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
			public void onFocusChange(View v, boolean hasFocus) {
				if (hasFocus) {
					editText.setHint(name);
				} else {
					editText.setHint("");
				}
			}
		});
	}

	//Get the currency code:

		public String GPSCurrency() {
			mContext = getActivity();
			//Create location manager object


			locationManager = (LocationManager) mContext.getSystemService(Context.LOCATION_SERVICE);

			location = new Location(LocationManager.GPS_PROVIDER);
			location.setLatitude(0.0);
			location.setLongitude(0.0);
			location = getLocation(location, 0);
			if(location == null){
				//Check if they listened, and changed got their location
				location = getLocation(location, 1);
			}
			String symbol = "$";
			double lat = 0.0;
			double lng = 0.0;
			//If location is null, just use default $. Otherwise, find the currency using location
			if(location != null) {
				lat = location.getLatitude();
				lng = location.getLongitude();
				try {
					symbol = getCurrency(lat, lng);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			return symbol;
		}

		public Location getLocation(Location loc, int flag) {
			if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
				if(flag==0) {
					//If we can't get the location, ask for it
					new AlertDialog.Builder(mContext).setMessage("Please enable GPS")
							.setPositiveButton("Open your location settings", new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface paramDialogInterface, int paramInt) {
									mContext.startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
								}
							})
							.setNegativeButton("Cancel", null).show();
				}
				return null;
			}else{
				locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 10, locationListenerGPS);
				loc = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
			}
			return loc;
		}

		//https://stackoverflow.com/questions/42218419/how-do-i-implement-the-locationlistener
		LocationListener locationListenerGPS = new LocationListener() {
			@Override
			public void onLocationChanged(android.location.Location location) {
				double latitude = location.getLatitude();
				double longitude = location.getLongitude();
				String msg = "New Latitude: " + latitude + "New Longitude: " + longitude;
				Toast.makeText(mContext, msg, Toast.LENGTH_LONG).show();
			}

			@Override
			public void onStatusChanged(String provider, int status, Bundle extras) {

			}

			@Override
			public void onProviderEnabled(String provider) {

			}

			@Override
			public void onProviderDisabled(String provider) {

			}
		};



		//Used to get the currency symbol from the latitude and longitude
		public String getCurrency(double lat, double lng) throws IOException {
			String countryName = getCountry(lat, lng);
			String countryCode = getCountryCode(countryName);
			//Create locale, get the currency, and get the symbol for that currency
			Locale locale = new Locale("", countryCode);
			Currency currency = Currency.getInstance(locale);
			String symbol = currency.getSymbol();
			return symbol;
		}


		//Used to get the country from latitude and longitude
		//https://stackoverflow.com/questions/9409195/how-to-get-complete-address-from-latitude-and-longitude
		public String getCountry(double lat, double lng) throws IOException {
			Geocoder geocoder;
			List<Address> addresses;
			geocoder = new Geocoder(getActivity(), Locale.getDefault());
			addresses = geocoder.getFromLocation(lat, lng,1);
			return addresses.get(0).getCountryName();
		}


		//Used to get the country code from latitude and longitude
		//https://stackoverflow.com/questions/28503225/get-country-code-from-country-name-in-android
		public String getCountryCode(String countryName) {

			// Get all country codes in a string array.
			String[] isoCountryCodes = Locale.getISOCountries();
			Map<String, String> countryMap = new HashMap<>();
			Locale locale;
			String name;

			// Iterate through all country codes:
			for (String code : isoCountryCodes) {
				// Create a locale using each country code
				locale = new Locale("", code);
				// Get country name for each code.
				name = locale.getDisplayCountry();
				// Map all country names and codes in key - value pairs.
				countryMap.put(name, code);
			}

			// Return the country code for the given country name using the map.
			// Here you will need some validation or better yet
			// a list of countries to give to user to choose from.
			return countryMap.get(countryName); // "NL" for Netherlands.
		}
	}
