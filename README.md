ElasticSearch Sink for Spring XD
================================

What is this?
-------------

This is a Spring XD sink for indexing data to ElasticSearch. The sink can operate in one of the three following modes:

- http - using the HTTP indexing API
- transport - using the 'native' TransportClient support
- node - using the 'native' NodeClient support

By default, the sink uses the transport mode.

NOTE: Due to classloading issues between ElasticSearch, and the hierarchical module classloader of Spring XD (
i.e resorting to the parent classloader for retrieving resources and deserialization), ElasticSearch and Lucene 
are not currently packaged in the module, but deployed in the shared library folder of Spring XD.

How can I use it?
-----------------

Build it

    ./gradlew clean xdModule
    

Deploy ElasticSearch if using transport or node modes

    ./gradlew elasticSearchDeploy -PxdHome=<spring-xd-home>

Note: if the environment variable XD_HOME is already defined, then the `-PxdHome` parameter can be ommitted.

Deploy the module

    ./gradlew xdModuleDeploy -PxdHome=<spring-xd-home>
    
Note: as above, if the environment variable XD_HOME is already defined, then the `-PxdHome` parameter can be ommitted.

Creating a stream definition
----------------------------

The expected input are Strings containing JSON documents, which are indexed into a given ElasticSearch instance or cluster.

The following configuration parameters are common:

- `mode` - the sink mode (optional, defaults to transport)
- `index` - the index where the document will be inserted (mandatory)
- `type` - the type of the document being inserted (mandatory)
- `idPath` - the path to the id field of the document (optional, the id will be assigned by ElasticSearch if not present)

The following configuration parameters are specific to `http` mode:
- `protocol` - the protocol to use (HTTP or HTTPS) (optional, defaults to HTTP)
- `hosts` - a comma-separated list of hosts where the data will be sent, in the `host:port` format (must be specified for HTTP)

The following configuration parameters are specific to the `transport` mode:
- `hosts` - a comma-separated list of hosts where the data will be sent, in the `host:port` format
- `clusterName` - the name of the ElasticSearch cluster (required)

The following configuration parameters are specific to the `node` mode:
- `clusterName` - the name of the ElasticSearch cluster (required)

Usage examples
--------------

Transport mode, id assigned from document

    stream create --name twittersearchWorldCup --definition "twittersearch --consumerKey=<consumerKey> --consumerSecret=<consumerSecret< --query='#WorldCup' | elasticsearch --mode=transport --idPath=$.id --hosts=localhost:9300,localhost:9301 --index=twitter --type=tweet --clusterName=elasticsearch" --deploy

HTTP mode, id automatic

    stream create --name twittersearchWorldCup --definition "twittersearch --consumerKey=<consumerKey> --consumerSecret=<consumerSecret< --query='#WorldCup' | elasticsearch --mode=http --hosts=localhost:9200 --index=twitter --type=tweet" --deploy
 
Node mode, id automatic   

    stream create --name twittersearchWorldCup --definition "twittersearch --consumerKey=<consumerKey> --consumerSecret=<consumerSecret< --query='#WorldCup' | elasticsearch --mode=node --index=twitter --type=tweet --clusterName=elasticsearch" --deploy
    
Known issues
------------

    Currently the node mode may generate PermGen issues after intensive use.
