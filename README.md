# Adaptive Distributed Chat Application Using Spring Framework

Distributed Chat Application was implemented in java; and the Spring Framework was used to add adaptive features to the Chat Application.
The chat application consists of chat clients and server(s) that are Java applications (not applets). The chat application supports that each client can talk to a particular member privately, a group of memebers, and all other members in the system.

The implementaion of this application supports the following features:
- Allowing the chat clients to join and leave the chat sessions at any time without causing any issue
- Builiding chat groups and assigning chat members to different groups
- Detecting the leaving of nodes and rebalancing the chat groups if there is a need.
- Sending three types of massages:
  - Private messaging
  - Group messaging
  - Broadcasting
- Creating GUI to simplify the interactive with the chat application 
- Configuring the application at runtime
- Scalability
- Logging
- Encryption
- Synchronization
- Failure Handling

## Application Behavior
src/cs518/a4/distributedchat/applications/Setting.java includes all configurable variables such as group size, size threshold to merge groups, and logging folder.

## Scalability
Unlike the naive approach where the central chat server receives a message from a client and broadcasts the message to all other clients, 
The developed application uses a chat server as a central server to bootstrap clients that want to join the chat session. All chat clients are aware of this central server. Every chat client is in a group (called peer group), which has a peer-group ID. The group members have their own member IDs. When a client joins a chat session, the central server informs the client about its peer group ID and group member IDs. Each client communicates with a small set of other clients (peers) and this set is determined by the central server.

When a chat client has a message that needs to be broadcast, it sends the message to all other members in its peer group and also to the central chat server. The chat server sends this message to one client in all other peer groups. That client then forwards the message to the other members existing in its group. This way, all chat clients eventually get the message and the task is distributed instead of relying on just one chat server.



<img src="https://user-images.githubusercontent.com/40745827/94462492-d7040400-0178-11eb-8797-6a3a4d524651.png" width="600" height="600">

<img src="https://user-images.githubusercontent.com/40745827/94462521-e3885c80-0178-11eb-8757-2af692060085.png" width="500" height="500">

