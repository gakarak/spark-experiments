package by.grid.imlab;

import by.grid.imlab.Imshow;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.highgui.Highgui;
import org.opencv.imgproc.Imgproc;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Created by ar on 24.10.15.
 */
public class TestOpenCVImageDecode {
    static {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
    }
    public static void main(String[] args) {
        int idxImg = 0;
        String imgPath = "data/doge2_rot_scale_crop" + idxImg + ".png";
        Path pathImg = Paths.get(imgPath);
        try {
            byte[] data = Files.readAllBytes(pathImg);
            System.out.println(data.length);
            Mat buff = new Mat(1,data.length, CvType.CV_8SC1);
            buff.put(0,0,data);
            System.out.println(buff.size());
            Mat img = Highgui.imdecode(buff, Highgui.CV_LOAD_IMAGE_GRAYSCALE);
            Imshow.show(img);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
