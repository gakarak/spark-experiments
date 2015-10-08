#!/usr/bin/env python

import matplotlib.pyplot as plt
import pyspark as sp

import numpy as np

from pyspark.mllib.clustering import KMeans, KMeansModel
from numpy import array
from math import sqrt

from pyspark import SparkContext


if __name__=='__main__':
	sc = SparkContext(appName="Test-KMeans")
	# Load and parse the data
	fn='/home/ar/dev/spark-1.5.0-bin-hadoop2.6/data/mllib/gmm_data.txt'
	data = sc.textFile(fn)
	parsedData = data.map(lambda line: array([float(x) for x in line.strip().split(' ')]))

	lstNumCls=range(2,12,3)
	sizLstNumCls=len(lstNumCls)

	for ii in xrange(len(lstNumCls)):
	    knum=lstNumCls[ii]
	    # Build the model (cluster the data)
	    clusters = KMeans.train(parsedData, knum, maxIterations=10, runs=10, initializationMode="random")
	    
	    # Evaluate clustering by computing Within Set Sum of Squared Errors
	    def error(point):
	        center = clusters.centers[clusters.predict(point)]
	        return sqrt(sum([x**2 for x in (point - center)]))
	    
	    WSSSE = parsedData.map(lambda point: error(point)).reduce(lambda x, y: x + y)
	    print("Within Set Sum of Squared Error = " + str(WSSSE))
	    
	    # Prepare Cluster-Labels for data
	    clsLabels=parsedData.map(lambda point: clusters.predict(point))
	    
	    # Convert to numpy Objects: if large data - need sampling
	    npDat=np.array(parsedData.collect())
	    npCls=np.array(clsLabels.collect())
	    
	    # Matplotlib-Visualization
	    plt.figure()
	    plt.title('#cls=%s, WSSSE=%s' % (knum, WSSSE))
	    plt.scatter(npDat[:,0], npDat[:,1], c=npCls)
	plt.show()