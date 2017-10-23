#!/usr/bin/env bash

REPO_URL=$HOME/IdeaProjects/greetgo.cmd
if ! [ -d $REPO_URL ] ; then
  REPO_URL=https://github.com/greetgo/greetgo.cmd.git
fi
P_NAME=greetgo.cmd

if ! which git > /dev/null ; then
  echo git does not installed >&2
  echo Please install git >&2
  echo You can install git with command: >&2
  echo "    " >&2
  echo "    sudo apt-get install git" >&2
  echo "    " >&2
  exit 1
fi

export CWD=$PWD

REPO_DIR=$HOME/.local/greetgo/repo

if [ ! -d $REPO_DIR/$P_NAME ] ; then
  mkdir -p $REPO_DIR
  cd $REPO_DIR
  git clone $REPO_URL $P_NAME
  if [ "$?" -ne "0" ] ; then
    exit $?
  fi
fi

cd $REPO_DIR/$P_NAME/scripts

bash cmd.bash $*
