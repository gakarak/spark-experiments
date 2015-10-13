package by.grid.imlab;

import org.opencv.core.Mat;
import org.opencv.core.MatOfFloat;
import org.opencv.core.MatOfInt;
import org.opencv.imgproc.Imgproc;

import java.util.Arrays;

/**
 * Created by ar on 13.10.15.
 */
public class Descriptor {
    public static int   DEF_BIN_NUMBER      = 16;
    public static float DEF_FLOAT_THRESHOLD = (Float.MIN_VALUE*1000);
    //
    public Descriptor() {
        dsc = null;
    }

    public Descriptor(Mat img) {
        if(img!=null) {
            build(img, Descriptor.DEF_BIN_NUMBER, true);
        } else {
            dsc = null;
        }
    }
    //
    public float dstL1(Descriptor d) {
        if(isValid()&&d.isValid() && (dsc.length==d.getSize())) {
            float ret = 0.f;
            float[] tmpDsc = d.getData();
            for(int ii=0; ii<dsc.length; ii++) {
                ret += Math.abs(dsc[ii]-tmpDsc[ii]);
            }
            return ret;
        } else {
            return -1.f;
        }
    }
    //
    public static Descriptor buildDsc(Mat img, int numBin, boolean isNormed) {
        Descriptor ret = new Descriptor();
        if(ret.build(img, numBin, isNormed)) {
            return ret;
        } else {
            return null;
        }
    }
    public boolean build(Mat img, int numBin, boolean isNormed) {
        boolean isOk = false;
        dsc = null;
        if( (img!=null) && (numBin>0) ) {
            if(img.depth()>1) {
                Imgproc.cvtColor(img, img, Imgproc.COLOR_BGR2GRAY);
            }
            Mat imgHist = new Mat();
            dsc = new float[numBin];
            Imgproc.calcHist(Arrays.asList(img), new MatOfInt(0), new Mat(), imgHist, new MatOfInt(numBin), new MatOfFloat(0f, 256f));
            imgHist.get(0,0,dsc);
            if(isNormed) {
                normalizeDsc();
            }
            isOk = true;
        }
        return isOk;
    }
    public void normalizeDsc() {
        if(isValid()) {
            float tmpSum = caclSum();
            if(tmpSum>Descriptor.DEF_FLOAT_THRESHOLD) {
                for (int ii=0; ii<dsc.length; ii++) {
                    dsc[ii] /= tmpSum;
                }
            } else {
                float constVal = 1.f/dsc.length;
                Arrays.fill(dsc, constVal);
            }
        }
    }
    public float caclSum() {
        if(isValid()) {
            float ret = 0.f;
            for (int ii=0; ii<dsc.length; ii++) {
                ret += Math.abs(dsc[ii]);
            }
            return ret;
        } else {
            return -1.0f;
        }
    }
    public int getSize() {
        if(isValid()) {
            return dsc.length;
        }
        return -1;
    }
    public int getSizeInBytes() {
        if(isValid()) {
            return (dsc.length*4); //FIXME:
        } else {
            return -1;
        }
    }
    public boolean isValid() {
        return (dsc!=null)&(dsc.length>0);
    }
    public float[] getData() {
        return dsc;
    }
    public String toString() {
        if(isValid()) {
            String strData = "arr[" + dsc.length + "] = {";
            for(int ii=0; ii<dsc.length; ii++) {
                strData += "" + dsc[ii] + ", ";
            }
            strData += "}";
            return strData;
        } else {
            return "dsc-isInvalid";
        }
    }
    //
    private float[] dsc = null;
}
