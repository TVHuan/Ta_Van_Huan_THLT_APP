package com.example.bai2thapp;

import android.Manifest;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private ImageSwitcher imageSwitcher;
    private GridView gridView;
    private TextView tvImageName;
    private Button btnSlideshow;
    private FloatingActionButton fabFavorite;

    private PhotoAdapter photoAdapter;
    private final List<Photo> photoList = new ArrayList<>();
    private int currentImageIndex = 0;

    private final Handler slideshowHandler = new Handler(Looper.getMainLooper());
    private boolean isSlideshowRunning = false;
    private static final int SLIDESHOW_DELAY = 3000;

    private static final String PREFS_NAME = "PhotoGalleryPrefs";
    private static final String KEY_LAST_IMAGE = "last_image_index";
    private static final String KEY_FAVORITE_PREFIX = "favorite_";

    private ActivityResultLauncher<String> requestPermissionLauncher;
    private ActivityResultLauncher<String> pickImageLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initializeViews();
        setupLaunchers();
        loadInitialPhotos();
        loadFavorites();

        setupImageSwitcher();
        setupGridView();

        loadLastViewedImage();
        setupClickListeners();
    }

    private void initializeViews() {
        imageSwitcher = findViewById(R.id.image_switcher);
        gridView = findViewById(R.id.grid_view_gallery);
        tvImageName = findViewById(R.id.tv_image_name);
        btnSlideshow = findViewById(R.id.btn_slideshow);
        fabFavorite = findViewById(R.id.fab_favorite);
    }

    private void setupLaunchers() {
        requestPermissionLauncher = registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
            if (isGranted) {
                pickImageLauncher.launch("image/*");
            } else {
                Toast.makeText(this, "Bạn cần cấp quyền để thêm ảnh.", Toast.LENGTH_SHORT).show();
            }
        });

        pickImageLauncher = registerForActivityResult(new ActivityResultContracts.GetContent(),
                uri -> {
                    if (uri != null) {
                        String name = "Ảnh từ bộ nhớ " + (photoList.size() + 1);
                        photoList.add(new Photo(uri, name));
                        photoAdapter.notifyDataSetChanged();
                        Toast.makeText(this, "Đã thêm ảnh mới!", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void loadInitialPhotos() {
        photoList.add(new Photo(R.drawable.img_1, "PNTT"));
        photoList.add(new Photo(R.drawable.img_2, "PNTT"));
        photoList.add(new Photo(R.drawable.img_3, "Thành phố"));
        photoList.add(new Photo(R.drawable.img_4, "Bình minh"));
        photoList.add(new Photo(R.drawable.img_5, "Bầu trời"));
        photoList.add(new Photo(R.drawable.img_6, "Bãi biển"));
    }

    private void setupImageSwitcher() {
        imageSwitcher.setFactory(() -> {
            ImageView imageView = new ImageView(getApplicationContext());
            imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
            return imageView;
        });
        Animation in = AnimationUtils.loadAnimation(this, android.R.anim.fade_in);
        in.setDuration(500);
        Animation out = AnimationUtils.loadAnimation(this, android.R.anim.fade_out);
        out.setDuration(500);
        imageSwitcher.setInAnimation(in);
        imageSwitcher.setOutAnimation(out);
    }

    private void setupGridView() {
        photoAdapter = new PhotoAdapter(this, photoList);
        gridView.setAdapter(photoAdapter);
    }

    private void setupClickListeners() {
        gridView.setOnItemClickListener((parent, view, position, id) -> {
            displayImage(position);
            if (isSlideshowRunning) stopSlideshow();
        });

        btnSlideshow.setOnClickListener(v -> {
            if (isSlideshowRunning) stopSlideshow();
            else startSlideshow();
        });

        findViewById(R.id.btn_add_image).setOnClickListener(v -> addImageFromStorage());

        fabFavorite.setOnClickListener(v -> toggleFavorite());
    }

    private void displayImage(int position) {
        if (position < 0 || position >= photoList.size()) return;

        currentImageIndex = position;
        Photo photo = photoList.get(position);

        if (photo.isFromUri()) {
            imageSwitcher.setImageURI(photo.getUri());
        } else {
            imageSwitcher.setImageResource(photo.getResourceId());
        }

        String displayName = photo.getName() + " (" + (position + 1) + "/" + photoList.size() + ")";
        tvImageName.setText(displayName);
        updateFavoriteButton();
        saveLastViewedImage(position);
    }

    private void toggleFavorite() {
        if (photoList.isEmpty()) return;
        Photo currentPhoto = photoList.get(currentImageIndex);
        currentPhoto.setFavorite(!currentPhoto.isFavorite());
        updateFavoriteButton();
        photoAdapter.notifyDataSetChanged(); // Cập nhật icon trên GridView
        saveFavoriteState(currentImageIndex, currentPhoto.isFavorite());
    }

    private void updateFavoriteButton() {
        if (photoList.isEmpty()) return;
        Photo currentPhoto = photoList.get(currentImageIndex);
        if (currentPhoto.isFavorite()) {
            fabFavorite.setImageResource(R.drawable.ic_favorite_filled);
        } else {
            fabFavorite.setImageResource(R.drawable.ic_favorite_border);
        }
    }


    private final Runnable slideshowRunnable = new Runnable() {
        @Override
        public void run() {
            if (photoList.size() > 0) {
                int nextIndex = (currentImageIndex + 1) % photoList.size();
                displayImage(nextIndex);
                slideshowHandler.postDelayed(this, SLIDESHOW_DELAY);
            } else {
                stopSlideshow();
            }
        }
    };

    private void startSlideshow() {
        if (!photoList.isEmpty() && !isSlideshowRunning) {
            isSlideshowRunning = true;
            btnSlideshow.setText("Dừng");
            slideshowHandler.postDelayed(slideshowRunnable, SLIDESHOW_DELAY);
        }
    }

    private void stopSlideshow() {
        if (isSlideshowRunning) {
            isSlideshowRunning = false;
            btnSlideshow.setText("Slideshow");
            slideshowHandler.removeCallbacks(slideshowRunnable);
        }
    }

    private void addImageFromStorage() {
        String permission;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            permission = Manifest.permission.READ_MEDIA_IMAGES;
        } else {
            permission = Manifest.permission.READ_EXTERNAL_STORAGE;
        }

        if (ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED) {
            pickImageLauncher.launch("image/*");
        } else {
            requestPermissionLauncher.launch(permission);
        }
    }

    private void saveLastViewedImage(int index) {
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        prefs.edit().putInt(KEY_LAST_IMAGE, index).apply();
    }

    private void loadLastViewedImage() {
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        int lastIndex = prefs.getInt(KEY_LAST_IMAGE, 0);
        displayImage(lastIndex);
    }

    private void saveFavoriteState(int index, boolean isFavorite) {
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        prefs.edit().putBoolean(KEY_FAVORITE_PREFIX + index, isFavorite).apply();
    }

    private void loadFavorites() {
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        Map<String, ?> allEntries = prefs.getAll();
        for (Map.Entry<String, ?> entry : allEntries.entrySet()) {
            if (entry.getKey().startsWith(KEY_FAVORITE_PREFIX)) {
                try {
                    int index = Integer.parseInt(entry.getKey().substring(KEY_FAVORITE_PREFIX.length()));
                    if (index >= 0 && index < photoList.size()) {
                        photoList.get(index).setFavorite((Boolean) entry.getValue());
                    }
                } catch (NumberFormatException e) {
                }
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        stopSlideshow();
    }
}
