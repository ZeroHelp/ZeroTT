#!/bin/bash
x=0
while [ $x -le 1000 ]
do
  echo "<bean id=\"sleepTest$x\" class=\"com.taobao.nb.quartz.impl.MethodInvokingJob\"><property name=\"jobName\" value=\"sleep$x\"/><property name=\"arguments\"><list><value>$x</value></list></property><property name=\"targetObject\"><ref bean=\"helloWorldManager\"/></property><property name=\"targetMethod\"><value>sleep10</value></property></bean>"
  x=$(( $x + 1 ))
done
