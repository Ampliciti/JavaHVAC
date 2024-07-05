#!/bin/bash
#Install script that fully installs the service on a Pi.
#To run: ./install.sh

echo "Setting up the JavaHVAC Raspberry PI Service..."
if [ "$EUID" -ne 0 ]
  then echo "Please run as root."
  exit
fi

if [ ! -d "/home/pi" ]
then
  echo "This script should only be installed on a RaspberryPi."
  exit
fi

echo "Installing debain dependencies..."
apt-get update
apt-get -y install python3-pip
apt-get -y install virtualenv
apt-get -y install python3-dev
apt-get -y install python3-rpi.gpio
echo "Done installing debian dependencies."

APP_HOME=/home/pi/JavaHVAC-Node
mkdir -p $APP_HOME

if [ ! -d "$APP_HOME/.venv" ]
then
  echo "Virtual environment does not yet exist. Creating now..."
  virtualenv $APP_HOME/.venv
  echo "Installing dependencies to virtual environment..."
  $APP_HOME/.venv/bin/pip install -r requirements.txt
  echo "Done setting up Virtual environment."
fi
chown -R pi:pi $APP_HOME

echo "Setting up service..."
mv ./JavaHVAC-Node.service /etc/systemd/system/
systemctl daemon-reload
systemctl enable JavaHVAC-Node.service

#one wire temp sensor setup
ONE_WIRE="dtoverlay=w1-gpio"
if grep "$ONE_WIRE" /boot/config.txt
then
  echo "One-wire interface for temperature sensors already enabled."
else
  echo "One-wire interface for temperature sensors not enabled; enabling now..."
  echo "$ONE_WIRE" >> /boot/config.txt
  echo "You will need to restart this PI after installation is complete to enable on-wire."
fi
echo "Done with installation... attempting to start up!"
service JavaHVAC-Node start
