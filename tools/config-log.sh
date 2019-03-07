# A script to create log folders on RoboRIO if they do not exist.

ssh lvuser@10.1.75.2

if [ ! -d "/home/lvuser/log" ]
then
    mkdir /home/lvuser/log
fi

if [ ! -d "/home/lvuser/csvlog" ]
then
    mkdir /home/lvuser/csvlog
fi