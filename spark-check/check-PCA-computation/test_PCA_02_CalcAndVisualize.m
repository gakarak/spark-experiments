close all;
clear all;

foutData='data_2d_gauss_rot_shift.csv';
data=csvread(foutData);

figure,
subplot(2,3,1),
scatter(data(:,1), data(:,2));
title(sprintf('OrigData: mean=%s, std=%s', mat2str(mean(data),2), mat2str(std(data),2)));
xlim([-5,5]), ylim([-5,5]);
grid on;
drawnow;

%%
[coeff,score,latent,tsquared,explained,mu] = pca(data);
dataPrj=(data - repmat(mu,size(data,1),1) )*coeff;
disp('PCA-Matlab:');
disp(coeff);

subplot(2,3,2),
scatter(dataPrj(:,1), dataPrj(:,2));
title(sprintf('MATLAB-PCA: mean=%s, std=%s', mat2str(mean(dataPrj),2), mat2str(std(dataPrj),2)));
xlim([-5,5]), ylim([-5,5]);
grid on
drawnow;

muPrj=mu*coeff;
siz=size(data);
dataPrjUnprj=(dataPrj+repmat(muPrj,siz(1),1))/coeff;

subplot(2,3,3),
scatter(dataPrjUnprj(:,1), dataPrjUnprj(:,2));
title(sprintf('MATLAB-UNPCA: mean=%s, std=%s', mat2str(mean(dataPrjUnprj),2), mat2str(std(dataPrjUnprj),2)));
xlim([-5,5]), ylim([-5,5]);
grid on

%%
fcsvSparkPCA='fdata_spark_pca2.csv';
fcsvSparkPrj='fdata_spark_prj2.csv';
if exist(fcsvSparkPCA,'file')==2
    dataSparkPCA=csvread(fcsvSparkPCA);
    dataSparkPrj=csvread(fcsvSparkPrj);

    disp('PCA-Spark:');
    disp(dataSparkPCA);

    subplot(2,3,4),
    scatter(dataSparkPrj(:,1), dataSparkPrj(:,2));
    title(sprintf('Spark-Prj: mean=%s, std=%s', mat2str(mean(dataSparkPrj),2), mat2str(std(dataSparkPrj),2)));
    xlim([-5,5]), ylim([-5,5]);
    grid on
    drawnow;

    %%
    dstMatlab=pdist2(dataPrj(:,1),dataPrj(:,1));
    dstSpark=pdist2(dataSparkPrj(:,1),dataSparkPrj(:,1));
    disp('Correlation of 1-st PCA-Projection between MATLAB and Spark:');
    corrCoef=corr2(dstMatlab, dstSpark);
    disp(corrCoef);
    subplot(2,3,5),
    imshow(dstMatlab,[]),   title('dst-Map: Matlab (1-st PC)');
    subplot(2,3,6),
    imshow(dstSpark,[]),    title('dst-Map: Spark (1-st PC)');
else
    disp(['!!! WARNING !!! Spark PCA-Data does not exist!: ', fcsvSparkPrj]);
end