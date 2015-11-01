close all;
clear all;

% (1) Prepare gaussian data:
rndData=randn(1000,2);
rndData(:,2)=0.1*rndData(:,2);
% (2) Rotate data:
ang=30.;
cosa=cos(ang*pi/180);
sina=sin(ang*pi/180);
mrot=[cosa,+sina; -sina, cosa];
rndDataRot=rndData*mrot;
% (3) Shift data:
dxy=repmat([1,1],size(rndDataRot,1),1);
data=rndDataRot+dxy;

foutData='data_2d_gauss_rot_shift.csv';
csvwrite(foutData, data);
