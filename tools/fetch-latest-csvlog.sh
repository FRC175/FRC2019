server="lvuser@172.22.11.2"
serverDir="/home/lvuser/csvlog"
clientDir="C:/Users/BUZZ-175/RoboRIOCSVLogs/"

scp $server:$serverDir/$(ssh $server 'ls -t /home/lvuser/csvlog | head -1') $clientDir