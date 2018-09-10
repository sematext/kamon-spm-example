# kamon-spm-example

Example App to demonsrate how to configure Kamon to use [SPM backend](https://kamon.io/documentation/1.x/reporters/sematext/) in order to monitor Akka/Play applications with [SPM](http://sematext.com/spm).


### Specify your SPM app token:

    vim src/main/resources/application.conf

    spm {
       token = "[place-token-here]"
    }

... as you can see in [application.conf](https://github.com/sematext/kamon-spm-example/blob/master/src/main/resources/application.conf)

### Start application:
   
    sbt run
 
