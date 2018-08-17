#!/usr/bin/env bash

export USED_COMMAND=$(basename $0)

export CURRENT_WORKING_DIR=$PWD

if [ -f $HOME/IdeaProjects/greetgo.cmd/scripts/cmd.bash ] ; then
  cd $HOME/IdeaProjects/greetgo.cmd/scripts
  export GREETGO_DEBUG=1
  bash cmd.bash $*
  exit $?
fi

if ! which git > /dev/null ; then
  echo git does not installed >&2
  echo Please install git >&2
  echo You can install git with command: >&2
  echo "    " >&2
  echo "    sudo apt-get install git" >&2
  echo "    " >&2
  exit 1
fi

REPO_DIR=$HOME/.local/greetgo/repo

if [ ! -d $REPO_DIR/greetgo.cmd ] ; then
  mkdir -p $REPO_DIR
  cd $REPO_DIR
  git clone https://github.com/greetgo/greetgo.cmd.git
  if [ "$?" -ne "0" ] ; then
    exit $?
  fi
fi

cd $REPO_DIR/greetgo.cmd/scripts

bash cmd.bash $*
exit $?
