#!/bin/bash
x=0
while [ $x -le 1000 ]
do
  echo "INSERT INTO \`lottery_timetask_control\` VALUES ('sleep$x',1,'*/1 * * * * ?','10.32.20.67','test',1,'magic','',CURRENT_TIMESTAMP,CURRENT_TIMESTAMP,CURRENT_TIMESTAMP,CURRENT_TIMESTAMP,'categeory');"
  x=$(( $x + 1 ))
done

