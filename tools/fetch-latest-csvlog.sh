result=scp lvuser@172.22.11.2:/home/lvuser/csvlog/tuning-data.csv C:/Users/BUZZ-175/RoboRIOCSVLogs/tuning-data.csv
if [[ -n $result ]]
then
    scp scp lvuser@10.1.75.2:/home/lvuser/csvlog/tuning-data.csv C:/Users/BUZZ-175/RoboRIOCSVLogs/tuning-data.csv
fi
python plot.py