To setup on a RaspberryPi node:

#Setup system:
https://learn.adafruit.com/adafruits-raspberry-pi-lesson-11-ds18b20-temperature-sensing/ds18b20

#Setup software:
sudo apt-get install python-pip
sudo apt-get install virtualenv
cd $project_home/RaspberryPi
virtualenv .venv
.venv/bin/pip install -r requirements.txt

#run:
.venv/bin/python pi-api.py
