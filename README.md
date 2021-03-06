# SCM Messaging Library

Link to umbrella project: [>> System's Configuration Management <<](https://github.com/ametiste-oss/ametiste-scm)

## Build Status
[![Build Status](https://travis-ci.org/ametiste-oss/ametiste-scm-messaging.svg?branch=master)](https://travis-ci.org/ametiste-oss/ametiste-scm-messaging)
[![Codacy Badge](https://api.codacy.com/project/badge/7ad6ce9909944604a65fa57e4d48d2a7)](https://www.codacy.com/app/bliznyuk-andrey/ametiste-scm-messaging)
[![codecov.io](https://codecov.io/github/ametiste-oss/ametiste-scm-messaging/coverage.svg?branch=master&precision=2)](https://codecov.io/github/ametiste-oss/ametiste-scm-messaging?branch=master)
[ ![Download](https://api.bintray.com/packages/ametiste-oss/maven/scm-messages/images/download.svg) ](https://bintray.com/ametiste-oss/maven/scm-messages/_latestVersion)

## Table Of Content
- [Overview](#overview)
- [Modules description](#modules-description)
  - [Entity modules](#entity-modules)
  - [Service modules](#service-modules)
- [Core Components](#core-components)
  - [Sender](#sender)
  - [Receiver](#receiver)
  - [Data models](#data-models)
  - [Components Usage](#components-usage)
    - [Event sender](#event-sender)
    - [Event receiver](#event-receiver)
- [Clients](#clients)
  - [Startup Event Sender](#startup-event-sender)
    - [Usage](#usage)
    - [Logic of operations](#logic-of-operations)
- [How To](#how-to)
  - [Add new event type](#add-new-event-type) 

## Overview

*Messaging Library* is a common part of **System's Configuration Management** system. It contains all needed components for organizing messaging between system modules: event and message models, sender and receiver components.

This structure encapsulates transport layer of messaging process that allows simple change or extend it. For example, add new type of events or change protocol. All you need it's update version of messaging library module in your application.

_Note_: readme contains all main information about library for now and soon will be divided to separate wiki pages. In this file stay only common information about components and usage.

## Modules description

Library contains set of modules, each of them responsible for one of processing aspects:

- scm-messages;
- scm-messages-http;
- scm-messages-mongo;
- scm-message-receiver;
- scm-message-receiver-starter;
- scm-message-sender;
- scm-message-sender-client;
- scm-message-sender-client-starter.

###### Entity modules

Central module of library is **scm-messages**. Module contains definition of basic data models: `Event` and `Message`. Event incapsulate information about system conponent changes. It's abstract class and all event types must extend this. Message is a transport object that contains event payload with all needed information for transporting.

**scm-messages-http** and **scm-messages-mongo** modules contains definition of specific data models reprasentation and service components for it. *http* module contains DTOs for transporting events via HTTP protocol with saving type inforamtion. For data serialization module use *Jackson JSON Processor*. It used by sender and receiver to communicate. *mongo* module defines entities for saving events in Mongo DB storage. Log service use it by default.

If you want add new event type you need add only three classes. For more details see section [How To](#how-to).

###### Service modules

**scm-message-sender** module defines *EventSender* interface and contains its implementations. Default implementation used only Apache HTTP Client and don't have any other dependencies. It's allows easily integrate to any service without any ristrictions. Sender incapsulate all transport details and provide simple interface to send messages.

**scm-message-receiver** module contains event receiving components. They receive massages using one of the transport protocols, transform DTO to events and organize them publishing. Module designed for using in Spring Applications and for publishing it used Spring _ApplicationEventPublisher_.
If you need use other framework you can simply provide API method for receiving messages itself.

**scm-message-sender-client** module contains clients of sender module that provides fucntionality for creating and send events about different aspects of service lifecycle.

###### Starters

Library oriented for usage with Spring Framework and contains two SpringBoot starter for simple add libarary functionality to application context.

**scm-message-receiver-starter** add to context event receiving and publishing mechanisms.

**scm-message-sender-client-starter** module add to applcaition context logic of instance lifecycle event sending.

## Core Components

Main components is sender, receiver, data models and DTO.

### Sender
Sender performs message composition and transmitting to system component.
![Sender class diagram](https://cloud.githubusercontent.com/assets/11256858/10567346/544e524e-760a-11e5-9ede-be3979189729.png)

### Receiver
Receiver is a endpoint that receive messages, convert them to events and publish with ApplicationEventPublisher.
![Receiver class diagram](https://cloud.githubusercontent.com/assets/11256858/10567347/56d7e476-760a-11e5-916f-f25f52301c9f.png)

### Data models
Represents group of entity that system operates.
![Data models](https://cloud.githubusercontent.com/assets/11256858/11195606/630c7186-8cbb-11e5-98a8-f5c69f423802.png)

### Components Usage

#### Event Sender

##### Configuration
To use Event Sender you need add to project dependencies _scm-message-sender.jar_ module. If you use Gradle:
```java
repository {
  jcenter()
}

dependencies {
  compile "org.ametiste.scm:scm-message-sender:{version}
}
```
Than create instance of EventSender and use it.
```java
HttpClient client = HttpEventSender.createHttpClient(1000, 1000);
EventSender sender = new HttpEventSender(client);
```
_Note_: to create sender you can any Apache HttpClient object. If you don't have it you can create client with HttpEventSender static method createHttpClient(connectionTimeout, readTimeout).

##### Supported environment
- JDK 1.8 or higher

##### Example of usage
```java
// create sender
EventSender sender = new HttpEventSender(HttpEventSender.createHttpClient(1000, 1000););
 
// prepare message
Map<String, Object> properties = new HashMap<>();
properties.put("dbUrl", new URI("http://localhost:9300/schema"));
properties.put("retry", 15);
 
URI target = new URI("http://192.168.0.5/endpoint");
URI excluded = new URI("http://192.168.0.5/endpoint");
 
InstanceLifecycleEvent event = InstanceLifecycleEvent.builder()
        .type(STARTUP)
        .instanceId("DEBS")
        .version("0.1.5-RELEASE")
        .properties(properties)
        .nodeId("AWS1.RAIN")
        .uri(new URI("http://192.168.0.2/endpoint"))
        .build();
 
TransportMessage<Event> message = new TransportMessage<>(event, Collections.singleton(excluded));
 
// send
sender.send(target, message);
```

#### Event Receiver
##### Configuration
Event Receiver is a Spring Controller with injected ApplicationEventSender instance. To add Event Receiver you need add to project dependencies scm-message-receiver.jar module. If you use Gradle:
```java
repository {
  jcenter()
}

dependencies {
  compile "org.ametiste.scm:scm-message-receiver:{version}
}
```
Than add to component scan package `org.ametiste.scm.messaging.receiver`. After that you have controller in context and can create listeners for processing events. For listening only `TransportMessage<Event>` use `EventTransportMessage` class (it resolve "Type Erasure" problem).

In case when you use library with SpringBoot you can add all needed functionality and settings with starter:
```java
repository {
  jcenter()
}

dependencies {
  compile "org.ametiste.scm:scm-message-receiver-starter:{version}
}
```

##### Supported environment
- JDK 1.8 or higher
- spring-context: 4.2.0.RELEASE or higher

##### Examples of usage
```java
// create event listeners with spring annotations
 
// you can listen all transport messages
@EventListener
public void onTransportMessage(TransportMessage message) {}
 
// you can listen only transport messages with events
@EventListener
public void onEventTransportMessage(EventTransportMessage message) {}
 
// you can listen all events
@EventListener
public void onEvent(Event event) {}
 
// and you can listen concrete event subtype
@EventListener
public void onCustomEventSubtype(InstanceStartupEvent event) {}
```

## Clients

### Startup Event Sender
Event Sender Client designed to simplify process of integration with System Configuration Management system. Clients create events and send it with event sender component to Event Broker. Client use Spring Boot configuration functionality and it involves use in Spring Boot applications.

#### Usage

##### Configuration
To use Event Sender you need add to project dependencies scm-message-sender-client.jar module. If you use Gradle:
```java
repository {
  jcenter()
}

dependencies {
  compile "org.ametiste.scm:scm-message-sender-client:{version}
}
```
Than include to application context configuration class `org.ametiste.scm.messaging.sender.client.config.StartupEventSendConfiguration`.

```java
@Configuration
@Import(StartupEventSendConfiguration.class)
public class ApplicationContext() {
}
```

Or simple use SpringBoot Starter:
```java
repository {
  jcenter()
}

dependencies {
  compile "org.ametiste.scm:scm-message-sender-client-starter:{version}
}
```
All needed configuration automatic will be add to applcaition context.

###### Configuration Properties
Clients has set of properties that must be defined in application.

**HttpClient properties**

| Name  | Type | Description | Default |
|-------|------|-------------|---------|
|`org.ametiste.scm.messaging.sender.client.connect-timeout`|Integer|Connection timeout for HTTP client (in milliseconds)|1000|
|`org.ametiste.scm.messaging.sender.client.read-time`|Integer|Read timeout for HTTP client (in milliseconds)|1000|

_Note_: if parameters not defined default values will be used.

**Bootstrap properties**

| Name  | Type | Description | Default |
|-------|------|-------------|---------|
|**`org.ametiste.scm.messaging.sender.bootstrap.targetUri`**|URI|URI (full path) to target endpoint of event message (Broker)||
|`org.ametiste.scm.messaging.sender.bootstrap.enabled`|boolean|Boolean flag that enabled bootstarp bean creation. If it's false bootstrap will not created and no message sent.|true|
|`org.ametiste.scm.messaging.sender.bootstrap.strict`|boolean|Boolean flag that specify exception handling policy. If it's true bootstrap throw expection when error occurred and service instance fall. If it's false bootstrap catch expection, save incident to log and  nothing signal to rest of service instance.|false|

_Note_: (warning) bold marked properties are required. Other fields may be ommited (default values will be used).

**SCM Info Properties**

To identify instance in SCM system must be set information properties.

| Name  | Type | Description | Default |
|-------|------|-------------|---------|
|**`org.ametiste.scm.instance.info.instanceId`**|String|Uniqe service instance identifier.||
|**`org.ametiste.scm.instance.info.version`**|String|Service software version.||
|`org.ametiste.scm.instance.info.nodeId`|String|Uniqe identifier of node where instance placed.||
|`org.ametiste.scm.instance.info.uri`|URI|URI of instance to communicate.||

_Note_: (warning) bold marked properties are required. Other fields may be ommited and they values stays undefined (null).

###### Supported environment
- JDK 1.8 or higher

#### Logic of operations
##### Structure
Structure of Sender Client shown below in class diargam:
![Sender Client Class Diagram](https://cloud.githubusercontent.com/assets/11256858/11196093/446708a6-8cbe-11e5-957c-3a43f8294d0f.png)

Main actors is:
- *EventSenderClient* - execute send opration;
- *EventFacory* - create event for sending;
- *AppPropertiesAggregator* - gather all available properties for event.

Client starter contains two clients: Startup and Shutdown Event sender clients. Each of them responsible to send signal about some lifecycle change in service instance.

##### Flow (Startup)
1. AppPropertiesAggregator gather properties (logic borrowed from Spring Actuator environment enpoint).
2. All porperites processed through Satinizer that hide all secret fields (password, credentials, etc.).
3. AppPropertiesAggregator return map with properties to EventFactory construction.
4. Startup event client include all dependencies and after creation execute send() method:
  1. create event with EventFactory;
  2. send event with EventSender to target.

This flow repeat every once when instance startup.

##### Flow (Shutdown)
1. On context desctory routine invoke method with `@PreDesctoy` annotation;
2. method call Shutdown Event client `send()` method.

This flow repeat every instance shutdown.

## How To
### Add new event type
This is a short tutorial that describe how to add new event type to system. Its pretty simple, you need only define three classes. In this tutorial wee add `InstanceShutdownEvent` event type. This type represents fact of some service instance shutdown and contains information about instance id (id, version, nodeId, reason).
Let's start:

**1. Create event model**

Create `ShutdownEvent` in *scm-message* module:
```java
public class InstanceShutdownEvent extend Event {
  private final String instanceId;
  private final String version;
  private final String nodeId;
  private final String reason;
  
  ...
}
```
Now SCM now about new event type.

**2. Create transport DTO**

That need to create DTO for thansporting via HTTP:
```java
@JsonTypeName("InstanceShutdownEventDTO")
public class InstanceShutdownEventDTO extends EventDTO {

    private String instanceId;
    private String version;
    private String nodeId;
    private String reason;
    
    ...
```
We add Jackson annotations to provide way for transmitting type info. Add also new subtype to list in annonation in `EventDTO`.

**3. Add mongo document entity**

After we create MongoDB annotated document:
```java
@Document
public class InstanceShutdownEventDocument extends EventDocument {

    private String instanceId;
    private String version;
    private String nodeId;
    private String reason;
    
    ...
```

That all. We add new event type.
