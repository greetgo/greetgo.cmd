#!/usr/bin/env bash

SHOW_TO_REBOOT=0

if [ ! -d $HOME/bin/ ] ; then
  SHOW_TO_REBOOT=1
  mkdir $HOME/bin/
fi

curl https://raw.githubusercontent.com/greetgo/greetgo.cmd/master/scripts/greetgo.bash > $HOME/bin/gg

chmod +x $HOME/bin/gg

if [ "$SHOW_TO_REBOOT" = "1" ] ; then
  echo "Installation complete. Please reboot the system and type gg in terminal"
  exit 0
fi

echo "Installation complete."

gg
