#!/usr/bin/env bash

if [ "$*" == "up" ] ; then
  git pull
  exit 0
fi
