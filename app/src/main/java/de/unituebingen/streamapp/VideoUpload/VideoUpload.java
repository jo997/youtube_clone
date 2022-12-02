package de.unituebingen.streamapp.VideoUpload;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.provider.OpenableColumns;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import de.unituebingen.streamapp.R;
import de.unituebingen.streamapp.UserData;
import de.unituebingen.streamapp.tools.Video;

public class VideoUpload extends Fragment {
    private View view;

    //Layout Elements
    private EditText videoTitle;
    private EditText videoDescription;

    private TextView filenameView;      // Display selected files filename
    private Button btnSelectVideo;      // Button to open File selection
    private Button btnUpload;           // Upload Button

    private VideoUploadViewModel mViewModel;

    // UserData
    private UserData ud;

    // File URI
    private Uri uri;


    final int ACTIVITY_CHOOSE_FILE = 1;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        view = inflater.inflate(R.layout.video_upload, container, false);

        // Layout Elements
        videoTitle = (EditText) view.findViewById(R.id.uv_et_title);
        videoDescription = (EditText) view.findViewById(R.id.vu_et_description);

        filenameView = (TextView) view.findViewById(R.id.vu_tv_filename);
        btnSelectVideo = (Button) view.findViewById(R.id.vu_btn_fileselect);

        btnUpload = (Button) view.findViewById(R.id.vu_btn_upload);


        return view;
    }

    @Override
    public void onActivityCreated(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mViewModel = new ViewModelProvider(this).get(VideoUploadViewModel.class);

        // Get Context
        Fragment context = this;

        // User Data
        ud = new UserData(this.getActivity());

        Observer<Video> uploadObserver = new Observer<Video>() {
            @Override
            public void onChanged(Video video) {
                if (video == null) {
                    Toast.makeText(context.getActivity().getApplicationContext(),
                            "Video Upload failed!",
                            Toast.LENGTH_SHORT).show();
                } else {
                    String videoTitle = video.getTitle();
                    String videoUploader = video.getUploaderName();

                    Toast.makeText(context.getActivity().getApplicationContext(),
                            "Upload of " + videoTitle + "successfull",
                            Toast.LENGTH_SHORT).show();

                    // TODO: switch to MyVideoView
                }
            }
        };

        // Button to select Video
        btnSelectVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent chooseFile;
                Intent intent;
                chooseFile = new Intent(Intent.ACTION_GET_CONTENT);
                chooseFile.addCategory(Intent.CATEGORY_OPENABLE);
                chooseFile.setType(getString(R.string.vu_MIME));
                intent = Intent.createChooser(chooseFile, getString(R.string.vu_chooseVideo));
                startActivityForResult(intent, ACTIVITY_CHOOSE_FILE);
            }
        });

        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ud.getTypeId() >= UserData.USER_PRIVILEGE) {
                    String title = videoTitle.getText().toString();
                    String desc = videoDescription.getText().toString();
                    File fileToBeUploaded = null;
                    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
                        fileToBeUploaded = getVideoFromContentUri(uri, context.getActivity());
                    } else {
                        fileToBeUploaded = getVideoFromContentUriQ(uri, context.getActivity());
                    }
                    Log.e("VideoUpload: File", fileToBeUploaded.length() + "");
                    mViewModel.uploadVideo(
                            ud.getAuthentication(),
                            title, desc, fileToBeUploaded)
                            .observe(context.getActivity(), uploadObserver);
                } else {
                    Toast.makeText(context.getActivity(),
                            "You are not allowed to upload a video!",
                            Toast.LENGTH_LONG).show();
                }
            }
        });

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == ACTIVITY_CHOOSE_FILE) {
            if (resultCode == Activity.RESULT_OK) {
                uri = data.getData();
                String filePath = uri.getPath();

                filenameView.setText(getFileName(uri, getContext()));
            }
        }
    }

    /* FILE capabilities */

    /**
     * Get the filename of an URI
     *
     * @param uri     Path object
     * @param context Application context
     * @return Human readable file name
     */
    private String getFileName(Uri uri, Context context) {
        String result = null;
        Cursor cursor = context.getContentResolver().query(uri, null, null, null, null);
        try {
            if (cursor != null && cursor.moveToFirst()) {
                int columnIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
                result = cursor.getString(columnIndex);
            }
        } finally {
            cursor.close();
        }
        if (result == null) {
            String[] paths = uri.getPath().split("/");
            result = paths[paths.length - 1];
        }
        return result;
    }

    /**
     * Copies InputStream to OutputStream
     *
     * @param from Incoming InputStream
     * @param to   Outcoming OutputStream
     * @throws IOException
     */
    private void copyStream(InputStream from, OutputStream to) throws IOException {
        byte[] buffer = new byte[1024];
        int length;
        try {
            while ((length = from.read(buffer)) > 0) {
                to.write(buffer, 0, length);
            }
            to.flush();
        } finally {
            from.close();
            to.close();
        }
    }

    /**
     * Get the Video from the Content Uri
     *
     * @param uri     video Uri
     * @param context Application context
     * @return Videofile or null
     */
    private File getVideoFromContentUri(Uri uri, Context context) {
        try {
            ParcelFileDescriptor pfd = context.getContentResolver()
                    .openFileDescriptor(uri, "r", null);

            File file = new File(context.getCacheDir(), getFileName(uri, context));

            FileInputStream input = new FileInputStream(pfd.getFileDescriptor());
            FileOutputStream output = new FileOutputStream(file);
            copyStream(input, output);
            return file;
        } catch (Exception e) {
            Log.e("VideoUpload: FileStream", e.getMessage());
            return null;
        }
    }

    /**
     * Get the Video from the Content Uri
     *
     * @param uri     video uri
     * @param context Application context
     * @return videofile or null
     */
    private File getVideoFromContentUriQ(Uri uri, Context context) {
        try {
            File file = new File(context.getCacheDir(), getFileName(uri, context));

            FileInputStream input = (FileInputStream) context.getContentResolver()
                    //.openInputStream(Uri.parse(uri.toString()));
                    .openInputStream(Uri.parse(uri.toString()));
            FileOutputStream output = new FileOutputStream(file);
            copyStream(input, output);
            return file;
        } catch (Exception e) {
            Log.e("VideoUpload: FileStream (Q)", e.getMessage());
            return null;
        }
    }


}