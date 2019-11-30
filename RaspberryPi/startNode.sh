#!/usr/bin/env bash

echo "Starting up the JavaHVAC Raspberry PI Service..."
if [ ! -d ".venv" ]
then
  echo "Virtual environment does not yet exist. Creating now..."
  virtualenv .venv
  echo "Installing dependencies to virtual environment..."
  .venv/bin/pip install -r requirements.txt
  echo "Done setting up Virtual environment.,"
fi

.venv/bin/python pi-api.py
