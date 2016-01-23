# RTDMP

Plataforma de venta de clasificación de usuarios real time, desarrollada con Spark Streaming y Drools como motor de reglas.
Las reglas de clasificacion de usuarios se leen de una hoja excel, de tal manera que se ha separado la logica de negocio de la
parte tecnologica.

La aplicacion lee mensajes de un tópico Kafka, procesa los datos y almacena la información en un servidor Redis.
Parametros de ejecucion:
* Servidor Kafka (host:puerto)
* Topico
* Hoja de reglas de negocio

Se adjuntan 2 cuadros de mando para explotar la informacion de los usuarios

[Video explicativo del proyecto](https://www.youtube.com/watch?v=T-nOoRAjmjc)