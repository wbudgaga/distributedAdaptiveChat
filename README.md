# Adaptive Distributed Chat Application Using Spring Framework

Distributed Chat Application was implemented in java. The Spring Framework was used to add adaptive features to the Chat Application.
The chat application consists of chat clients and server(s) that are Java applications (not applets). The chat application supports that each client can talk to all other clients in the system.

## Scalability
Unlike the naive approach where the central chat server receives a message from a client and broadcasts the message to all other clients, 
The developed application uses a chat server as a central server to bootstrap clients that want to join the chat session. All chat clients are aware of this central server. Every chat client is in a group called peer group, which has a peer-group ID. The group members each have their own member IDs. When a client joins a chat session, the central server informs the client about its peer group ID and group member IDs. Each client communicates with a small set of other clients (peers) and this set is determined by the central server.

<img src="https://user-images.githubusercontent.com/40745827/94462492-d7040400-0178-11eb-8797-6a3a4d524651.png" width="600" height="600">

<img src="https://user-images.githubusercontent.com/40745827/94462521-e3885c80-0178-11eb-8757-2af692060085.png" width="500" height="500">

