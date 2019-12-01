#What this is:
This is a bit of Python code that is intended to run on a RaspberryPi to act on behalf of the JavaHVAC server and gather sensor data.

#To install:
1. Login to the Pi and ensure you're in /home/pi
2. wget https://github.com/Ampliciti/JavaHVAC/raw/master/JavaHVAC-Node.tar.gz
3. tar xvfz JavaHVAC-Node.tar.gz
4. cd JavaHVAC-Node
5. sudo ./install.sh
6. Copy over or modify the config files for your needs. Restart the service after you make any config changes.

#Running as a service
The installation script will run the script as a systemd service. Standard systemd commands will work.
sudo service JavaHVAC-Node status
sudo service JavaHVAC-Node start
sudo service JavaHVAC-Node stop
sudo service JavaHVAC-Node restart



#Manual setup system (do this first):
https://learn.adafruit.com/adafruits-raspberry-pi-lesson-11-ds18b20-temperature-sensing/ds18b20

#Run script:
./startNode.sh

#Manual software setup:
sudo apt-get update
sudo apt-get -y install python-pip
sudo apt-get -y install virtualenv
sudo apt-get -y install python-dev
sudo apt-get -y install python-rpi.gpio
cd $project_home/RaspberryPi
virtualenv .venv
.venv/bin/pip install -r requirements.txt

#Manual run:
.venv/bin/python pi-api.py
#or better yet:
nohup .venv/bin/python pi-api.py & 2> nohup.out

#supported routes
GET /info -- gets all info needed for a server to make decisions (all non-hardware info)
Example response body:
{
    "zones": [],
    "sources": [
        {
            "source": "cistern",
            "name": "cisternBottom",
            "temp": 42.9116
        },
        {
            "source": "cistern",
            "name": "cisternInlet",
            "temp": 42.461600000000004
        },
        {
            "source": "cistern",
            "name": "cisternTop",
            "temp": 43.025
        },
        {
            "source": "cistern",
            "name": "cisternHotWaterPreheat",
            "temp": 40.55
        },
        {
            "source": "cistern",
            "state": false,
            "name": "recirculatorPump"
        },
        {
            "source": "cistern",
            "state": false,
            "name": "barnPump"
        },
        {
            "source": "cistern",
            "state": false,
            "name": "housePump"
        }
    ],
    "misc": [
        {
            "name": "EastTemp",
            "temp": 53.2616
        }
    ],
    "name": "pump-pi",
    "address": "pump-pi.lan:5000"
}
GET /sensor/$sensor_name -- gets all info relating to the sensor (including hardware info)
Example response body:
{
    "model": "DS18B20",
    "name": "EastTemp",
    "temp": 53.15,
    "address": "28-0000087f0ccc"
}
GET /actor/$relay_name -- gets all info related to the relay (including hardware info)
Example response body:
{
    "GPIO": 8,
    "state": false,
    "name": "housePump",
    "source": "cistern"
}
POST /action with body: {"name":<zoneOrSourceName>:,"state":true|false}"
Example response body:
{
    "GPIO": 3,
    "state": true,
    "name": "recirculatorPump",
    "source": "cistern"
}
