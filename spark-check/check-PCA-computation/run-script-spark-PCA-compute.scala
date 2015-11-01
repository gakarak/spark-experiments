import org.apache.spark.{SparkConf, SparkContext}
import org.apache.spark.SparkContext._
import org.apache.spark.mllib.linalg._
import org.apache.spark.mllib.linalg.distributed.RowMatrix
import breeze.linalg.{Vector, DenseVector, squaredDistance, DenseMatrix}
import java.io._


def saveMatToFile(fout: String, mat: DenseMatrix[Double]) {
    val f = new PrintWriter(fout)
    for (rr <- 0 until mat.rows) {
        for(cc <- 0 until mat.cols) {
            if(cc>0) {
                f.print(", ")
            }
            f.print("" + mat(rr,cc))
        }
        f.println()
    }
    f.close()
}

def saveArr2ToFile(fout: String, arr: Array[org.apache.spark.mllib.linalg.Vector]) {
	val f = new PrintWriter(fout)
	for (rr <- 0 until arr.length) {
		for(cc <- 0 until arr(0).size) {
	    	if(cc>0) {
	      		f.print(", ")
	    	}
	    	f.print("" + arr(rr)(cc))
	  	}
	  	f.println()
	}
	f.close()
}

// (1) Read data from file:
val rddTXT = sc.textFile("data_2d_gauss_rot_shift.csv")
val data = rddTXT.map(_.split(",").map(_.toDouble))
//
val dataLocal=data.collect()
val dataLocalV = for {ii <-dataLocal} yield Vectors.dense(ii)
val dataVDist = sc.parallelize(dataLocalV)
val dataMat = new RowMatrix(dataVDist)
//
val dataPCA = dataMat.computePrincipalComponents(dataMat.numCols().toInt)
val dmatPCA = DenseMatrix(dataPCA.toArray).reshape(dataPCA.numRows, dataPCA.numCols)
val foutDMatPCA : String = "fdata_spark_pca2.csv"
saveMatToFile(foutDMatPCA, dmatPCA)
//
val dataPrjDist = dataMat.multiply(dataPCA)
val foutDdataPrj : String = "fdata_spark_prj2.csv"
saveArr2ToFile(foutDdataPrj, dataPrjDist.rows.collect)
