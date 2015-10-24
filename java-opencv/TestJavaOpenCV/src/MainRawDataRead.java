/**
 * Created by ar on 13.10.15.
 */

import by.grid.imlab.Descriptor;
import by.grid.imlab.Imshow;
import org.opencv.core.*;
import org.opencv.highgui.Highgui;

import java.nio.file.Paths;
import java.util.ArrayList;


//////////////////////////////////
public class MainRawDataRead {
    static {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
    }
    public static void main(String[] args) {

        System.out.println("Current directory: " + Paths.get(".").toAbsolutePath().normalize().toString());

        int numImages = 7;
        ArrayList<Descriptor> listDsc = new ArrayList<Descriptor>(numImages);
        for(int ii=0; ii<numImages; ii++) {
            String pathImg = "data/doge2_rot_scale_crop" + ii + ".png";
            Mat img = Highgui.imread(pathImg, Highgui.CV_LOAD_IMAGE_GRAYSCALE);
            Imshow viewImg = new Imshow("Img #" + ii);
            viewImg.showImage(img);
            Descriptor dsc = Descriptor.buildDsc(img, Descriptor.DEF_BIN_NUMBER, true);
            listDsc.add(dsc);
        }

        System.out.println("#Descriptors: " + listDsc.size());

        System.out.println("Compare #0 descriptor with other: ");
        Descriptor dsc0 = listDsc.get(0);
        for(int ii=1; ii<listDsc.size(); ii++) {
            float dst = dsc0.dstL1(listDsc.get(ii));
            String strOk = "Ok";
            if(dst>0.1) {
                strOk = "Bad";
            }
            System.out.println("Distance #0 and #" + ii +  " is : " + dst + ", \t is [" + strOk + "]");
        }

    }
}

