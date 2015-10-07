SCM Messaging Library
====================

Messaging Library is a common part of **System's Configuration Management** system.
Library contains all needed components for organizing messaging between system modules: event and message models,
sender and receiver components.

Module **scm-messaging** contains modules:

* scm-messages
* scm-messages-http
* scm-message-receiver
* scm-message-sender
* scm-message-sender-client

###Module scm-messages
Module _scm-messages_ contains all event models and transport messages used in system.

###Module scm-messages-http
Module _scm-messages-http_ contains event DTOs for transporting via HTTP protocol.
For data serialization used Jackson JSON Processor. This module used by sender and receiver to communicate.

###Module scm-message-receiver
Module _scm-message-receiver_ defines EventSender interface and contains its implementations.
Default implementation used only Apache HTTP Client and can easily integrate to any service.

###Module scm-message-sender
Module _scm-message-sender_ contains event receiver components. Components receive massages using one of the 
transport protocols, transform DTO to events and organize them publishing.
Module designed for using in Spring Applications and for publishing it used Spring _ApplicationEventPublisher_.

###Module scm-message-sender-client
