server="lvuser@172.22.11.2"
serverDir="/home/lvuser/log"
clientDir="C:/Users/BUZZ-175/RoboRIOLogs/"

# Copy latest log from RIO to RoboRIOLogs folder
scp $server:$serverDir/$(ssh $server 'ls /home/lvuser/log/ -t | head -1') $clientDir
vi 'ls C:/Users/BUZZ-175/RoboRIOLogs/ -t | head -1'