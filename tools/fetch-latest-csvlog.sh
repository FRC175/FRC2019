server="lvuser@172.22.11.2"
serverDir="/home/lvuser/csvlog/telemetry.csv"
clientDir="C:/Users/BUZZ-175/RoboRIOCSVLogs/telemetry.csv"

scp $server:$serverDir $clientDir

vi $clientDir