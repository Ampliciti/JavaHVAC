#!/bin/bash

echo "Setting up the JavaHVAC Raspberry PI Service..."
if [ "$EUID" -ne 0 ]
  then echo "Please run as root"
  exit
fi

if [ ! -d "/home/pi" ]
then
  echo "This script should only be installed on a RaspberryPi."
  exit
fi
