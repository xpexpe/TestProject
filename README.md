# TestProject
Pose Detect -> getDegree : 포즈 인식해서 요소들의 각도 계산

# 이미지 올리는 법

1. `Android Studio` 켜기
2. 오른쪽 하단 Device File Explorer 클릭
3. `storage` 폴더로 들어가기
4. 숫자로 시작하는거 이상한거 있으니까 그거 클릭
5. `DCIM` 폴더에 드래그해서 이미지 넣기
6. 디바이스에서 Get Image 버튼 클릭
7. 왼쪽 위에 3줄 있는 아이콘 클릭
8. sdk~~gphone64_x86_64 이런거 클릭
9. DCIM 폴더 클릭
10. 이미지 가져오기!

# 각도 계산

이미지 가져오고 Detection Image 버튼 누르면 올려놓은 예제에서는 오른쪽 어깨 - 오른쪽 엉덩이 - 오른쪽 무릎 각도 계산되어 출력

```java
                                                double rightHipAngle = getAngle(rightShoulder, rightHip, rightKnee);
```
