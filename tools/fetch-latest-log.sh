server="lvuser@172.22.11.2"
serverDir="/home/lvuser/log"
clientDir="C:/Users/BUZZ-175/RoboRIOLogs/"

# Copy latest log from RIO to RoboRIOLogs folder
scp $server:$serverDir/$(ssh $server 'ls -t /home/lvuser/log | head -1') $clientDir
vi $clientDir/'ls -t /home/lvuser/csvlog | head -1'