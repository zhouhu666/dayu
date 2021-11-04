#!/bin/bash
#source ../env.sh
#JAVA_OPTS参数根据需要添加，尤其是内存相关参数，并且注意这里的设置会覆盖分组设置中的同名值
JAVA_OPTS="-Xms=$XMS -Xmx=$XMX xxx"
PROC_NAME=java # 进程名 java类的都为java
serviceName="p2p-test"
# 开发、测试启动命令
START_COMMAND="java $JACOCO_OPTS -Xmx6g -Xms6g -Xmn3g  -XX:MetaspaceSize=128m -XX:MaxMetaspaceSize=128m -XX:+UseParNewGC -XX:+UseConcMarkSweepGC -XX:CMSFullGCsBeforeCompaction=5 -XX:+UseCMSCompactAtFullCollection -XX:ParallelGCThreads=8 -Xloggc:gc.log -XX:+PrintGCDetails -XX:+PrintGCDateStamps -XX:+HeapDumpOnOutOfMemoryError -XX:HeapDumpPath=/data/logs/outofmemory/gcServer.hprof -jar ./${serviceName}.jar --spring.config.location=./conf/application.properties" #replae this
# 线上启动命令
START_PRODUCT_COMMAND="java -Xmx16g -Xms16g -Xmn8g  -XX:MetaspaceSize=128m -XX:MaxMetaspaceSize=128m -XX:+UseParNewGC -XX:+UseConcMarkSweepGC -XX:CMSFullGCsBeforeCompaction=5 -XX:+UseCMSCompactAtFullCollection -XX:ParallelGCThreads=8 -Xloggc:gc.log -XX:+PrintGCDetails -XX:+PrintGCDateStamps -XX:+HeapDumpOnOutOfMemoryError -XX:HeapDumpPath=/data/logs/outofmemory/gcServer.hprof -jar ./${serviceName}.jar --spring.config.location=./conf/application.properties" #replae this
PROC_URI= #需要检查的URI路径比如/status  为空则不检查
PROC_PORT=8083 #服务的PORT
RESPONSE=OK #这个添加你需要从返回结果里面获取的值
WAIT_TIME=120 # 等待启动/停止时间

PROC_NAME=${PROC_NAME::15}


help(){
    echo "${0} <start|stop|restart|status>"
    exit 1
}

checkhealth(){
 if [[ -n "$PROC_URI" ]] ; then
        curl -is "http://localhost:$PROC_PORT$PROC_URI" | grep "$RESPONSE" >/dev/null 2>&1
        if [ "$?" = 0 ] ; then
        echo "running"
        return 0
        fi
        echo " not running"
        return 1

elif [[ -n "$PROC_PORT" ]] ; then
        PORT_PROC=$(/usr/sbin/ss -nltp "( sport = :$PROC_PORT )" |sed 1d |awk '{print $NF}' |awk -F'"' '{print $2}'|uniq)
          if [ X"$PORT_PROC" = X"$PROC_NAME" ] ; then
                echo "running"
            return 0
           fi
           echo "not running"
           return 1
else
         ps -eo comm,pid |grep -P  "^$PROC_NAME\b"
                if [ "$?" = 0 ] ; then
                echo "running"
                return 0
                fi
               echo "not running"
               return 1

fi
}

start(){
    checkhealth
    if [ $? = 0 ]; then
        echo "[WARN] $PROC_NAME is aleady running!"
        return 0
    fi

#    nohup $START_COMMAND  </dev/null &>>./log/${PROC_NAME}_output.log  &
    echo ${env} > ./test.log
    if [[ ${env} == "product" ]]; then
        nohup $START_PRODUCT_COMMAND  </dev/null &>> /dev/null &
    else
       nohup $START_COMMAND  </dev/null &>> /dev/null &
    fi

    for i in $(seq $WAIT_TIME) ; do
        sleep 1
        checkhealth
        if [ $? = 0 ]; then
            echo "Start $PROC_NAME success"
            return 0
        fi
    done
    echo "[ERROR] Start $PROC_NAME failed"
    return 1
}

stop(){
    if [[ -n "$PROC_PORT"  ]] ; then
        PROC_ID=$(  /usr/sbin/ss -nltp "( sport = :$PROC_PORT )" |sed 1d  | awk '{print $NF}' |  grep -oP '\,.*\,' | grep -oP "\d+" |  uniq )
    else
        PROC_ID=$(ps -eo comm,pid  | grep "^$PROC_NAME\b" |awk '{print $2}')
    fi

    if [[ -z "$PROC_ID" ]] ; then
        echo "[WARN] $PROC_NAME is aleady exit, skip stop"
        return 0
    fi

    checkhealth
    if [ "$?" != "0" ] ; then
        echo "[WARN] $PROC_NAME is aleady exit, skip stop"
        return 0
    fi
    kill $PROC_ID
    for i in $(seq $WAIT_TIME) ; do
        sleep 1
        checkhealth
        if [ "$?" != "0" ] ; then
            echo "Stop $PROC_NAME success"
            return 0
        fi
    done

    kill -9 $PROC_ID
    sleep 1
    checkhealth
    if [ "$?" != "0" ] ; then
        echo "Stop $PROC_NAME success"
        return 0
    fi

    echo "[ERROR] Stop $PROC_NAME failed"
    return 1
}
DIR="log"
FILE="java_output.log"


if [ -d "${DIR}" ]; then
   echo "当前存在log目录，删除当前log目录"
   rm -rf ${DIR}
fi
#mkdir ${DIR}
#touch ${DIR}/${FILE}

start
