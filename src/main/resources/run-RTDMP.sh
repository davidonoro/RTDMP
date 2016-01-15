$SPARK_HOME/bin/spark-submit --master local[4]
                             --files $PATHTOFILE/ReglasTest.xlsx
                             --class com.pebd.rtdmp.RTDMP
                             ./target/RTDMP-0.0.1-SNAPSHOT-jar-with-dependencies.jar
                             localhost:9092
                             TOPICO.RTDMP
                             $PATHTOFILE/ReglasTest.xlsx
