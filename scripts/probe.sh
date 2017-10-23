#!/usr/bin/env bash

PORT=58207

#curl -V

#printf "line 1\nline 2" | nc localhost $PORT

echo -n $'asd\ndsa\n'| nc localhost $PORT
