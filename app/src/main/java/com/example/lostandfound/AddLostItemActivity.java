package com.example.lostandfound;

import android.app.DatePickerDialog;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
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

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class AddLostItemActivity extends AppCompatActivity {

    ImageButton btnBack;
    ImageView ivLostImage;
    LinearLayout layoutPhotoPlaceholder;
    Button btnCamera, btnGallery, btnSubmitLost;
    EditText etLostUserName, etLostUserEmail, etLostDescription, etLostLocation, etLostDate, etLostPhone;

    private Calendar calendar;
    private Uri currentImageUri = null;

    private final ActivityResultLauncher<String> galleryLauncher =
            registerForActivityResult(new ActivityResultContracts.GetContent(), uri -> {
                if (uri != null) {
                    currentImageUri = copyUriToInternalStorage(uri, "lost_gallery_");
                    ivLostImage.setImageURI(currentImageUri);
                    layoutPhotoPlaceholder.setVisibility(View.GONE);
                }
            });

    private final ActivityResultLauncher<Void> cameraLauncher =
            registerForActivityResult(new ActivityResultContracts.TakePicturePreview(), bitmap -> {
                if (bitmap != null) {
                    currentImageUri = saveBitmapToInternalStorage(bitmap, "lost_cam_");
                    ivLostImage.setImageBitmap(bitmap);
                    layoutPhotoPlaceholder.setVisibility(View.GONE);
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_lost_item);

        btnBack = findViewById(R.id.btnBack);
        ivLostImage = findViewById(R.id.ivLostImage);
        layoutPhotoPlaceholder = findViewById(R.id.layoutPhotoPlaceholder);
        btnCamera = findViewById(R.id.btnCamera);
        btnGallery = findViewById(R.id.btnGallery);
        btnSubmitLost = findViewById(R.id.btnSubmitLost);

        etLostUserName = findViewById(R.id.etLostUserName);
        etLostUserEmail = findViewById(R.id.etLostUserEmail);
        etLostDescription = findViewById(R.id.etLostDescription);
        etLostLocation = findViewById(R.id.etLostLocation);
        etLostDate = findViewById(R.id.etLostDate);
        etLostPhone = findViewById(R.id.etLostPhone);

        String loggedEmail = SessionManager.getUserEmail(this);
        if (!loggedEmail.isEmpty()) {
            etLostUserEmail.setText(loggedEmail);
        }
        String loggedName = SessionManager.getUserName(this);
        if (!loggedName.isEmpty()) {
            etLostUserName.setText(loggedName);
        }

        btnBack.setOnClickListener(v -> finish());

        btnCamera.setOnClickListener(v -> cameraLauncher.launch(null));
        btnGallery.setOnClickListener(v -> galleryLauncher.launch("image/*"));

        calendar = Calendar.getInstance();
        updateDateInView();

        etLostDate.setOnClickListener(v -> {
            DatePickerDialog datePickerDialog = new DatePickerDialog(
                    AddLostItemActivity.this,
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

        btnSubmitLost.setOnClickListener(v -> {
            String description = etLostDescription.getText().toString().trim();
            String location = etLostLocation.getText().toString().trim();
            String date = etLostDate.getText().toString().trim();
            String phone = etLostPhone.getText().toString().trim();

            if (description.isEmpty()) {
                etLostDescription.setError("Please enter description");
                etLostDescription.requestFocus();
                return;
            }

            if (location.isEmpty()) {
                etLostLocation.setError("Please enter location");
                etLostLocation.requestFocus();
                return;
            }

            if (date.isEmpty()) {
                etLostDate.setError("Please select date");
                return;
            }

            if (phone.isEmpty()) {
                etLostPhone.setError("Please enter your phone number");
                etLostPhone.requestFocus();
                return;
            }

            String userName = etLostUserName.getText().toString().trim();
            if (userName.isEmpty()) {
                userName = "Student";
            }

            String userEmail = etLostUserEmail.getText().toString().trim().toLowerCase(Locale.ROOT);
            if (userEmail.isEmpty()) {
                userEmail = SessionManager.getUserEmail(this);
            }

            String campusCode = CampusManager.getJoinedCampusCode(this);
            String imageUriStr = currentImageUri != null ? currentImageUri.toString() : null;
            DatabaseHelper.getInstance(this).insertItem(
                    campusCode, userName, userEmail, "lost",
                    imageUriStr, description, location, date, phone
            );

            Toast.makeText(this, "Lost item posted successfully!", Toast.LENGTH_SHORT).show();
            finish();
        });
    }

    private void updateDateInView() {
        String format = "dd MMM yyyy";
        SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.getDefault());
        etLostDate.setText(sdf.format(calendar.getTime()));
    }

    private Uri copyUriToInternalStorage(Uri uri, String prefix) {
        try {
            File file = new File(getFilesDir(), prefix + System.currentTimeMillis() + ".jpg");
            try (InputStream in = getContentResolver().openInputStream(uri);
                 OutputStream out = new FileOutputStream(file)) {
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
            return null;
        }
    }

    private Uri saveBitmapToInternalStorage(Bitmap bitmap, String prefix) {
        try {
            File file = new File(getFilesDir(), prefix + System.currentTimeMillis() + ".jpg");
            try (OutputStream out = new FileOutputStream(file)) {
                bitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);
                out.flush();
            }
            return Uri.fromFile(file);
        } catch (Exception e) {
            return null;
        }
    }
}
