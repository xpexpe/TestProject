package org.techtown.testproject;

import static java.lang.Math.atan2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.PermissionChecker;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.pose.Pose;
import com.google.mlkit.vision.pose.PoseDetection;
import com.google.mlkit.vision.pose.PoseDetector;
import com.google.mlkit.vision.pose.PoseLandmark;
import com.google.mlkit.vision.pose.accurate.AccuratePoseDetectorOptions;
import com.google.mlkit.vision.pose.defaults.PoseDetectorOptions;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    static final int REQUEST_CODE = 1;
    ImageView imageView;
    Uri uri;
    Bitmap bitmap;
    Button btn_get_image, btn_detection_image;
    InputImage image;
    TextView degree_info;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


//        포즈 감지기는 먼저 이미지에서 가장 중요한 사람을 감지한 다음 자세 감지를 실행합니다.
//        후속 프레임에서는 사람이 흐릿해지거나 더 이상 확실하게 감지되지 않는 한 사람 감지 단계가 실행되지 않습니다.
//                포즈 감지기는 가장 중요한 사람을 추적하고 각 추론에서 자세를 반환합니다.
//                이렇게 하면 지연 시간이 줄어들고 감지 기능이 원활해집니다.
//                동영상 스트림에서 포즈를 감지하려면 이 모드를 사용하세요.
        // GOOGLE API(자세 분류 옵션) 발췌

        PoseDetectorOptions options =
                new PoseDetectorOptions.Builder()
                        .setDetectorMode(PoseDetectorOptions.STREAM_MODE)
                        .build();

        PoseDetector poseDetector = PoseDetection.getClient(options);

        imageView = findViewById(R.id.imageView);
        degree_info = findViewById(R.id.degreeView);


        // GET_IMAGE 버튼 클릭 리스너 등록
        btn_get_image = findViewById(R.id.btn_get_image);
        btn_get_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 인텐트를 통하여 일단 이미지를 갤러리에서 가져옴
                // 프로젝트 할 때 카메라에서 가져오고 싶다면 이부분 변경하면 됨 ~!
                Intent intent = new Intent(Intent.ACTION_PICK);
                //intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent, REQUEST_CODE);
            }
        });

        // POSE DETECTION 버튼 - 불러온 이미지 인식하여 각각의 관절의 좌표 가져옴
        // 가져온 좌표들은 아래 OnSuccess 함수에서 응용하면 됨
        btn_detection_image = findViewById(R.id.btn_detection_image);
        btn_detection_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Task<Pose> result =
                        poseDetector.process(image)
                                .addOnSuccessListener(
                                        new OnSuccessListener<Pose>() {
                                            @Override
                                            public void onSuccess(Pose pose) {
                                                // Task completed successfully
                                                // 성공적으로 가져왔다면 onSuccess에서 요소들을 사용할 수 있음
                                                List<PoseLandmark> allPoseLandmarks = pose.getAllPoseLandmarks();

                                                // 일단 가져옴 사람 없으면 각각 요소들 null값을 가지게 됨
                                                // 각각 요소들은 다음과 같이 사용할 수 있음
                                                // 모두 불러오는 것보단 필요한 요소들만 뽑아서 pose.getPoseLandmark(PoseLandmark.~~) 이렇게 응용하면 될거같음
                                                // 일단 나열해봤다!
                                                PoseLandmark leftShoulder = pose.getPoseLandmark(PoseLandmark.LEFT_SHOULDER);
                                                PoseLandmark rightShoulder = pose.getPoseLandmark(PoseLandmark.RIGHT_SHOULDER);
                                                PoseLandmark leftElbow = pose.getPoseLandmark(PoseLandmark.LEFT_ELBOW);
                                                PoseLandmark rightElbow = pose.getPoseLandmark(PoseLandmark.RIGHT_ELBOW);
                                                PoseLandmark leftWrist = pose.getPoseLandmark(PoseLandmark.LEFT_WRIST);
                                                PoseLandmark rightWrist = pose.getPoseLandmark(PoseLandmark.RIGHT_WRIST);
                                                PoseLandmark leftHip = pose.getPoseLandmark(PoseLandmark.LEFT_HIP);
                                                PoseLandmark rightHip = pose.getPoseLandmark(PoseLandmark.RIGHT_HIP);
                                                PoseLandmark leftKnee = pose.getPoseLandmark(PoseLandmark.LEFT_KNEE);
                                                PoseLandmark rightKnee = pose.getPoseLandmark(PoseLandmark.RIGHT_KNEE);
                                                PoseLandmark leftAnkle = pose.getPoseLandmark(PoseLandmark.LEFT_ANKLE);
                                                PoseLandmark rightAnkle = pose.getPoseLandmark(PoseLandmark.RIGHT_ANKLE);
                                                PoseLandmark leftPinky = pose.getPoseLandmark(PoseLandmark.LEFT_PINKY);
                                                PoseLandmark rightPinky = pose.getPoseLandmark(PoseLandmark.RIGHT_PINKY);
                                                PoseLandmark leftIndex = pose.getPoseLandmark(PoseLandmark.LEFT_INDEX);
                                                PoseLandmark rightIndex = pose.getPoseLandmark(PoseLandmark.RIGHT_INDEX);
                                                PoseLandmark leftThumb = pose.getPoseLandmark(PoseLandmark.LEFT_THUMB);
                                                PoseLandmark rightThumb = pose.getPoseLandmark(PoseLandmark.RIGHT_THUMB);
                                                PoseLandmark leftHeel = pose.getPoseLandmark(PoseLandmark.LEFT_HEEL);
                                                PoseLandmark rightHeel = pose.getPoseLandmark(PoseLandmark.RIGHT_HEEL);
                                                PoseLandmark leftFootIndex = pose.getPoseLandmark(PoseLandmark.LEFT_FOOT_INDEX);
                                                PoseLandmark rightFootIndex = pose.getPoseLandmark(PoseLandmark.RIGHT_FOOT_INDEX);
                                                PoseLandmark nose = pose.getPoseLandmark(PoseLandmark.NOSE);
                                                PoseLandmark leftEyeInner = pose.getPoseLandmark(PoseLandmark.LEFT_EYE_INNER);
                                                PoseLandmark leftEye = pose.getPoseLandmark(PoseLandmark.LEFT_EYE);
                                                PoseLandmark leftEyeOuter = pose.getPoseLandmark(PoseLandmark.LEFT_EYE_OUTER);
                                                PoseLandmark rightEyeInner = pose.getPoseLandmark(PoseLandmark.RIGHT_EYE_INNER);
                                                PoseLandmark rightEye = pose.getPoseLandmark(PoseLandmark.RIGHT_EYE);
                                                PoseLandmark rightEyeOuter = pose.getPoseLandmark(PoseLandmark.RIGHT_EYE_OUTER);
                                                PoseLandmark leftEar = pose.getPoseLandmark(PoseLandmark.LEFT_EAR);
                                                PoseLandmark rightEar = pose.getPoseLandmark(PoseLandmark.RIGHT_EAR);
                                                PoseLandmark leftMouth = pose.getPoseLandmark(PoseLandmark.LEFT_MOUTH);
                                                PoseLandmark rightMouth = pose.getPoseLandmark(PoseLandmark.RIGHT_MOUTH);

                                                double rightHipAngle = getAngle(rightShoulder, rightHip, rightKnee);
                                                degree_info.setText(rightHipAngle + "도");
                                                Log.e("[TEST]", rightHipAngle + " 각도");
                                                // 각도 출력은 텍스트를 통하여 출력
                                                // 일단 getAngle함수를 통하여 세 요소가 이루는 각도 계산한 후 degree_info TextView를 통하여 출력함
                                                // 로그 또한 출력
                                            }
                                        })
                                .addOnFailureListener(
                                        new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                // Task failed with an exception
                                                // ...
                                                // 실패시 할 내용들 적으면 됨
                                            }
                                        });
            }
        });
    }

    // getAngle 메서드 -> 세 포인트 잡고 세 포인트가 이루는 각도 계산
    // 일단 레그레이즈 ? 의 경우 제일 중요한 요소는 허리 - 다리 사이 각도라고 생각해서 위 예제에는 어깨 - 엉덩이 - 무릎 각도를 계산해서 출력해보았음

    static double getAngle(PoseLandmark firstPoint, PoseLandmark midPoint, PoseLandmark lastPoint) {
        double result =
                Math.toDegrees(
                        atan2(lastPoint.getPosition().y - midPoint.getPosition().y,
                                lastPoint.getPosition().x - midPoint.getPosition().x)
                                - atan2(firstPoint.getPosition().y - midPoint.getPosition().y,
                                firstPoint.getPosition().x - midPoint.getPosition().x));
        result = Math.abs(result); // Angle should never be negative
        if (result > 180) {
            result = (360.0 - result); // Always get the acute representation of the angle
        }
        return result;
        // 값은 항상 180도를 넘을 수 없음 (0-180)
    }

    // 갤러리에서 사진뽑아내는 함수
    @Override
    protected  void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode,resultCode,data);
        if (requestCode == REQUEST_CODE) {
            // 갤러리에서 선택한 사진에 대한 uri를 가져온다.
            uri = data.getData();

            setImage(uri);
        }
    }

    // uri를 비트맵으로 변환시킨후 이미지뷰에 띄워주고 InputImage를 생성하는 메서드
    // 갤러리 -> uri -> 비트맵 -> ImageView
    private void setImage(Uri uri) {
        try{
            InputStream in = getContentResolver().openInputStream(uri);
            bitmap = BitmapFactory.decodeStream(in);
            imageView.setImageBitmap(bitmap);

            image = InputImage.fromBitmap(bitmap, 0);
            Log.e("setImage", "이미지 to 비트맵");
        } catch (FileNotFoundException e){
            e.printStackTrace();
        }
    }
}