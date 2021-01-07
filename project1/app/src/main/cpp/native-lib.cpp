#include <jni.h>
#include <opencv2/opencv.hpp>

using namespace cv;
using namespace std;

extern "C"
JNIEXPORT void JNICALL
Java_com_example_project1_Test_image_1processing(JNIEnv *env, jobject thiz,
                                                      jlong mat_addr_input, jlong mat_addr_input2,
                                                      jlong mat_addr_input3,
                                                      jlong mat_addr_input4,
                                                      jlong mat_addr_result, jlong mat_mask,
                                                      jlong mat_mask_inv, jlong mat_res,
                                                      jlong mat_res_2, jlong mat_roi, jlong mat_dst,
                                                      jlong mat_gray) {
    // TODO: implement imageprocessing()
    Mat &matInput = *(Mat *) mat_addr_input;    // 전신사진
    Mat &matResult = *(Mat *) mat_addr_result;
    Mat &matInput2 = *(Mat *) mat_addr_input2;  // 상의
    Mat &matInput3 = *(Mat *) mat_addr_input3;  // 하의
    Mat &matInput4 = *(Mat *) mat_addr_input4;  // 모자

    Mat &matMask = *(Mat *) mat_mask;
    Mat &matMaskInv = *(Mat *) mat_mask_inv;
    Mat &matRes = *(Mat *) mat_res;
    Mat &matRes2 = *(Mat *) mat_res_2;
    Mat &matRoi = *(Mat *) mat_roi;
    Mat &matDst = *(Mat *) mat_dst;

    Mat mask, mask_inv, res, res2, dst, gray, gray2, gray3;


    resize(matInput2, matInput2, Size(798, 798));   // upper_height, upper_width
    resize(matInput3, matInput3, Size(800,798)); // lower_height, upper_width
    resize(matInput4, matInput4, Size(266,266));    // 머리크기 = 너비 / 3

    cvtColor(matInput2, gray, COLOR_BGR2GRAY);  // 상의
    cvtColor(matInput3, gray2, COLOR_BGR2GRAY); // 하의
    cvtColor(matInput4, gray3, COLOR_BGR2GRAY); // 모자

    /* 상의 */
    threshold(gray, matMask, 250, 255, THRESH_BINARY_INV);
    bitwise_not(matMask, matMaskInv);

    // Roi
    matRoi = matInput(Rect(1120, 1545, 798, 798));

    copyTo(matRoi, matRes, matMaskInv);
    copyTo(matInput2, matRes2, matMask);

    cvtColor(matRes, matRes, COLOR_BGR2BGRA);
    cvtColor(matRes2, matRes2, COLOR_BGR2BGRA);
    add(matRes, matRes2, matDst);

    matDst.copyTo(matInput(Rect(1120, 1545, 798, 798)));


    /* 하의 */

    threshold(gray2, matMask, 250, 255, THRESH_BINARY_INV); // 하의
    bitwise_not(matMask, matMaskInv);

    matRoi = matInput(Rect(1120,2343,800,798));

    copyTo(matRoi, matRes,matMaskInv);
    copyTo(matInput3,matRes2, matMask);
    add(matRes,matRes2,matDst);

    matDst.copyTo(matInput(Rect(1120,2343,800,798)));
    // Roi
    matResult = matInput;


    /* 모자 */

    threshold(gray3, matMask, 250, 255, THRESH_BINARY_INV); // 모자
    bitwise_not(matMask, matMaskInv);

    matRoi = matInput(Rect(1316,1045,266,266));

    copyTo(matRoi, matRes,matMaskInv);
    copyTo(matInput4,matRes2, matMask);
    add(matRes,matRes2,matDst);

    matDst.copyTo(matInput(Rect(1316,1045,266,266)));
    // Roi
    matResult = matInput;

}
