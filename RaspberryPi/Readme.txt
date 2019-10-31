To setup on a RaspberryPi node:

#Setup system:
https://learn.adafruit.com/adafruits-raspberry-pi-lesson-11-ds18b20-temperature-sensing/ds18b20

#Setup software:
sudo apt-get update
sudo apt-get -y install python-pip
sudo apt-get -y install virtualenv
sudo apt-get -y install python-dev
sudo apt-get -y install python-rpi.gpio
cd $project_home/RaspberryPi
virtualenv .venv
.venv/bin/pip install -r requirements.txt

#run:
.venv/bin/python pi-api.py

#supported routes
GET /info -- gets all info needed for a server to make decisions (all non-hardware info)
GET /sensor/$sensor_name -- gets all info relating to the sensor (including hardware info)
GET /actor/$relay_name -- gets all info related to the relay (including hardware info)
POST /action with body: {"name":<zoneOrSourceName>:,"state":true|false}"
