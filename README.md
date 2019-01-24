# Play REST API

[![Build Status](https://travis-ci.org/playframework/play-scala-rest-api-example.svg?branch=2.6.x)](https://travis-ci.org/playframework/play-scala-rest-api-example)

This is the example project for [Making a REST API in Play](http://developer.lightbend.com/guides/play-rest-api/index.html).


# Database

    Database Config: application.conf

    Database Name crud-api
    
    CREATE TABLE IF NOT EXISTS `person` (
      `id` int(11) NOT NULL AUTO_INCREMENT,
      `name` varchar(250) NOT NULL,
      `email` varchar(250) NOT NULL,
      `country` varchar(100) NOT NULL,
      PRIMARY KEY (`id`)
    );
