#!/usr/bin/bash
# Starts up the RaspberryPi node with a single, no arguments, command.

echo "Starting up the JavaHVAC Raspberry PI Service..."
#if [ ! -d ".venv" ]
#then
  #echo "Virtual environment does not yet exist. Creating now..."
  #virtualenv .venv
  #echo "Installing dependencies to virtual environment..."
  #.venv/bin/pip install -r requirements.txt
  #echo "Done setting up Virtual environment."
#fi

nohup python pi-api.py &
