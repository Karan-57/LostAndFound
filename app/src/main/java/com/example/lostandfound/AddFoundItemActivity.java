package com.example.lostandfound;

import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class AddFoundItemActivity extends AppCompatActivity {

    ImageButton btnBack;
    ImageView ivFoundImage;
    LinearLayout layoutPhotoPlaceholder;
    Button btnCamera, btnGallery, btnSubmitFound;
    EditText etFoundUserName, etFoundUserEmail, etFoundDescription, etFoundLocation, etFoundDate, etFoundPhone;

    private Calendar calendar;
    private Uri currentImageUri = null;

    private final ActivityResultLauncher<String> galleryLauncher =
            registerForActivityResult(new ActivityResultContracts.GetContent(), uri -> {
                if (uri != null) {
                    currentImageUri = copyUriToInternalStorage(uri, "found_gallery_");
                    ivFoundImage.setImageURI(currentImageUri);
                    layoutPhotoPlaceholder.setVisibility(View.GONE);
                }
            });

    private final ActivityResultLauncher<Void> cameraLauncher =
            registerForActivityResult(new ActivityResultContracts.TakePicturePreview(), bitmap -> {
                if (bitmap != null) {
                    currentImageUri = saveBitmapToInternalStorage(bitmap, "found_cam_");
                    ivFoundImage.setImageBitmap(bitmap);
                    layoutPhotoPlaceholder.setVisibility(View.GONE);
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_found_item);

        btnBack = findViewById(R.id.btnBack);
        ivFoundImage = findViewById(R.id.ivFoundImage);
        layoutPhotoPlaceholder = findViewById(R.id.layoutPhotoPlaceholder);
        btnCamera = findViewById(R.id.btnCamera);
        btnGallery = findViewById(R.id.btnGallery);
        btnSubmitFound = findViewById(R.id.btnSubmitFound);

        etFoundUserName = findViewById(R.id.etFoundUserName);
        etFoundUserEmail = findViewById(R.id.etFoundUserEmail);
        etFoundDescription = findViewById(R.id.etFoundDescription);
        etFoundLocation = findViewById(R.id.etFoundLocation);
        etFoundDate = findViewById(R.id.etFoundDate);
        etFoundPhone = findViewById(R.id.etFoundPhone);

        // Pre-fill email/name if logged in
        String loggedEmail = SessionManager.getUserEmail(this);
        if (!loggedEmail.isEmpty()) {
            etFoundUserEmail.setText(loggedEmail);
        }
        String loggedName = SessionManager.getUserName(this);
        if (!loggedName.isEmpty()) {
            etFoundUserName.setText(loggedName);
        }

        btnBack.setOnClickListener(v -> finish());

        btnCamera.setOnClickListener(v -> cameraLauncher.launch(null));
        btnGallery.setOnClickListener(v -> galleryLauncher.launch("image/*"));

        calendar = Calendar.getInstance();
        updateDateInView();

        etFoundDate.setOnClickListener(v -> {
            DatePickerDialog datePickerDialog = new DatePickerDialog(
                    AddFoundItemActivity.this,
                    (view, year, month, dayOfMonth) -> {
                        calendar.set(Calendar.YEAR, year);
                        calendar.set(Calendar.MONTH, month);
                        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                        updateDateInView();
                    },
                    calendar.get(Calendar.YEAR),
                    calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DAY_OF_MONTH)
            );
            datePickerDialog.show();
        });

        btnSubmitFound.setOnClickListener(v -> {
            String description = etFoundDescription.getText().toString().trim();
            String location = etFoundLocation.getText().toString().trim();
            String date = etFoundDate.getText().toString().trim();
            String phone = etFoundPhone.getText().toString().trim();

            if (description.isEmpty()) {
                etFoundDescription.setError("Please enter description");
                etFoundDescription.requestFocus();
                return;
            }

            if (location.isEmpty()) {
                etFoundLocation.setError("Please enter location");
                etFoundLocation.requestFocus();
                return;
            }

            if (date.isEmpty()) {
                etFoundDate.setError("Please select date");
                return;
            }

            if (phone.isEmpty()) {
                etFoundPhone.setError("Please enter your phone number");
                etFoundPhone.requestFocus();
                return;
            }

            String userName = etFoundUserName.getText().toString().trim();
            if (userName.isEmpty()) {
                userName = "Student";
            }

            String userEmail = etFoundUserEmail.getText().toString().trim().toLowerCase();
            if (userEmail.isEmpty()) {
                userEmail = SessionManager.getUserEmail(this);
            }

            String campusCode = CampusManager.getJoinedCampusCode(this);
            String imageUriStr = currentImageUri != null ? currentImageUri.toString() : null;
            DatabaseHelper.getInstance(this).insertItem(
                    campusCode, userName, userEmail, "found",
                    imageUriStr, description, location, date, phone
            );

            Toast.makeText(this, "Found item posted successfully!", Toast.LENGTH_SHORT).show();
            finish();
        });
    }

    private void updateDateInView() {
        String format = "dd MMM yyyy";
        SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.getDefault());
        etFoundDate.setText(sdf.format(calendar.getTime()));
    }

    /** Copies a selected image URI into private app internal storage so it is permanently accessible. */
    private Uri copyUriToInternalStorage(Uri uri, String prefix) {
        try {
            java.io.File file = new java.io.File(getFilesDir(), prefix + System.currentTimeMillis() + ".jpg");
            try (java.io.InputStream in = getContentResolver().openInputStream(uri);
                 java.io.OutputStream out = new java.io.FileOutputStream(file)) {
                if (in == null) return null;
                byte[] buffer = new byte[8192];
                int read;
                while ((read = in.read(buffer)) != -1) {
                    out.write(buffer, 0, read);
                }
                out.flush();
            }
            return Uri.fromFile(file);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /** Saves a camera bitmap to app internal storage and returns its Uri. */
    private Uri saveBitmapToInternalStorage(Bitmap bitmap, String prefix) {
        try {
            java.io.File file = new java.io.File(getFilesDir(), prefix + System.currentTimeMillis() + ".jpg");
            try (java.io.OutputStream out = new java.io.FileOutputStream(file)) {
                bitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);
                out.flush();
            }
            return Uri.fromFile(file);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
