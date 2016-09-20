package com.test.okhttppost;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


public class MainActivity extends Activity {

	@BindView(R.id.editName) EditText editName;
	@BindView(R.id.editId) EditText editId;
	public static final int PICK_IMAGE_CODE = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
		ButterKnife.bind(this);
    }

	@OnClick(R.id.btnSubmit)
	public void submit(View view) {
		Intent intent = new Intent();
		intent.setType("image/*");
		intent.setAction(Intent.ACTION_GET_CONTENT);
		startActivityForResult(Intent.createChooser(intent, "Select Image"), PICK_IMAGE_CODE);
	}


	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == PICK_IMAGE_CODE && resultCode == Activity.RESULT_OK) {

			android.net.Uri selectedImage = data.getData();
			String[] filePathColumn = {MediaStore.Images.Media.DATA};
			android.database.Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);

			if (cursor == null)
				return;

			cursor.moveToFirst();

			int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
			String filePath = cursor.getString(columnIndex);
			cursor.close();

			File file = new File(filePath);
			MediaType MEDIA_TYPE = MediaType.parse("image/*");

			OkHttpClient client = new OkHttpClient();
			Request request = new Request.Builder()
					.url("http://ruslancode.net23.net/retrofit/post.php"
//							new HttpUrl.Builder()
//							.scheme("http") //http
//							.host("ruslancode.net23.net")
//							.addPathSegment("/retrofit/post.php")//adds "/pathSegment" at the end of hostname
//									//.addQueryParameter("param1", "value1") //add query parameters to the URL
//									//.addEncodedQueryParameter("encodedName", "encodedValue")//add encoded query parameters to the URL
//							.build()

//							HttpUrl.Builder urlBuilder
//							String url = urlBuilder.build().toString();
					)
					.post(new MultipartBody.Builder()
							.setType(MultipartBody.FORM)
							.addFormDataPart("name", editName.getText().toString())
							.addFormDataPart("id", editId.getText().toString())
//							.addFormDataPart("filename", file.getName()) //e.g. title.png
//							.addFormDataPart("file", "uploaded_image", RequestBody.create(MEDIA_TYPE, file))
							.addFormDataPart("uploaded_image", file.getName(), RequestBody.create(MEDIA_TYPE, file))
									//.addFormDataPart("token", token)
							.build())
					.build();

			client.newCall(request).enqueue(new Callback() {
				@Override
				public void onFailure(Call call, IOException e) {
					Toast.makeText(getBaseContext(), "Post Failure", Toast.LENGTH_SHORT).show();
					e.printStackTrace();
				}
				@Override
				public void onResponse(Call call, Response response) throws IOException {
					if (!response.isSuccessful()) {
						Toast.makeText(getBaseContext(), "Post Unsuccessful", Toast.LENGTH_SHORT).show();
						throw new IOException("Unexpected code " + response);
					} else {
						// do something wih the result
						Log.i("Response", response.body().string());
						//Toast.makeText(getBaseContext(), "Post Successful", Toast.LENGTH_SHORT).show();
					}
				}
			});
		}
	}
}