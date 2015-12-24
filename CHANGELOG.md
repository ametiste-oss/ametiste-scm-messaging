# SCM Messaging Library releases and changes #

### v0.1.0

Initial release:
- library documentation;
- base `InstanceLyfeCycle` event (startup, shutdown);
- base send/receive mechanism.

### v0.1.1

Starter logic improvement:
- fix service crash on uri properties is null [#19](https://github.com/ametiste-oss/ametiste-scm-messaging/issues/19);
- service not crash on clients property set `false` [#20](https://github.com/ametiste-oss/ametiste-scm-messaging/issues/20);
- default `targetUri` provided;
- refactoring of `EventFactories`.
