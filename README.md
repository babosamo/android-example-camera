# android-example-camera


https://developer.android.com/training/camera/photobasics

#### 필수 권한
```
카메라 사용 권한 
<uses-feature android:name="android.hardware.camera" android:required="true" />
```


#### thumbnail 이미지를 가져오는 방법
동작 순서
1. 카메라 앱 호출
  - Action : MediaStore.ACTION_IMAGE_CAPTURE
  
  ```
  val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
  startActivityForResult(takePictureIntent, requestCode)
  ```
2. 카메라 앱에서 사진 촬영
3. 사진 촬영후 호출한 activity 로 데이타 반환
4. onActivityReesult 에서 전달 받은 데이타를 사용


##### full size 이미지를 가져오는 방법

동작 순서
1. 사진 촬영후 저장될 full size 파일을 생성
2. FileProvider 를 통해서 해당 파일의 경로를 uri 형태로 생성
  ```
  val photoURI = FileProvider.getUriForFile(this, BuildConfig.APPLICATION_ID + ".fileprovider", photoFile)
  ```
3. 카메라 앱 호출 시 파일의 경로를 함께 전달 
  - Action : MediaStore.ACTION_IMAGE_CAPTURE
  
  ```
  val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
  val photoURI = FileProvider.getUriForFile(this, BuildConfig.APPLICATION_ID + ".fileprovider", photoFile)
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
  startActivityForResult(takePictureIntent, requestCode)
  ```
4. 사진 촬영후 호출한 activity 로 onActivityReesult 호출됨
5. 카메라에 전달했던 uri 값에 full size 포토가 있음을 확인

#### full size 이미지 획득을 위한 준비
- 파일 저장을 위한 권한 획득
```
<uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />
```

- AndroidManifest.xml 에 fileprovider 등

```
<provider
  android:name="android.support.v4.content.FileProvider"
  android:authorities="com.babosamo.cameraexample.fileprovider"
  android:exported="false"
  android:grantUriPermissions="true">
  <meta-data
      android:name="android.support.FILE_PROVIDER_PATHS"
      android:resource="@xml/filepaths"></meta-data>
</provider>
```
