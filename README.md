# Adaptive Distributed Chat Application Using Spring Framework

Adaptive distributed chat application was implemented in java in previous work ([The previously implemented chat application](https://github.com/wbudgaga/distributedChat)). The Spring Framework was used to add adaptive features to that application to address its limitations.

## Problem Description:
In this extension, we addressed the problems and limitations that exist in the original chat application. The problems are:
- The chat application has only one chat server, which means there is a single point of failure. Failing the server leads to the stopping of all its services
- In the original chat application, the chat groups have a fixed size. The group size should be dynamic so that it increases once the activity in the group increases and shrinks otherwise. 
- In the previous implementation, the application does not take advantage of active big groups for investment. Many commercial companies would like to publish their advertisements to reach the highest selling rate in order to increase their revenue.
- In the chat application, the chat server assigns a new chat client to the smallest chat group to balance the sizes among the groups. The problem here is that the chat clients are forced to join particular chat groups. 

## Solution:
To address the mentioned problems with minimum complexity in either design and implementation, I have used the Spring framework. The framework was to add **adaption features** to the original chat application in order to address the limitations in the orginal chat application.


## Adaptive Features
These adaptive features are the key that lets the application to adapt itself based on occurred events and conditions. The following adaptive features have been added to application to solve the menitioned limitations:

* ### Implementing a mirror chat server
To solve the issues associated with a single point of failure in the chat application, I added another server (referred to as a mirror chat server). Both servers should run on different machines. The original chat server launches the mirror server immediately when it starts and keeps updating the mirror server about any changes.
To accomplish this task, I implemented a remote monitoring component that runs on the same mirror server's machine. The role of this component is to keep checking whether the original server is still listening on its port. 
When the monitoring component detects that the original server has  the failed, 
it informs the mirror server that directly adapts itself to become the main chat server. 

* ### Merging small groups into one group
Because the chat clients can join and leave at any time, some chat groups might become very small, which leads to resource wasting. To balance the size of the groups, I implemented a mechanism for merging groups that are staying small for a particular period into one chat group.  I defined two variables--one has been used to define the maximum size below which a group is considered as small and the other variable has been used to define the period length at which a group cannot be small. Both variables are configurable even at run time.
The groups that are considered as small will be monitored for the defined time and then merged if they are still small.

* ### Increasing the maximum size of active groups
A chat group is considered as an active group when the rate of exchanged messages exceeds a conﬁgurable threshold. Once an active group becomes full, its size will be automatically increased to a conﬁgurable size rate.

* ### Sending of advertising messages to active groups
As long as the group is considered as an active group, it will receive from time to time random advertising messages about some new products.

* ### Inviting of chat clients
Each chat client can invite other chat clients using their identiﬁers. If the invited chat client is in another chat group, it will receive the invitation directly and need to take action by accepting or denying the invitation. 
If the invitation is accepted, the invitee will be transferred to the group where the invitation is created. If the invitee does not join the chat application yet, the invitee will be assigned to the chat group of the invitation once the invitee joins the chat application. the transferring of the invitees can be done only when the destination chat group is not full.


## Desgin
The overall design of the original application has not been changed. New components that needed to be managed by the spring framework have been added to the original design. Additionally, callback methods were added to be invoked by the new components to perform appropriate actions.
Many techniques oﬀered by the spring framework were employed to implement the required adaptions. Some of them were scheduling, messaging, event handling, and AOP. The high-level design can be seen in the following figure:

<p align="center">
<img src="https://user-images.githubusercontent.com/40745827/94462492-d7040400-0178-11eb-8797-6a3a4d524651.png" width="550" height="550">
</p>

Three monitoring components (**observer A**, **observer B**, and **observer C**) were added to monitor and trigger actions at different parts of the application. 

- **The observer A** is responsible for managing the activation of the mirror chat server once the main one fails. It immediately activates the mirror chat server to serve the chat clients after detecting the failure of the main chat server. Observer A is a component that contains a collection of beans that are running on the same machine the mirror chat server is running. 
One of these beans is monitoring the chat server by periodically sending heartbeat messages to the chat server. If the main server does not respond, This bean publishes a corresponding event which will be captured by a listener bean on the same machine.
The listener bean calls a specific callback method within the mirror chat server to change its behavior and become the main chat server.

- **The observer B** ....

- **The observer C** ....


## Application Folder
The main folder, distributedChat,  contains :
- makefile: it is used to compile java classes(make all) and remove classes (make clean)
- MANIFEST.MF:  needed to create chatApp.jar
- src: contains java source packages
- bin: 
  - chatApp_lib: contains all needed runtime libraries
	- cs518/a4/distributedchat: contains the compiled java classes in their packages
	- chatApp.jar: Runnable chat application
	- ChatClient.sh: a script that is used to run chat application client and to create chatApp.jar if it does not exist
	- ChatServer.sh: a script that is used to run chat application server and to create chatApp.jar if it does not exist
- lib: contains aspectjrt.jar, aspectj runtime library, that is needed by the application


## Application Behavior
src/cs518/a4/distributedchat/applications/Setting.java includes all configurable variables (such as group size, size threshold to merge groups, and logging folder) that enable changing the behavior of the chat application.


## Scalability
Unlike the naive approach where the central chat server receives a message from a client and broadcasts the message to all other clients, 
The developed application uses a chat server as a central server to bootstrap clients that want to join the chat session. All chat clients are aware of this central server. Every chat client is in a group (called peer group), which has a peer-group ID. The group members have their own member IDs. When a client joins a chat session, the central server informs the client about its peer group ID and group member IDs. Each client communicates with a small set of other clients (peers) and this set is determined by the central server.

When a chat client has a message that needs to be broadcast, it sends the message to all other members in its peer group and also to the central chat server. The chat server sends this message to one client in all other peer groups. That client then forwards the message to the other members existing in its group. This way, all chat clients eventually get the message and the task is distributed instead of relying on just one chat server.




<img src="https://user-images.githubusercontent.com/40745827/94462521-e3885c80-0178-11eb-8757-2af692060085.png" width="500" height="500">

