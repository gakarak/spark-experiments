# Check Spark PCA computation
- run MATLAB script test_PCA_01_Projection_Generate.m to generate 2d-data
- run scala script in spark-shell (just :load run-script-spark-PCA-compute.scala) to compute Spark PCA and project 2d-data
- run MATLAB script test_PCA_02_CalcAndVisualize.m to visualize results
