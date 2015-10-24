/**
 * Created by ar on 14.10.15.
 */

import by.grid.imlab.Descriptor
import by.grid.imlab.Imshow
import org.opencv.highgui.Highgui

object MainScala {
  def main (args: Array[String]){
    // (1) Load OpenCV dynamic library
    System.loadLibrary("opencv_java246")
    // (2) Load & visualize images, calculate Descriptors
    var ii = 0
    val numImg = 7
    val arrDsc = new Array[Descriptor](numImg)
    for(ii <- 0 until numImg) {
      val fn = "data/doge2_rot_scale_crop" + ii + ".png"
      val img = Highgui.imread(fn, Highgui.CV_LOAD_IMAGE_GRAYSCALE)
      Imshow.show(img)
      val dsc = Descriptor.buildDsc(img, Descriptor.DEF_BIN_NUMBER, true)
      arrDsc(ii) = dsc
      println(ii + " : " + dsc)
    }
    // (3) Check Descriptors distances
    println("--------[ Check Descriptors distances ]--------")
    val dstThresh = 0.1f
    val dsc0 = arrDsc(0)
    for(ii <- 1 until numImg) {
      val dst   = dsc0.dstL1(arrDsc(ii))
      var strOk = "Ok"
      if(dst>dstThresh) {
        strOk = "Bad"
      }
      println("Distance #0 and #" + ii + " is : " + dst + ",\tis [" + strOk + "]")
    }
  }
}
