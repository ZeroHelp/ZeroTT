#!/bin/bash
x=0
while [ $x -le 1000 ]
do
  echo "<ref local=\"sleepTest$x\" />"
  x=$(( $x + 1 ))
done
