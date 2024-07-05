#!/bin/bash
# Starts up the RaspberryPi node with a single, no arguments, command.

echo "Starting up the JavaHVAC Raspberry PI Service..."
nohup .venv/bin/python3 pi-api.py &
