# kamon-spm-example

Example App to demonsrate how to configure Kamon to use [SPM backend](http://kamon.io/documentation/kamon-sematext-spm/0.6.6/overview/) in order to monitor Akka/Play applications with [SPM](http://sematext.com/spm).


### Specify your SPM app token:

    vim src/main/resources/application.conf

    spm {
       token = "[place-token-here]"
    }

... as you can see in [application.conf](https://github.com/sematext/kamon-spm-example/blob/master/src/main/resources/application.conf)

### Start application:
   
    sbt run
 
