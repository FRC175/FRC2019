$server = "lvuser@172.22.11.2"
$serverDir = "~/log/"
$clientDir = "C:/Users/BUZZ-175/RoboRIOLogs/"

scp $server:$serverDir/$(ssh $server 'ls -t $serverDir | head -1') $clientDir