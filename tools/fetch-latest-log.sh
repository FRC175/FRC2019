server="lvuser@172.22.11.2"
serverAlt="lvuser@10.1.75.2"
serverDir="/home/lvuser/log"
clientDir="C:/Users/BUZZ-175/RoboRIOLogs"

# Copy latest log from RIO to RoboRIOLogs folder
# Use radio ip address if tether ip address does not work
result=scp $server:$serverDir/$(ssh $server 'ls /home/lvuser/log/ -t | head -1') $clientDir
if [[ -n $result ]]
then
    scp $serverAlt:$serverDir/$(ssh $serverAlt 'ls /home/lvuser/log/ -t | head -1') $clientDir
fi

# Open log in vi
vi $clientDir/`ls $clientDir -t | head -1`