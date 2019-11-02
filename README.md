# panda-fix
A Fix Engine written by java, for study purpose

Panda-fix is not going to implementation full features of a fix engine but it is going to implement below key functions
for study purpose:

1. The engine can be started up and initialize a fix session which connects to remote fix engine.
2. The engine can be started up and listen to a fix connection from remote fix engine.
3. Once the fix session is up, it can exchange the heartbeats with the other end of the session, send and handle events
to/from the other fix engine. The events include logon, logout, resend request, sequence reset, test message, reject.
4. The engine can provide an interface to let send and receive application level messages to fulfill the business needs.
5. It only support single fix version which is 4.2
6. It can produce a fix message in tag-value format according to fix specification. It can also parse a fix message into
a java object for application usage.
7. It can maintain the message sequence and data files. It is able to replay if the message sequence number is mismatched.
8. It can load a config file to configure the fix sessions.
9. It can support multiple sessions.